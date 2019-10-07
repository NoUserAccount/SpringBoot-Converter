package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;

import org.json.JSONException;

public interface ConverterDAO {

	public Connection connect();
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public String assureDate(String datum) throws JSONException;
	public String getJsonFromHNB();
	public String getJsonFromHNB(String date);
	public String loadCurrencyFromDB(String date) throws SQLException ;

	
}
