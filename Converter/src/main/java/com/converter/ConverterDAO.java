package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;

public interface ConverterDAO {

	public DBModel loadDataFromDB(Connection conn, String datum) throws SQLException;
	public Connection connect();
	
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public void tecajRazdoblje(Connection conn) throws JSONException, SQLException;
	public void assureDate(String date, Connection conn, String datum);
	public void populateDropdown(Connection conn);
}
