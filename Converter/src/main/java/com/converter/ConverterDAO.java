package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.util.HashMap;

import org.json.JSONException;

public interface ConverterDAO {

	public HashMap<String,Double> loadDataFromDB(String statement);
	public Connection connect();
	
	public HttpURLConnection urlConnect(String url);
	public StringBuilder downloadHNB(String url);
	public void loadDataModel(StringBuilder response) throws JSONException;
}
