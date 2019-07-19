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
	public ModelAndView racun(@ModelAttribute FormModel form) throws SQLException, JSONException, ParseException {    
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datum = form.getDatum();
		Date date = sdf.parse(datum);
		String fDate = format.format(date);
		String iznos = form.getIznos();
		Connection conn = impl.connect();
		System.out.println(fDate+"  "+iznos);
		impl.assureDate(fDate, conn, datum);			//osigurava valutu u bazi na zadani datum
		dbm = impl.loadDataFromDB(conn, datum);			//puni objekt sa vrijednostima iz baze 

		conn.close();
		return mav;
	}
}