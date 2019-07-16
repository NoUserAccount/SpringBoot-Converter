package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

public interface ConverterDAO {

	public HashMap<String,List<String>> loadDataFromDB(Connection conn, String datum);
	public Connection connect();
	
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public HashMap<String,List<String>> fillDataModel(StringBuilder response) throws JSONException;
	public void tecajRazdoblje(Connection conn) throws JSONException, SQLException;
	public boolean checkDate(String date, Connection conn, String datum);
}
