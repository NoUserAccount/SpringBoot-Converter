package com.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.converter.model.JsonModel;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.json.XML;

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
	// assureDate(String datum) --> ok or error
	// loadCurrencyFromDB(String date) --> fetch curreny response from MySQL

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
	public String getCurrency(String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		Validacije val = new Validacije();
		if (val.validacijaDatuma(date)) {
			String responseFinal = "";
			String day = date.substring(5, 7);
			String month = date.substring(8, 10);
			String year = date.substring(0, 4);
			date = year + "-" + day + "-" + month;
			if ("ok".equals(assureDate(date))) {
				responseFinal = loadCurrencyFromDB(date);
				System.out.println("Tečaj učitan iz baze!");
			}
			return responseFinal;
		} else
			return null;
	}

	@Override
	public String assureDate(String datum)  {
		String sqlCount = "SELECT COUNT(*) FROM Currency WHERE Datum= ?";
		String sqlUpdate = "INSERT INTO Currency (Valuta,Vrijednost,Jedinica,Datum, Drzava) Values (?,?,?,?,?)";
		ResultSet rs = null;
		StringBuilder response = null;
		ConverterDAOImpl impl = new ConverterDAOImpl();
		String valuta = "";
		float vrijednost = 0;
		int jedinica = 0;
		String datumPrimjene = null;
		String drzava = "";
		String dateMod = datum.substring(8, 10) + "." + datum.substring(5, 7) + "." + datum.substring(0, 4);
		String url = "http://api.hnb.hr/tecajn/v1?datum=" + datum;
		JSONArray arr;
		Connection conne = impl.connect();
		int numOfRows = 0;
		try {
			PreparedStatement psCount = conne.prepareStatement(sqlCount);
			psCount.setString(1, dateMod);
			rs = psCount.executeQuery();
			while (rs.next()) {
				numOfRows = rs.getInt(1);
			}
			if (numOfRows != 0) {
				conne.close();
				return "ok";
			} else {
				response = impl.getHNB(url);
				if ("[]".equals(response.toString())) {
					return "wrong date";
				}
				arr = new JSONArray(response.toString());
				PreparedStatement pst = conne.prepareStatement(sqlUpdate);
				pst.setString(1, "HRK");
				pst.setInt(2, 1);
				pst.setFloat(3, 1);
				pst.setString(4, dateMod);
				pst.setString(5, "Hrvatska");
				pst.executeUpdate();
				for (int i = 0; i < arr.length(); i++) {
					datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
					valuta = arr.getJSONObject(i).getString("Valuta");
					vrijednost = Float
							.parseFloat(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
					jedinica = arr.getJSONObject(i).getInt("Jedinica");
					drzava = arr.getJSONObject(i).getString("Država");
					try {
						PreparedStatement ps = conne.prepareStatement(sqlUpdate);
						ps.setString(1, valuta);
						ps.setFloat(2, vrijednost);
						ps.setInt(3, jedinica);
						ps.setString(4, datumPrimjene);
						ps.setString(5, drzava);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Tečaj upisan u bazu!");
				conne.close();
				return "ok";
			}
		} catch (SQLException | JSONException e1) {
			try {
				conne.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			e1.printStackTrace();
		}
		return "error";
	}

	// connecting to URL passed in argument
	@Override
	public HttpURLConnection urlConnect(String url) {
		URL obj = null;
		HttpURLConnection con = null;
		boolean ok = false;
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

	@Override
	public void databaseCleaner() throws SQLException {
		Connection conne = connect();
		String sql = "DELETE FROM Valute";
		Statement st = conne.createStatement();
		st.execute(sql);
		conne.close();
		System.out.println("Baza podataka očišćena!");
	}

	@Override
	public String loadCurrencyFromDB(String date) throws SQLException {
		String dateMod = date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4);
		String sqlSelect = "SELECT Valuta, Jedinica, Vrijednost, Drzava FROM Currency WHERE Datum= ?";
		Connection conne = connect();
		List<String> currencyList = new ArrayList<>();
		List<Float> valuesList = new ArrayList<>();
		List<Integer> unitsList = new ArrayList<>();
		List<String> countryList = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement ps = conne.prepareStatement(sqlSelect);
		ps.setString(1, dateMod);
		rs = ps.executeQuery();
		while (rs.next()) {
			currencyList.add(rs.getString(1));
			unitsList.add(rs.getInt(2));
			valuesList.add(rs.getFloat(3));
			countryList.add(rs.getString(4));
		}
		conne.close();

		String response = "[";
		for (int i = 0; i < currencyList.size(); i++) { // proizvodnja JSON-a na zagorski način
			response += "{\"Srednji\" : \"" + valuesList.get(i) + "\",\n\"Valuta\":\"" + currencyList.get(i)
					+ "\",\n\"Drzava\" : \"" + countryList.get(i) + "\",\n\"Jedinica\" : \"" + unitsList.get(i)
					+ "\"},\n";
		}
		response = response.substring(0, response.length() - 2) + "]";
		return response;
	}

	@Override
	public Connection connect() {
		Connection conne = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/CurrencyConverter?useUnicode=true&"
					+ "useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"
					+ "useSSL=false", "root", "root");
			return conne;
		} catch (Exception e) {
			System.out.println("Konekcija na bazu nije uspostavljena!");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String contactInfo(String name, String surname, String contact, String message) throws SQLException {
		Connection conne = connect();
		String sql = "INSERT INTO Contacts (Name,Surname,Contact,Message) VALUES(?,?,?,?)";
		try {
			PreparedStatement ps = conne.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, surname);
			ps.setString(3, contact);
			ps.setString(4, message);
			ps.execute();
			conne.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Error. Message not sent!";
		} finally {
			if (conne != null) {
				conne.close();
			}
		}
		return "Message sent!";
	}

	@Override
	public JSONArray login(String user, String psw) throws SQLException, JSONException {
		Connection conne = connect();
		String sql = "Select Password from Autorisation where Username = ?";
		JSONObject output = new JSONObject();
		JSONArray array = new JSONArray();
		String approval = "";
		try {
			ResultSet rs = null;
			PreparedStatement ps = conne.prepareStatement(sql);
			ps.setString(1, user);
			rs = ps.executeQuery();
			while(rs.next()) {
				approval = rs.getString(1);
			}
			if(!psw.equals(approval)) {
					output.put("value", "FALSE");
					array.put(output);
			}
			else{
					output.put("value", "OK");
					array.put(output);
			}
			conne.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conne != null) {
				conne.close();
			}
		}
		return array;
	}

	@Override
	public String getMessages() throws SQLException {
		String sqlSelect = "SELECT Name, Surname, Contact, Message FROM Contacts";
		Connection conne = connect();
		List<String> nameList = new ArrayList<>();
		List<String> surnameList = new ArrayList<>();
		List<String> contactList = new ArrayList<>();
		List<String> messageList = new ArrayList<>();
		ResultSet st = conne.createStatement().executeQuery(sqlSelect);
		while (st.next()) {
			nameList.add(st.getString(1));
			surnameList.add(st.getString(2));
			contactList.add(st.getString(3));
			messageList.add(st.getString(4));
		}
		conne.close();

		String response = "[";
		for (int i = 0; i < nameList.size(); i++) { // proizvodnja JSON-a na zagorski način
			response += "{\"Ime\" : \"" + nameList.get(i) + "\",\n\"Prezime\":\"" + surnameList.get(i)
					+ "\",\n\"Contact\" : \"" + contactList.get(i) + "\",\n\"Message\" : \"" + messageList.get(i)
					+ "\"},\n";
		}
		response = response.substring(0, response.length() - 2) + "]";
		return response;	
		}

	@Override
	public String getWeather() {
	String url ="https://vrijeme.hr/hrvatska1_n.xml";
			HttpURLConnection con = null;
			String inputLine = "";
			StringBuilder response = null;
			InputStreamReader sr = null;
			BufferedReader in = null;
			String jsonPrettyPrintString = null;
			int PRETTY_PRINT_INDENT_FACTOR = 4;
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
					JSONObject xmlJSONObj = XML.toJSONObject(response.toString());
		            jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		            JSONObject obj = new JSONObject(new JSONTokener(new StringReader(jsonPrettyPrintString)));
					ObjectMapper mapper = new ObjectMapper();
					JsonNode rootNode = mapper.readTree(obj.toString());
					if (rootNode.iterator().next().get("Grad").isArray()) {
					    for (final JsonNode objNode : rootNode.iterator().next().get("Grad")) {
							JSONObject podatci = new JSONObject();
							podatci = new JSONObject(objNode.get("Podatci").toString());
							if (objNode.get("Podatci").isObject()) {
							        System.out.println(objNode.get("GradIme")+"\t\t\t\t"+podatci.get("Tlak").toString() +"hPa\t"+podatci.getFloat("Temp")+"\t"+   podatci.getString("VjetarSmjer") +"\t"+ podatci.getInt("Vlaga"));
							}
					    }
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return "OK";
	}

}