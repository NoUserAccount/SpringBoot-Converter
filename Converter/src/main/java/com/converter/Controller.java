package com.converter;

import java.sql.SQLException;

import org.json.JSONException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class Controller {

	ModelAndView mav;
	StringBuilder response = null;
	ConverterDAOImpl dao = new ConverterDAOImpl();
	String url = "http://api.hnb.hr/tecajn/v1";

	@RequestMapping("/converter")
	public ModelAndView jspCall() {
		mav = new ModelAndView("firstPage.jsp");
		response = dao.downloadHNB(url);
		try {
			dao.loadDataModel(response);
			dao.tecajRazdoblje();
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return mav;
	}
}