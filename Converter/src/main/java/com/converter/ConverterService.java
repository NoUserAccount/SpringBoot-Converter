package com.converter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ConverterService {

	@Autowired
	public ConverterDAO cDao;
	
	@Autowired
	public Statistika stats;

	public String loadCurrencyFromDB(String date) throws SQLException, JsonProcessingException {
		return cDao.loadCurrencyFromDB(date);
	}
	
	public Connection connect() {
		return cDao.connect();
	}
	
	public HttpURLConnection urlConnect(String url) {
		return cDao.urlConnect(url);
	}
	
	public StringBuilder getHNB(String url) throws JsonParseException, JsonMappingException, IOException {
		return cDao.getURL(url);
	}
	
	public String assureDate(String datum) throws JSONException, JsonParseException, JsonMappingException, IOException{
		return cDao.assureDate(datum);
	}
	
	public String getCurrency(String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cDao.getCurrency(date);
	}
		
	public JSONArray getMostCommonOverall() throws SQLException {
		return stats.getMostCommonOverall();
	}
	
	public JSONArray getMostComonInterval(int interval) throws SQLException {
		return stats.getMostComonInterval(interval);
	}
	
	public String getIntervalStats(int interval, String valuta) throws JsonProcessingException, SQLException {
		return stats.getIntervalStats(interval, valuta);
	}
	
	public String contactInfo(String name, String surname, String contact, String message) throws SQLException {
		return cDao.contactInfo(name, surname, contact, message);
	}
	
	public JSONArray login(String user, String psw) throws SQLException, JSONException {
		return cDao.login(user, psw);
	}

	public String getMessages() throws SQLException {
		return cDao.getMessages();
	}

	public String getWeather() {
		return cDao.getWeather();
	}

	public String getChartData(String date) throws SQLException {
		return cDao.getChartData(date);
	}

	public String getWeatherStatus(String grad) {
		return cDao.getWeatherStatus(grad);
	}

	public String getEarthquake() {
		return cDao.getEarthquake();
	}

	public JSONArray getConverterStats(int mostCommonInterval, int currencyInterval, String currency) {
		return stats.getConverterStatistics(mostCommonInterval, currencyInterval,currency);
	}
	
}

