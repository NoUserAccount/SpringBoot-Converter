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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
			valuteKV.put(valuta, lista);
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

	public void tecajRazdoblje() throws JSONException, SQLException {
		Connection conn;
		StringBuilder response = null;
		ConverterDAOImpl impl = new ConverterDAOImpl();
		String valuta = null;
		double vrijednost = 0;
		double jedinica = 0;
		String datumPrimjene = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String time = sdf.format(date);
		String timeAgo = String.valueOf(Integer.parseInt(time.substring(0, 4)) - 1) + time.substring(4);
		String url = "http://api.hnb.hr/tecajn/v1?datum-od=" + timeAgo + "&datum-do=" + time;
		String check = "SELECT COUNT(Valuta) FROM Valute";
		ResultSet rs;

		conn = impl.connect();
		rs = conn.createStatement().executeQuery(check);
		int o = 0;
		while (rs.next()) {
			o = rs.getInt(1);
		}
		if (o < 1000) {
			response = impl.downloadHNB(url);
			JSONArray arr = new JSONArray(response.toString());
			for (int i = 0; i < arr.length(); i++) {
				datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
				valuta = arr.getJSONObject(i).getString("Valuta");
				vrijednost = Double
						.parseDouble(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
				jedinica = Double.parseDouble(arr.getJSONObject(i).getString("Jedinica"));
				try {
					conn.createStatement()
							.executeUpdate("INSERT INTO Valute (Valuta,Vrijednost,Jedinica,Datum) " + "VALUES ('"
									+ valuta + "','" + vrijednost + "','" + jedinica + "','" + datumPrimjene + "')");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean checkDate(String date, Connection conn) {
		String statement = "SELECT COUNT(*) FROM Valute";
		ResultSet rs = null;
		int numOfRows = 0;
		try {
			rs = conn.createStatement().executeQuery(statement);
			while (rs.next()) {
				numOfRows = rs.getInt(1);
			}
			if (numOfRows != 0) {
				conn.close();
				return true;
			} else {
				StringBuilder response = null;
				ConverterDAOImpl impl = new ConverterDAOImpl();
				String valuta = null;
				double vrijednost = 0;
				double jedinica = 0;
				String datumPrimjene = null;
				String url = "http://api.hnb.hr/tecajn/v1?datum=" + date;

				response = impl.downloadHNB(url);
				JSONArray arr = new JSONArray(response.toString());
				for (int i = 0; i < arr.length(); i++) {
					datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
					valuta = arr.getJSONObject(i).getString("Valuta");
					vrijednost = Double
							.parseDouble(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
					jedinica = Double.parseDouble(arr.getJSONObject(i).getString("Jedinica"));
					try {
						conn.createStatement().executeUpdate(
								"INSERT INTO Valute (Valuta,Vrijednost,Jedinica,Datum) " + "VALUES ('" + valuta + "','"
										+ vrijednost + "','" + jedinica + "','" + datumPrimjene + "')");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException | JSONException e1) {
			System.out.println("");
		}
		return false;
	}
}