package com.converter;

import java.net.HttpURLConnection;
import org.json.JSONException;

public interface ConverterDAO {

	//public Connection connect();
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public void assureDate(String datum) throws JSONException;
	public String getJsonFromHNB();
	public String getJsonFromHNB(String date);

	
}
