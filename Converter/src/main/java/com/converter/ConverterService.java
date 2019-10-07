package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConverterService {

	@Autowired
	public ConverterDAO cDao;
	
	@Autowired
	public Statistika stats;

	public String loadCurrencyFromDB(String date) throws SQLException {
		return cDao.loadCurrencyFromDB(date);
	}
	
	public Connection connect() {
		return cDao.connect();
	}
	
	public HttpURLConnection urlConnect(String url) {
		return cDao.urlConnect(url);
	}
	
	public StringBuilder getHNB(String url) {
		return cDao.getHNB(url);
	}
	
	public String assureDate(String datum) throws JSONException{
		return cDao.assureDate(datum);
	}
	
	public String getJsonFromHNB() {
		return cDao.getJsonFromHNB();
	}
	
	public String getJsonFromHNB(String date) {
		return cDao.getJsonFromHNB(date);
	}
		
	public JSONArray getMostCommonOverall() throws SQLException {
		return stats.getMostCommonOverall();
	}
	
	public JSONArray getMostComonInterval(int interval) throws SQLException {
		return stats.getMostComonInterval(interval);
	}
	
}

