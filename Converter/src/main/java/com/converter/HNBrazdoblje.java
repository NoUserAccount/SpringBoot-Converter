package com.converter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;

public class HNBrazdoblje {

	private Connection conn;
	URL obj = null;
	HttpURLConnection con = null;
	private StringBuilder response = null;
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
	public String check = "SELECT COUNT(Valuta) FROM Valute";
	ResultSet rs;

	public void tecajRazdoblje() throws JSONException, SQLException {
		conn = impl.connect();
		rs = conn.createStatement().executeQuery(check);
		int o = 0;
		while(rs.next()) {
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
}