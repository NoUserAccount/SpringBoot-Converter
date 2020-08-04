package com.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(value = "/jsonOnInit", method = RequestMethod.GET, produces = "application/json")
	public String getJson() {
		System.out.println("radim li išta?");
		return cService.getJsonFromHNB();
	}
	
	@RequestMapping(value = "/weatherOnDemand", method = RequestMethod.GET, produces = "application/json")
	public String getWeather() {
		return cService.getWeather();
	}

	@RequestMapping(value = "/jsonByDate/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String jsonByDate(@PathVariable(value = "date") String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cService.getCurrency(date);
	}
	
	@RequestMapping(value = "/getChartData/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getForChart(@PathVariable(value = "date") String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cService.getChartData(date);
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
	
	@RequestMapping(value = "/currencyUsage/{interval}/{valuta}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getCurrencyUsage(@PathVariable(value = "interval") int interval,
												 @PathVariable(value = "valuta" ) String valuta	) throws SQLException, JsonProcessingException {
		return cService.getIntervalStats(interval, valuta);
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
	
	
	@RequestMapping(value = "/jpa", method = RequestMethod.GET, produces = "application/json")
	public String jpaTest() throws JsonProcessingException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        String datum = "14.11.2019";
		@SuppressWarnings("unchecked")
		List<Object[]> objects = em.createQuery("SELECT Valuta, Jedinica, Vrijednost, Drzava FROM Currency WHERE Datum= :date").setParameter("date", datum).getResultList();
        List<Currency> currencyes = new ArrayList<>(objects.size());
        for(Object[] obj: objects) {
        	currencyes.add(new Currency((String) obj[0], (Integer) obj[1], (float) obj[2], (String) obj[3]));
        }
        String jsonArray = null;
        ObjectMapper mapper = new ObjectMapper();
        jsonArray = mapper.writeValueAsString(currencyes);
        em.clear();
        em.close();
        emf.close();
		return jsonArray;
	}	
	
	@RequestMapping(value = "/weatherStatus/{grad}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getWeatherStatus(
			@PathVariable(value = "grad") String grad) throws SQLException {
		return cService.getWeatherStatus(grad);
	}
	
}