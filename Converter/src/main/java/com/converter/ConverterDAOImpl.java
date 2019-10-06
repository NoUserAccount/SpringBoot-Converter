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
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.converter.model.ErrorModel;
import com.converter.model.JsonModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
@ComponentScan(basePackages = "com.converter")
@EnableAutoConfiguration
public class ConverterDAOImpl implements ConverterDAO {

	@SuppressWarnings("unused")
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// urlConnect(String url) --> connecting to url passed in argument
	// getHNB(String url) --> returning json response from url, HNB API
	// connect() --> returning connection to database - null if fail
	// getJsonFromHNB() --> returning JSON from HNB --- using Jackson
	// getJsonFromHNB(String date) --> JSON from HNB by date
	// assureDate(Connection conn, String datum)

	@Override
	public String getJsonFromHNB() {
		URL url;
		String response = "";
		String responseModified = "";
		String responseFinal = "";
		String insertHRKinJson;

		try {
			url = new URL("http://api.hnb.hr/tecajn/v1");
			ObjectMapper mapper = new ObjectMapper();
			JsonModel[] obj = mapper.readValue(url, JsonModel[].class);
			response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			responseModified = response.replace('ž', 'z').replace("Srednji za devize", "Srednji");
			insertHRKinJson = "{\n\"Srednji\" : \"1\",\n\"Drzava\" : \"Hrvatska\",\n\"Valuta\" : \"HRK\",\n\"Jedinica\" : \"1\"\n}";
			insertHRKinJson = ',' + insertHRKinJson;
			if (responseModified.equals("[ ]")) {
				responseFinal = "[{\n\"Srednji\" : \"1\",\n\"Drzava\" : \"Hrvatska\",\n\"Valuta\" : \"HRK\",\n\"Jedinica\" : \"1\"\n}]";
			} else {
				responseFinal = responseModified.substring(0, responseModified.length() - 1) + insertHRKinJson
						+ responseModified.charAt(responseModified.length() - 1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseFinal;
	}

	@Override
	public String getJsonFromHNB(String date) {
		Validacije val = new Validacije();
		if (val.validacijaDatuma(date)) {
			URL url;
			String response = "";
			String responseModified = "";
			String responseFinal = "";
			String insertHRKinJson;
			String day = date.substring(5, 7);
			String month = date.substring(8, 10);
			String year = date.substring(0, 4);
			date = year + "-" + day + "-" + month;
			try {
				// assureDate(connect(),date);
				url = new URL("http://api.hnb.hr/tecajn/v1?datum=" + date);
				ObjectMapper mapper = new ObjectMapper();
				JsonModel[] obj = mapper.readValue(url, JsonModel[].class);
				response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
				responseModified = response.replace('ž', 'z').replace("Srednji za devize", "Srednji");
				insertHRKinJson = "{\n\"Srednji\" : \"1\",\n\"Drzava\" : \"Hrvatska\",\n\"Valuta\" : \"HRK\",\n\"Jedinica\" : \"1\"\n}";
				insertHRKinJson = ',' + insertHRKinJson;
				if (responseModified.equals("[ ]")) {
					responseFinal = "[{\n\"Srednji\" : \"1\",\n\"Drzava\" : \"Hrvatska\",\n\"Valuta\" : \"HRK\",\n\"Jedinica\" : \"1\"\n}]";
				} else {
					responseFinal = responseModified.substring(0, responseModified.length() - 1) + insertHRKinJson
							+ responseModified.charAt(responseModified.length() - 1);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseFinal;
		} else
			return null;
	}

	@Override
	public void assureDate(String datum) {
		String sqlCount = "SELECT COUNT(*) FROM Valute WHERE Datum= ?";
		String sqlUpdate = "INSERT INTO Valute (Valuta,Vrijednost,Jedinica,Datum) Values (?,?,?,?)";
		ResultSet rs = null;
		StringBuilder response = null;
		ConverterDAOImpl impl = new ConverterDAOImpl();
		String valuta = null;
		float vrijednost = 0;
		int jedinica = 0;
		String datumPrimjene = null;
		String dateMod = datum.substring(8, 10) + "." + datum.substring(5, 7) + "." + datum.substring(0, 4);

		String url = "http://api.hnb.hr/tecajn/v1?datum=" + datum;
		JSONArray arr;
		Connection conne = null;
		int numOfRows = 0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "lozinka1");
			PreparedStatement psCount = conne.prepareStatement(sqlCount);
			psCount.setString(1, dateMod);
			rs = psCount.executeQuery();
			while (rs.next()) {
				numOfRows = rs.getInt(1);
			}
			if (numOfRows != 0) {
				conne.close();
			} else {
				response = impl.getHNB(url);
				arr = new JSONArray(response.toString());
				for (int i = 0; i < arr.length(); i++) {
					datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
					valuta = arr.getJSONObject(i).getString("Valuta");
					vrijednost = Float
							.parseFloat(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
					jedinica = Integer.parseInt(arr.getJSONObject(i).getString("Jedinica"));
					try {
						if (i == 0) {
							PreparedStatement ps = conne.prepareStatement(sqlUpdate);
							ps.setString(1, "HRK");
							ps.setInt(2, 1);
							ps.setFloat(3, 1);
							ps.setString(4, datumPrimjene);
							ps.executeUpdate();
						}
						PreparedStatement ps = conne.prepareStatement(sqlUpdate);
						ps.setString(1, valuta);
						ps.setFloat(2, vrijednost);
						ps.setInt(3, jedinica);
						ps.setString(4, datumPrimjene);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				conne.close();
			}
		} catch (SQLException | JSONException | ClassNotFoundException e1) {
			try {
				conne.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
	}

	// connecting to URL passed in argument
	@Override
	public HttpURLConnection urlConnect(String url) {
		URL obj = null;
		HttpURLConnection con = null;
		ErrorModel em = new ErrorModel();
		boolean ok = false;
		try {
			obj = new URL(url);
			con = (HttpURLConnection) obj.openConnection();
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
		InputStreamReader sr = null;
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

// connect().close() not working 	
//	@Override
//	public Connection connect() {
//		Connection conne = null;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/Imenik?useUnicode=true&"
//					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
//					+ "useSSL=false", "root", "ministar");
//			return conne;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
}