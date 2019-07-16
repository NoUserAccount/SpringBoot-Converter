package com.converter;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {
	
	ErrorModel err = new ErrorModel();
	ModelAndView mav;
	StringBuilder response = null;
	ConverterDAOImpl impl = new ConverterDAOImpl();
	String url = "http://api.hnb.hr/tecajn/v1";

	@RequestMapping("/converter")
	public ModelAndView jspCall() {
		mav = new ModelAndView("firstPage.jsp");
		return mav;
	}
	
	@RequestMapping(value="/konverzija", method = RequestMethod.POST)
	public void racun(@ModelAttribute FormModel form) throws SQLException, JSONException, ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String datum = form.getDatum();
		Date date = sdf.parse(datum);
		String fDate = format.format(date);
		System.out.println(fDate);
		String odredisnaValuta = "EUR";
		float iznos = Float.parseFloat(form.getIznos());
		Connection conn = impl.connect();
		impl.tecajRazdoblje(conn);
		if(!impl.checkDate(fDate, conn, datum)) {
			HashMap<String,List<String>> mapKV= impl.loadDataFromDB(conn, fDate);
			System.out.println("keyset = "+mapKV.keySet());
		}
		conn.close();
	}
	
	
	
}