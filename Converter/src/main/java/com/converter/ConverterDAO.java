package com.converter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Repository
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
	public String getWeatherStatus(String grad);
	public String getEarthquake();
	public String autheticateUser(String username, String password);
	public String addNewBook(String title, String writerLast, String writerFirst, String genre) throws JsonProcessingException;
	public String addNewUser(String admin, String username, String password, String name, String surname,
			String telephone, String address, String email) throws JsonProcessingException;
	public String getBooksList() throws JsonProcessingException;
	public String getLoanedBooks(String user) throws SQLException;
	public String verifyUser(String user) throws SQLException;
	public String loanBook(String user, String book) throws SQLException;
	public String returnBook(String book) throws SQLException;
	public String deleteUser(String user) throws SQLException;
	public String extendLoan(String user, String book, String admin) throws SQLException;
	public String registerNewUser(String name, String surname, String email, String telephone, String address,
			String username, String password) throws SQLException;
	public String getChartData(String date) throws SQLException;
	public String getTvzRss() throws JSONException;
	public String submitScore(String playerOne, String playerTwo, String winner) throws SQLException;
	public String getWeatherSearchHistory() throws JsonProcessingException, SQLException;



	
}
