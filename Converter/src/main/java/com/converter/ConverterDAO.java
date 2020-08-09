package com.converter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ConverterDAO {

	public Connection connect();
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getURL(String url) throws JsonParseException, JsonMappingException, IOException;
	public String assureDate(String datum) throws JSONException, JsonParseException, JsonMappingException, IOException;
	public String getCurrency(String date) throws SQLException, JsonParseException, JsonMappingException, IOException;
	public String loadCurrencyFromDB(String date) throws SQLException, JsonProcessingException ;
	public String contactInfo(String name, String surname, String contact, String message) throws SQLException;
	public JSONArray login(String user, String psw) throws SQLException, JSONException;
	public String getMessages() throws SQLException;
	public String getWeather();
	public String getChartData(String datum) throws SQLException;
	public String getWeatherStatus(String grad);
	String getEarthquake();
	public String autheticateUser(String username, String password);
	public String addNewBook(String title, String writerLast, String writerFirst, String genre) throws JsonProcessingException;
	public String addNewUser(String admin, String username, String password, String name, String surname,
			String telephone, String address) throws JsonProcessingException;
	String getBooksList() throws JsonProcessingException;
	public String getLoanedBooks(String user) throws SQLException;
	public String verifyUser(String user) throws SQLException;



	
}
