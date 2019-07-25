package com.converter;

import java.net.HttpURLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import org.json.JSONException;

import com.converter.model.DBModel;
import com.converter.model.MessageModel;
import com.converter.model.PopulateDropdownModel;

public interface ConverterDAO {

	public DBModel loadDataFromDB(Connection conn, String datum, String valuta) throws SQLException;
	public Connection connect();
	
	public HttpURLConnection urlConnect(String url);
	public StringBuilder getHNB(String url);
	public void tecajRazdoblje(Connection conn) throws JSONException, SQLException;
	public void assureDate(String date, Connection conn, String datum);
	public PopulateDropdownModel populateDropdown(Connection conn, String datum);
	public MessageModel doConversion(int polazna, int odredisna, float polaznaVr, float odredisnaVr, float iznos);
	String fetchJsonJackson();
	DBModel jdbcTemplate(String datum, String valuta);
}
