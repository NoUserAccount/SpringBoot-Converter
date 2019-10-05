package com.converter;

import java.sql.SQLException;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
public class Controller {

	@Autowired
	ConverterService cService;
	
	ConverterDAOImpl impl = new ConverterDAOImpl();
	Statistika stats = new Statistika();

	@RequestMapping(value = "/jsonOnInit", method = RequestMethod.GET, produces = "application/json")
	public String getJson() {
		return impl.getJsonFromHNB();
	}

	@RequestMapping(value = "/jsonByDate/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String jsonByDate(@PathVariable(value = "date") String date) {
		return impl.getJsonFromHNB(date);
	}

	@RequestMapping(value = "/mostCommonStartValue", method = RequestMethod.GET)
	public String getStatsOverall() throws SQLException {
		return stats.getMostCommonOverall().toString();
	}
	

	@RequestMapping(value = "/mostCommonStartValueInterval/{interval}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String  getStatsInterval(@PathVariable(value = "interval") int interval) throws SQLException {
		return stats.getMostComonInterval(interval).toString();
	}
	
	@RequestMapping(value = "/updateStatisticsCounter/{startValue}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String statisticUpdate(@PathVariable(value = "startValue") String value) throws ParseException, SQLException {
		stats.updateCounter(value);
		return value +" updated!";
	}
}