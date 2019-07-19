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
	
	@RequestMapping(value="/converter")
	public ModelAndView jspCall() {
		return mav;
	}

	
	@RequestMapping(value = "/converterSubmited", method = RequestMethod.GET)
	public ModelAndView onSubmitAction(@ModelAttribute FormModel form) throws SQLException, JSONException, ParseException {    
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datum = form.getDatum();								// datum
		Date date = sdf.parse(datum);
		String fDate = format.format(date);
		float iznos = Float.parseFloat(form.getIznos());			// iznos
		String odredisna = form.getValutaO();						// odredisna valuta
		String polazna = form.getValutaP();							// polazna valuta
		MessageModel mm = new MessageModel();
		Connection conn = impl.connect();
		
		impl.assureDate(fDate, conn, datum);						//  osigurava valutu u bazi na zadani datum
		impl.populateDropdown(conn, datum);							//  puni objekt sa vrijednostima za dropdown
		
		dbm = impl.loadDataFromDB(conn, fDate, polazna);			//  puni objekt sa vrijednostima iz baze 
			int jedinicaPolazna = dbm.getJedinica();				// polazna jedinica
			float vrijednostPolazna = dbm.getIznos();				// vrijednost polazne valute
		dbm = impl.loadDataFromDB(conn, datum, odredisna);
			int jedinicaOdredisna = dbm.getJedinica();				// odredisna jedinica
			float vrijednostOdredisna = dbm.getIznos();				// vrijednost polazne valute
		
		mm = impl.doConversion(jedinicaPolazna, jedinicaOdredisna, vrijednostPolazna, vrijednostOdredisna, iznos);  // konverzija i result message
		System.out.println(mm.getMessage());
		conn.close();
		return mav;
	}
	
}