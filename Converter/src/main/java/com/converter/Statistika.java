package com.converter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Statistika {

	public void updateCounter(String polaznaValuta, Connection connection) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String time = sdf.format(date);
		String sql = "INSERT INTO DnevnaStatistika (Valuta,Datum,Counter)\n" + 
				"VALUES(?,?,?)\n" + 								// primary key(Valuta, Datum)
				"ON DUPLICATE KEY UPDATE Counter = Counter + 1;"; 
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, polaznaValuta);
			ps.setString(2, time);
			ps.setString(3, "1");
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getMostCommonOverall(Connection conn) {
		String sql = "SELECT Valuta,SUM(counter) AS Ukupno FROM DnevnaStatistika GROUP BY Valuta ORDER BY total DESC LIMIT 1";
		int result = 0;
		try {
			ResultSet rs = null;
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				result = Integer.parseInt(rs.getString(1));
			}
			conn.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return result;
	}
	
	
	public int getMostComonInterval(Connection connection, int interval) {
		String sql = "select Valuta,sum(counter) as total from DnevnaStatistika WHERE Datum >= DATE(NOW()) - INTERVAL ? DAY group by Valuta order by total desc limit 1";
		ResultSet rs = null;
		int result = 0;
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, interval);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}

