package com.converter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {

	ErrorModel err = new ErrorModel();
	ModelAndView mav = new ModelAndView("firstPage.jsp");;
	StringBuilder response = null;
	ConverterDAOImpl impl = new ConverterDAOImpl();
	String url = "http://api.hnb.hr/tecajn/v1";
	DBModel dbm = new DBModel();

	@RequestMapping(value = "/dateToSpring", method = RequestMethod.GET)
	public void chosenDate(@RequestParam FormModel form) {
		FormModel fm = new FormModel();
		fm.setDatum(form.getDatum()); 												// 1. korak --> puni datum u formModel
	}

	@RequestMapping(value = "/populateDropdown", method = RequestMethod.POST)
	public PopulateDropdownModel PopulateDropdown(@ModelAttribute FormModel form) { // 2.korak --> šalje atribute za
																					// dropDown prema datumu iz prve
																					// metode
		PopulateDropdownModel ppd = new PopulateDropdownModel(); 					// --> osigurava zadani datum u bazi podataka
		Connection conn = impl.connect();
		String odabraniDatum = form.getDatum();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datum = form.getDatum();
		Date date;
		String fDate = null;
		try {
			date = sdf.parse(datum);
			fDate = format.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		impl.assureDate(fDate, conn, datum); 

		ppd = impl.populateDropdown(conn, odabraniDatum);
		return ppd;
	}

	@RequestMapping(value = "/converterSubmited", method = RequestMethod.GET)		// 3. korak --> računa i puni atribut sa porukom o konverziji
	public MessageModel onSubmitAction(@ModelAttribute FormModel form) throws SQLException, JSONException, ParseException {
		MessageModel mm = new MessageModel();
		Connection conn = impl.connect();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datum = form.getDatum(); 						// datum

		Date date = sdf.parse(datum);
		String fDate = format.format(date);
		float iznos = Float.parseFloat(form.getIznos()); 		// iznos
		String odredisna = form.getValutaO(); 					// odredisna valuta
		String polazna = form.getValutaP(); 					// polazna valuta
		Statistika stat = new Statistika();
		stat.updateCounter(polazna, conn);

		dbm = impl.loadDataFromDB(conn, fDate, polazna); 		// puni objekt sa vrijednostima iz baze
		int jedinicaPolazna = dbm.getJedinica(); 				// polazna jedinica
		float vrijednostPolazna = dbm.getIznos(); 				// vrijednost polazne valute
		dbm = impl.loadDataFromDB(conn, datum, odredisna);
		int jedinicaOdredisna = dbm.getJedinica(); 				// odredisna jedinica
		float vrijednostOdredisna = dbm.getIznos(); 			// vrijednost polazne valute
		mm = impl.doConversion(jedinicaPolazna, jedinicaOdredisna, vrijednostPolazna, vrijednostOdredisna, iznos); 
																// konverzija i result message
		conn.close();
		return mm;
	}

	@RequestMapping(value = "/converterStatsOverall", method = RequestMethod.GET)     //
	public String getStatsOverall() {
		Connection conn = impl.connect();
		Statistika stat = new Statistika();
		String statistika = stat.getMostCommonOverall(conn);
		return statistika;
	}

}