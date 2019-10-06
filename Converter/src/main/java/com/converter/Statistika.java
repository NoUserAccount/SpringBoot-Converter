package com.converter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class Statistika {

	ConverterDAOImpl impl = new ConverterDAOImpl();

	public void updateCounter(String polaznaValuta) throws ParseException, SQLException {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String time = sdf.format(date);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date dateStr = format.parse(time);
		java.sql.Date dateDB = new java.sql.Date(dateStr.getTime());
		Connection conne = null;
		String sql = "INSERT INTO DailyStats (Valuta,Datum,Counter)\n" + "VALUES(?,?,?)\n" + // primary key(Valuta,
																								// Datum)
				"ON DUPLICATE KEY UPDATE Counter = Counter + 1;";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "lozinka1");
			PreparedStatement ps = conne.prepareStatement(sql);
			ps.setString(1, polaznaValuta);
			ps.setDate(2, dateDB);
			ps.setString(3, "1");
			ps.execute();
			conne.close();
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (conne != null) {
				conne.close();
			}
		}
	}

	public JSONArray getMostCommonOverall() throws SQLException {
		String sql = "SELECT Valuta, SUM(counter) AS Ukupno FROM DailyStats GROUP BY Valuta ORDER BY Ukupno DESC LIMIT 1";
		String result = "";
		JSONObject output = new JSONObject();
		JSONArray array = new JSONArray();
		Connection conne = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "lozinka1");
			ResultSet rs = null;
			rs = conne.createStatement().executeQuery(sql);
			while (rs.next()) {
				result = rs.getString(1);
			}
			output.put("value", result);
			array.put(output);
			conne.close();
		} catch (SQLException | JSONException | ClassNotFoundException e1) {
			e1.printStackTrace();
		} finally {
			if (conne != null) {
				conne.close();
			}
		}
		return array;
	}

	public JSONArray getMostComonInterval(int interval) throws SQLException {
		String sql = "SELECT Valuta, SUM(counter) AS total FROM DailyStats WHERE Datum >= DATE(NOW()) - INTERVAL ? DAY GROUP BY Valuta ORDER BY total DESC LIMIT 1";
		ResultSet rs = null;
		String result = "";
		JSONObject output = new JSONObject();
		JSONArray array = new JSONArray();
		Connection conne = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "lozinka1");
			PreparedStatement ps = conne.prepareStatement(sql);
			ps.setInt(1, interval);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}
			output.put("value", result);
			array.put(output);
			conne.close();
		} catch (SQLException | JSONException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (conne != null) {
				conne.close();
			}
		}
		return array;
	}
}
