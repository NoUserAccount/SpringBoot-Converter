package com.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Repository;

@Repository
public class ConverterDAOImpl implements ConverterDAO {

	private Connection conn;
	private Connection conne;
	private ResultSet rs;
	HashMap<String, Double> mapKV = new HashMap<String, Double>();
	URL obj = null;
	HttpURLConnection con = null;
	private boolean ok = false;
	private InputStreamReader sr = null;
	private BufferedReader in = null;
	private StringBuilder response = null;
	private String inputLine = null;

	// -------------------------------------------------------------------------------------------------->
	// H T T P
	public HttpURLConnection urlConnect(String url) {
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
			if (con.getResponseCode() == 200) {
				ok = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!ok)
			return null;
		return con;
	}

	public StringBuilder downloadHNB(String url) {
		if ((con = urlConnect(url)) != null) {
			try {
				sr = new InputStreamReader(con.getInputStream());
				in = new BufferedReader(sr);
				response = new StringBuilder();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				sr.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("HTTP konekcija nije uspjela!");
			}
		}
		return response;
	}

	public void loadDataModel(StringBuilder response) throws JSONException {
		JSONArray arr = new JSONArray(response.toString());
		HashMap<String, List<String>> valuteKV = new HashMap<String, List<String>>();
		for (int i = 0; i < arr.length(); i++) {
			List<String> lista = new ArrayList<String>();
			String valuta = arr.getJSONObject(i).getString("Valuta");
			String vrijednost = arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", ".");
			String jedinica = arr.getJSONObject(i).getString("Jedinica");
			String datum = arr.getJSONObject(i).getString("Datum primjene");
			lista.add(vrijednost);
			lista.add(jedinica);
			lista.add(datum);
			valuteKV.put(valuta,lista);
		}
	}

	// -------------------------------------------------------------------------------------------------->
	// D A T A B A S E
	public Connection connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "ministar");
			System.out.println("Konekcija na bazu uspjela!");
			return conne;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, Double> loadDataFromDB(String statement) {
		conn = connect();
		try {
			rs = conn.createStatement().executeQuery(statement);
			while (rs.next()) {
				mapKV.put(rs.getString(1), rs.getDouble(2));
			}
			conn.close();
		} catch (SQLException e1) {
			System.out.println("");
		}
		return mapKV;
	}
}