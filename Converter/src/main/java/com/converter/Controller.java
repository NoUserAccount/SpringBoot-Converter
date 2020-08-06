package com.converter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.converter.jpa.Currency;

@CrossOrigin(origins = "*")
@RestController
public class Controller {

	@Autowired
	ConverterService cService;
	@Autowired
	Statistika stat;

	@RequestMapping(value = "/converter/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String jsonByDate(@PathVariable(value = "date") 
		String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cService.getCurrency(date);
	}

	@RequestMapping(value = "/converterStatistics/{mostCommonInterval}/{currencyInterval}/{currency}", method = RequestMethod.GET, produces = "application/json")
	public String getConverterStatistics(
			@PathVariable(value="mostCommonInterval") int mostCommonInterval,
			@PathVariable(value="currencyInterval") int currencyInterval,
			@PathVariable(value="currency") String currency) throws SQLException {
		return cService.getConverterStats(mostCommonInterval, currencyInterval, currency).toString();
	}

	@RequestMapping(value = "/updateCounter/{startValue}", method = RequestMethod.GET)
	public @ResponseBody String statisticUpdate(@PathVariable(value = "startValue") String value) throws ParseException, SQLException {
		stat.updateCounter(value);
		return value + " updated!";
	}
	
	@RequestMapping(value = "/contact/{name}/{surname}/{contact}/{message}", method = RequestMethod.GET)
	public @ResponseBody String contactInfo(
			@PathVariable(value = "name") String name, 
			@PathVariable(value = "surname") String surname,
			@PathVariable(value = "contact") String contact, 
			@PathVariable(value = "message") String message) throws SQLException {
		return cService.contactInfo(name, surname, contact, message);
	}
	
	@RequestMapping(value = "/weather/{grad}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getWeatherStatus(
			@PathVariable(value = "grad") String grad) throws SQLException {
		return cService.getWeatherStatus(grad);
	}
	
	@RequestMapping(value = "/earthquake", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getEarthquakeData () throws SQLException {
		return cService.getEarthquake();
	}

}