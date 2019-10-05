package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;

import com.converter.model.DBModel;
import com.converter.model.MessageModel;
import com.converter.model.PopulateDropdownModel;

public interface ConverterDAO {

	//public Connection connect();
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public void assureDate(String datum) throws JSONException;
	public String getJsonFromHNB();
	public String getJsonFromHNB(String date);

	
}
