package com.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ConverterDAOImpl implements ConverterDAO {


	// -------------------------------------------------------------------------------------------------->
	// H T T P
	@Override
	public HttpURLConnection urlConnect(String url) {
		URL obj = null;
		HttpURLConnection con = null;
		ErrorModel em = new ErrorModel();
		boolean ok = false;
		try {
			obj = new URL(url);
			con  = (HttpURLConnection) obj.openConnection();
			if (con.getResponseCode() == 200) {
				ok = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			em.setErrorMessage("Nije moguće uspostaviti http konekciju!");
		}
		if (!ok)
			return null;
		return con;
	}
	@Override
	public StringBuilder getHNB(String url) {
		HttpURLConnection con = null;
		String inputLine = "";
		StringBuilder response = null;
		InputStreamReader sr = null;;
		BufferedReader in = null;
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
			}
		}
		return response;
	}


	// -------------------------------------------------------------------------------------------------->
	// D A T A B A S E
	@Override
	public Connection connect() {
		Connection conne = null;
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

	@Override
	public DBModel loadDataFromDB(Connection conn, String datum, String valuta) throws SQLException {
		String sql = "SELECT Valuta,Vrijednost,Jedinica FROM Valute WHERE Datum= ? AND Valuta = ?";
		ResultSet rs = null;
		DBModel dbm = new DBModel();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, datum);
			ps.setString(2, valuta);
			rs = ps.executeQuery();
			while (rs.next()) {
				dbm.setValuta(rs.getString(1));
				dbm.setJedinica(rs.getInt(2));
				dbm.setIznos(rs.getFloat(3));
			}
		} catch (SQLException e1) {
		}
		return dbm;
	}

	@Override
	public void tecajRazdoblje(Connection conn) throws JSONException, SQLException {
		StringBuilder response = null;
		ConverterDAOImpl impl = new ConverterDAOImpl();
		String valuta = null;
		float vrijednost = 0;
		int jedinica = 0;
		String datumPrimjene = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());
		String time = sdf.format(date);
		String timeAgo = String.valueOf(Integer.parseInt(time.substring(0, 4)) - 1) + time.substring(4);
		String url = "http://api.hnb.hr/tecajn/v1?datum-od=" + timeAgo + "&datum-do=" + time;
		String sqlCount = "SELECT COUNT(Valuta) FROM Valute";
		String sqlUpdate = "INSERT INTO Valute (Valuta, Vrijednost, Jedinica, Datum) VALUES (????)";
		ResultSet rs;

		conn = impl.connect();
		rs = conn.createStatement().executeQuery(sqlCount);
		int o = 0;
		while (rs.next()) {
			o = rs.getInt(1);
		}
		if (o < 1000) {
			response = impl.getHNB(url);
			JSONArray arr = new JSONArray(response.toString());
			for (int i = 0; i < arr.length(); i++) {
				datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
				valuta = arr.getJSONObject(i).getString("Valuta");
				vrijednost = Float.parseFloat(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
				jedinica = Integer.parseInt(arr.getJSONObject(i).getString("Jedinica"));
				try {
					PreparedStatement ps = conn.prepareStatement(sqlUpdate);
					ps.setString(1, valuta);
					ps.setFloat(2, vrijednost);
					ps.setInt(3, jedinica);
					ps.setString(4, datumPrimjene);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void assureDate(String date, Connection conn, String datum) {
		String sqlCount = "SELECT COUNT(*) FROM Valute WHERE Datum= ?";
		String sqlUpdate = "INSERT INTO Valute (Valuta,Vrijednost,Jedinica,Datum) Values (?,?,?,?)";
		ResultSet rs = null;
		StringBuilder response = null;
		ConverterDAOImpl impl = new ConverterDAOImpl();
		String valuta = null;
		float vrijednost = 0;
		int jedinica = 0;
		String datumPrimjene = null;
		String url = "http://api.hnb.hr/tecajn/v1?datum=" + datum;
		JSONArray arr;

		int numOfRows = 0;
		try {
			PreparedStatement psCount = conn.prepareStatement(sqlCount);
			psCount.setString(1, date);
			rs = psCount.executeQuery();
			while (rs.next()) {
				numOfRows = rs.getInt(1);
			}
			if (numOfRows != 0) {
				conn.close();
				System.out.println("NumOfRows=true="+numOfRows);
			} else {
				System.out.println("NumOfRows=false="+numOfRows);
				response = impl.getHNB(url);
				ObjectMapper mapper = new ObjectMapper();
				
				arr = new JSONArray(response.toString());
				for (int i = 0; i < arr.length(); i++) {
					datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
					valuta = arr.getJSONObject(i).getString("Valuta");
					vrijednost = Float
							.parseFloat(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
					jedinica = Integer.parseInt(arr.getJSONObject(i).getString("Jedinica"));
					try {
						if (i == 0) {
							PreparedStatement ps = conn.prepareStatement(sqlUpdate);
							ps.setString(1, "HRK");
							ps.setInt(2, 1);
							ps.setFloat(3, 1);
							ps.setString(4, datumPrimjene);
							ps.executeUpdate();
						}
						PreparedStatement ps = conn.prepareStatement(sqlUpdate);
						ps.setString(1, valuta);
						ps.setFloat(2, vrijednost);
						ps.setInt(3, jedinica);
						ps.setString(4, datumPrimjene);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException | JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void populateDropdown(Connection conn, String datum) {
		String sql = "SELECT Valuta FROM Valute WHERE Datum= ?";
		ResultSet rs = null;
		PopulateDropdownModel pdd = new PopulateDropdownModel();
		List<String> populateDDarr = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, datum);
			rs = ps.executeQuery();
			while (rs.next()) {
				populateDDarr.add(rs.getString(1));
			}
			pdd.setPopulateDD(populateDDarr);
		} catch (SQLException e1) {
		}
	}
	
	@Override
	public MessageModel doConversion(int polaznaJed, int odredisnaJed, float polaznaVr, float odredisnaVr, float iznos) {
		MessageModel mm = new MessageModel();
		DecimalFormat df = new DecimalFormat("#0.00");
		float sum = ((polaznaVr / polaznaJed) * iznos) / (odredisnaVr / odredisnaJed);
		String formated = (df.format(sum));
		
		mm.setMessage("Iznos konverzije: "+formated);
		return mm;
	}
}