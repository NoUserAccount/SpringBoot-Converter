package com.converter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "*")
@RestController
public class Controller {

	@Autowired
	ConverterService cService;
	@Autowired
	Statistika stat;
	
	@RequestMapping(value = "/jsonOnInit", method = RequestMethod.GET, produces = "application/json")
	public String getJson() {
		return cService.getJsonFromHNB();
	}

	@RequestMapping(value = "/jsonByDate/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String jsonByDate(@PathVariable(value = "date") String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cService.getCurrency(date);
	}

	@RequestMapping(value = "/mostCommonStartValue", method = RequestMethod.GET, produces = "application/json")
	public String getStatsOverall() throws SQLException {
		return cService.getMostCommonOverall().toString();
	}

	@RequestMapping(value = "/mostCommonStartValueInterval/{interval}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getStatsInterval(@PathVariable(value = "interval") int interval) throws SQLException {
		return cService.getMostComonInterval(interval).toString();
	}

	@RequestMapping(value = "/updateStatisticsCounter/{startValue}", method = RequestMethod.GET)
	public @ResponseBody String statisticUpdate(@PathVariable(value = "startValue") String value) throws ParseException, SQLException {
		stat.updateCounter(value);
		return value + " updated!";
	}
	
	@RequestMapping(value = "/assureDate/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String assureDate(@PathVariable(value = "date") String date) throws JsonParseException, JsonMappingException, IOException {
		ConverterDAOImpl impl = new ConverterDAOImpl();
		return impl.assureDate(date);
	}
	
	@RequestMapping(value = "/contactInfo/{name}/{surname}/{contact}/{message}", method = RequestMethod.GET)
	public @ResponseBody String contactInfo(
			@PathVariable(value = "name") String name, 
			@PathVariable(value = "surname") String surname,
			@PathVariable(value = "contact") String contact, 
			@PathVariable(value = "message") String message) throws SQLException {
		return cService.contactInfo(name, surname, contact, message);
	}
	
	@RequestMapping(value = "/login/{user}/{psw}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String login(
			@PathVariable(value = "user") String user, 
			@PathVariable(value = "psw") String psw) throws SQLException, JSONException {
		return cService.login(user, psw).toString();
	}
	
	@RequestMapping(value = "/getMessages", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getMessages() throws SQLException {
		return cService.getMessages();
	}
}