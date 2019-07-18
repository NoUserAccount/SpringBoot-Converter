package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

public interface ConverterDAO {

	public DBModel loadDataFromDB(Connection conn, String datum);
	public Connection connect();
	
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public HashMap<String,List<String>> populateDataModel(StringBuilder response) throws JSONException;
	public void tecajRazdoblje(Connection conn) throws JSONException, SQLException;
	public void assureDate(String date, Connection conn, String datum);
	public void populateDropdown(Connection conn);
}
