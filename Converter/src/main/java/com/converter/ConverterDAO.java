package com.converter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ConverterDAO {

	public Connection connect();
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url) throws JsonParseException, JsonMappingException, IOException;
	public String assureDate(String datum) throws JSONException, JsonParseException, JsonMappingException, IOException;
	public String getJsonFromHNB();
	public String getCurrency(String date) throws SQLException, JsonParseException, JsonMappingException, IOException;
	public String loadCurrencyFromDB(String date) throws SQLException ;
	public void databaseCleaner() throws SQLException;
	public String contactInfo(String name, String surname, String contact, String message) throws SQLException;
	public JSONArray login(String user, String psw) throws SQLException, JSONException;
	public String getMessages() throws SQLException;
	public String getWeather();
	
}
