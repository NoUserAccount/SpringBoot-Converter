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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.converter.jpa.Autorisation;
import com.converter.jpa.Bookshelf;
import com.converter.jpa.Currency;
import com.converter.jpa.Weather;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.XML;

@Repository
@EnableTransactionManagement
@ComponentScan(basePackages = "com.converter")
@EnableAutoConfiguration
public class ConverterDAOImpl implements ConverterDAO {

	@Override
	public String getCurrency(String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		Validacije val = new Validacije();
		String responseFinal = "";
		JSONObject output = new JSONObject();
		JSONArray array = new JSONArray();
		output.put("Srednji", 1);
		output.put("Drzava", "Hrvatska");
		output.put("Valuta", "HRK");
		output.put("Jedinica", 1);
		output.put("SelectOption", "HRK Hrvatska");
		array.put(output);
		if (val.validacijaDatuma(date)) {
			if ("ok".equals(assureDate(date))) {
				responseFinal = loadCurrencyFromDB(date);
				return responseFinal;
			} else {
				return array.toString();
			}
		} else {
			return array.toString();
		}
	}

	@Override
	public String assureDate(String datum) {
		String sqlCount = "SELECT COUNT(*) FROM Currency WHERE Datum= ?";
		String sqlUpdate = "INSERT INTO Currency (Valuta,Vrijednost,Jedinica,Datum, Drzava, SelectOption) Values (?,?,?,?,?,?)";
		ResultSet rs = null;
		StringBuilder response = null;
		String valuta = "";
		float vrijednost = 0;
		int jedinica = 0;
		String datumPrimjene = null;
		String drzava = "";
		String option = "";
		String dateMod = datum.substring(8, 10) + "." + datum.substring(5, 7) + "." + datum.substring(0, 4);
		String url = "http://api.hnb.hr/tecajn/v1?datum=" + datum;
		JSONArray array;
		Connection conne = connect();
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
				response = getURL(url);
				if ("[]".equals(response.toString())) {
					return "wrong date";
				}
				array = new JSONArray(response.toString());
				String jsonArray = array.toString().replaceAll("ž", "z");
				JSONArray arr = new JSONArray(jsonArray);
				PreparedStatement pst = conne.prepareStatement(sqlUpdate);
				pst.setString(1, "HRK");
				pst.setInt(2, 1);
				pst.setFloat(3, 1);
				pst.setString(4, dateMod);
				pst.setString(5, "Hrvatska");
				pst.setString(6, "HRK Hrvatska");
				pst.executeUpdate();
				for (int i = 0; i < arr.length(); i++) {
					datumPrimjene = arr.getJSONObject(i).getString("Datum primjene");
					valuta = arr.getJSONObject(i).getString("Valuta");
					vrijednost = Float
							.parseFloat(arr.getJSONObject(i).getString("Srednji za devize").replaceFirst(",", "."));
					jedinica = arr.getJSONObject(i).getInt("Jedinica");
					drzava = arr.getJSONObject(i).getString("Drzava");
					option = valuta + " " + drzava;
					try {
						PreparedStatement ps = conne.prepareStatement(sqlUpdate);
						ps.setString(1, valuta);
						ps.setFloat(2, vrijednost);
						ps.setInt(3, jedinica);
						ps.setString(4, datumPrimjene);
						ps.setString(5, drzava);
						ps.setString(6, option);
						ps.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
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
	public StringBuilder getURL(String url) {
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
	public String loadCurrencyFromDB(String date) throws SQLException, JsonProcessingException {
		String jsonArray = null;
		ObjectMapper mapper = new ObjectMapper();
		String dateMod = date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4);
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
		EntityManager manager = emf.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Object[]> objects = manager
				.createQuery(
						"SELECT Valuta, Jedinica, Vrijednost, Drzava, SelectOption FROM Currency WHERE Datum= :date")
				.setParameter("date", dateMod).getResultList();
		List<Currency> currencyes = new ArrayList<>(objects.size());
		for (Object[] obj : objects) {
			currencyes.add(
					new Currency((String) obj[0], (Integer) obj[1], (float) obj[2], (String) obj[3], (String) obj[4]));
		}
		jsonArray = mapper.writeValueAsString(currencyes).replaceAll("vrijednost", "Srednji")
				.replaceAll("drzava", "Drzava").replaceAll("jedinica", "Jedinica").replaceAll("valuta", "Valuta")
				.replaceAll("selectOption", "SelectOption");
		manager.close();
		emf.close();
		return jsonArray;
	}

	@Override
	public String getChartData(String date) throws SQLException {
		String dateMod = date.substring(8, 10) + "." + date.substring(5, 7) + "." + date.substring(0, 4);
		String sqlSelect = "SELECT Jedinica, Vrijednost, Valuta FROM Currency WHERE Datum= ?";
		Connection conne = connect();
		ResultSet rs = null;
		PreparedStatement ps = conne.prepareStatement(sqlSelect);
		ps.setString(1, dateMod);
		rs = ps.executeQuery();
		JSONArray array = new JSONArray();
		while (rs.next()) {
			JSONObject output = new JSONObject();
			output.put("Srednji", rs.getFloat(2) / rs.getInt(1));
			output.put("Valuta", rs.getString(3));
			array.put(output);
		}
		conne.close();
		return array.toString();
	}

	@Override
	public Connection connect() {
		Connection conne = null;
		try {
			// Class.forName("com.mysql.cj.jdbc.Driver");
			conne = DriverManager.getConnection("jdbc:mysql://localhost:3306/currencyConverter?useUnicode=true&"
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
		EmailService es = new EmailService();
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
		String RECIPIENT = "madagasakaria@gmail.com";
		String SUBJECT = "Contact form message";
		String MAIL_MESSAGE = message + "\n\n" + name + " " + surname + "\n\n" + contact;
		es.emailClient(RECIPIENT, SUBJECT, MAIL_MESSAGE);
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
			while (rs.next()) {
				approval = rs.getString(1);
			}
			if (!psw.equals(approval)) {
				output.put("value", "FALSE");
				array.put(output);
			} else {
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
		ResultSet rs = conne.createStatement().executeQuery(sqlSelect);
		JSONArray array = new JSONArray();
		while (rs.next()) {
			JSONObject output = new JSONObject();
			output.put("Ime", rs.getString(1));
			output.put("Prezime", rs.getString(2));
			output.put("Contact", rs.getString(3));
			output.put("Message", rs.getString(4));
			array.put(output);
		}
		conne.close();
		return array.toString();
	}

	@Override
	public String getWeather() {
		String url = "https://vrijeme.hr/hrvatska1_n.xml";
		HttpURLConnection con = null;
		String inputLine = "";
		StringBuilder response = null;
		InputStreamReader sr = null;
		BufferedReader in = null;
		String jsonPrettyPrintString = null;
		JSONArray array = new JSONArray();
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
							String grad = objNode.get("GradIme").toString().substring(1,
									objNode.get("GradIme").toString().length() - 1);
							JSONObject output = new JSONObject();
							output.put("Grad", grad);
							output.put("Temperatura", podatci.get("Temp").toString());
							output.put("Tlak", podatci.get("Tlak").toString());
							output.put("TlakTend", podatci.get("TlakTend").toString());
							output.put("VjetarSmjer", podatci.get("VjetarSmjer").toString());
							output.put("VjetarBrzina", podatci.get("VjetarBrzina").toString());
							output.put("Vrijeme", podatci.get("Vrijeme").toString());
							array.put(output);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return array.toString();
	}

	@Override
	public String getWeatherStatus(String grad) {
		LocalDate today = LocalDate.now();
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		today.format(formater);
		ObjectMapper mapper = new ObjectMapper();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
		EntityManager manager = emf.createEntityManager();
		JSONObject obj;
		String url = "http://api.weatherstack.com/current?access_key=1f87991019336577bfd3c34fd77644ba&query="
				+ grad.replace(" ", "%20");
		String response;
		JSONObject output = new JSONObject();
		JSONArray array = new JSONArray();
		String select = "SELECT COUNT(*) FROM Weather where dates = ? and querye LIKE ?";
		String jsonArray = "empty";
		Connection conn = connect();
		try {
			PreparedStatement ps = conn.prepareStatement(select);
			ps.setString(1, today.toString());
			ps.setString(2, "%" + grad + "%");
			ResultSet rs;
			rs = ps.executeQuery();
			int numOfRows = 0;
			while (rs.next()) {
				numOfRows = rs.getInt(1);
			}
			if (numOfRows == 0) {
				if ((response = getURL(url).toString()) != null) {
					obj = new JSONObject(response.toString());
					JSONObject request = new JSONObject();
					try {
						request = obj.getJSONObject("request");
					} catch (Exception e) {
						jsonArray = null;
						return jsonArray;
					}
					JSONObject location = obj.getJSONObject("location");
					JSONObject current = obj.getJSONObject("current");
					output.put("query", request.getString("query"));
					output.put("region", location.getString("region"));
					output.put("observationTime", current.getString("observation_time"));
					output.put("temperature", current.get("temperature").toString());
					output.put("description", current.getJSONArray("weather_descriptions").getString(0));
					output.put("weatherIcon", current.getJSONArray("weather_icons").getString(0));
					output.put("windSpeed", current.get("wind_speed").toString());
					output.put("windDirection", current.get("wind_dir").toString());
					output.put("pressure", current.get("pressure").toString());
					output.put("precipitation", current.get("precip").toString());
					output.put("humidity", current.get("humidity").toString());
					output.put("cloudcover", current.get("cloudcover").toString());
					output.put("feelsLike", current.get("feelslike").toString());
					output.put("uvIndex", current.get("uv_index").toString());
					output.put("visibility", current.get("visibility").toString());
					output.put("isDay", current.get("is_day").toString());
					array.put(output);
					jsonArray = array.toString();

					try {
						manager.getTransaction().begin();
						Weather weather = new Weather(request.getString("query"), location.getString("region"),
								current.getString("observation_time"), current.get("temperature").toString(),
								current.getJSONArray("weather_descriptions").getString(0),
								current.getJSONArray("weather_icons").getString(0),
								current.get("wind_speed").toString(), current.get("wind_dir").toString(),
								current.get("pressure").toString(), current.get("precip").toString(),
								current.get("humidity").toString(), current.get("cloudcover").toString(),
								current.get("feelslike").toString(), current.get("uv_index").toString(),
								current.get("visibility").toString(), current.get("is_day").toString(),
								today.toString());
						manager.persist(weather);
						manager.getTransaction().commit();
						manager.close();
						emf.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					jsonArray = null;
				}
			} else {
				@SuppressWarnings("unchecked")
				List<Object[]> objects = manager.createQuery(
						"select querye, region, observationTime,temperature, descriptions, weatherIcon, windSpeed, windDirection,"
								+ " pressure, precipitation, humidity, cloudcover, feelsLike, uvIndex, visibility, isDay, dates "
								+ "from Weather where dates = :datum and querye like :grad")
						.setParameter("datum", today.toString()).setParameter("grad", "%" + grad + "%").getResultList();
				List<Weather> forecast = new ArrayList<>(objects.size());
				for (Object[] o : objects) {
					forecast.add(new Weather((String) o[0], (String) o[1], (String) o[2], (String) o[3], (String) o[4],
							(String) o[5], (String) o[6], (String) o[7], (String) o[8], (String) o[9], (String) o[10],
							(String) o[11], (String) o[12], (String) o[13], (String) o[14], (String) o[15],
							(String) o[16]));
				}
				jsonArray = mapper.writeValueAsString(forecast);
				manager.close();
				emf.close();
				conn.close();
			}

		} catch (SQLException | JsonProcessingException e1) {
			e1.printStackTrace();
		}
		return jsonArray;
	}

	@Override
	public String getEarthquake() {
		LocalDate today = LocalDate.now();
		LocalDate daysAgo = LocalDate.now().minusDays(365);
		String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=" + daysAgo
				+ "&endtime=" + today + "&"
				+ "minmagnitude=0&minlongitude=13.040373&maxlongitude=19.944292&minlatitude=41.101707&maxlatitude=47.239711";
		SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		String response;
		JSONObject obj;
		JSONObject properties = new JSONObject();
		JSONObject singlePropertie = new JSONObject();
		JSONObject singleGeometry = new JSONObject();
		JSONArray array = new JSONArray();
		JSONArray features = new JSONArray();
		if ((response = getURL(url).toString()) != null) {
			obj = new JSONObject(response.toString());
			try {
				features = obj.getJSONArray("features");
			} catch (JSONException json) {
				return "[]";
			}

			for (int i = 0; i < features.length(); i++) {
				properties = features.getJSONObject(i);
				singlePropertie = properties.getJSONObject("properties");
				singleGeometry = properties.getJSONObject("geometry");
				JSONObject output = new JSONObject();
				output.put("place", singlePropertie.get("place").toString());
				output.put("magnitude", singlePropertie.get("mag").toString());
				Timestamp stamp = new Timestamp((long) singlePropertie.get("time"));
				Date date = new Date(stamp.getTime());
				output.put("time", dt1.format(date).toString());
				String felt = singlePropertie.get("felt").toString();
				if ("null".equals(felt) || "".equals(felt)) {
					felt = "no reports";
				}
				output.put("DYFI", felt);
				String alert = singlePropertie.get("alert").toString();
				if ("null".equals(alert) || "".equals(alert)) {
					alert = "white";
				}
				output.put("alert", alert);
				output.put("url", singlePropertie.get("url"));
				output.put("longitude", singleGeometry.getJSONArray("coordinates").get(0).toString());
				output.put("latitude", singleGeometry.getJSONArray("coordinates").get(1).toString());
				output.put("depth", singleGeometry.getJSONArray("coordinates").get(2).toString());
				array.put(output);
			}

		}
		return array.toString();
	}

	@Override
	public String autheticateUser(String username, String password) {
		username = username.replaceAll(" ", "");
		password = password.replaceAll(" ", "");
		try {
			String users = "select uid from autorisation";
			String update = "update currencyconverter.Bookshelf set Warning = ? where UID = ? and BID = ?";
			String sql = "select\n"
					+ " tab.UID, tab.BID, tab.FirstName, tab.LastName, tab.BookTitle, tab.AuthorLastName,\n"
					+ "  tab.IssuedDate, tab.Period, tab.FINE , tab.Email, tab.Warning\n" + "from (\n"
					+ "SELECT Autorisation.UID, Bookshelf.BookTitle, Bookshelf.AuthorLastName, \n"
					+ "Autorisation.FirstName, Autorisation.LastName, Bookshelf.BID, Bookshelf.IssuedDate, \n"
					+ "Bookshelf.Period, Bookshelf.FINE, Autorisation.Email , Bookshelf.Warning\n"
					+ "FROM Autorisation\n" + "INNER JOIN Bookshelf \n" + "ON Autorisation.UID = Bookshelf.UID\n"
					+ ") tab;";
			Connection conne = connect();
			PreparedStatement st = conne.prepareStatement(users);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				getLoanedBooks(rs.getString(1));
			}
			PreparedStatement ps = conne.prepareStatement(sql);
			ResultSet resultSet = ps.executeQuery();
			EmailService es = new EmailService();
			while (resultSet.next()) {
				float fine = Float.valueOf(resultSet.getString(9));
				String warning = "";
				if (fine != 0) {
					String name = resultSet.getString(3) + " " + resultSet.getString(4);
					String book = resultSet.getString(5) + ", " + resultSet.getString(6);
					String issued = resultSet.getString(7);
					String warningDB = resultSet.getString(11);
					boolean execute = true;
					String EMAIL = resultSet.getString(10);
					String SUBJECT = "Library Bookshelf - delay notice";
					String EMAIL_TEXT = "Dear " + name + ",\n\n"
							+ "we inform you that you are late in returning the book that you borowed on " + issued
							+ ", " + book + "\nand your debit is " + fine + "kn and rising 0,50kn/day."
							+ "\n\n\nSincerely, \n\nlibrary Bookshelf!";
					if (fine >= 0.50 && fine < 15 && (warningDB == null || warningDB.contentEquals(""))) {
						warning = "SENT";
						es.emailClient(EMAIL, SUBJECT, EMAIL_TEXT);
					} else if (fine >= 15.0 && fine <= 30
							&& (warningDB.contentEquals("SENT") || warningDB.contentEquals("") || warningDB == null)) {
						warning = "FIRST";
						es.emailClient(EMAIL, SUBJECT, EMAIL_TEXT);
					} else if (fine > 30 && fine <= 50 && (warningDB.contentEquals("FIRST")
							|| warningDB.contentEquals("SENT") || warningDB.contentEquals("") || warningDB == null)) {
						warning = "SECOND";
						es.emailClient(EMAIL, SUBJECT, EMAIL_TEXT);
					} else if (fine > 50 && (warningDB.contentEquals("SECOND") || warningDB.contentEquals("FIRST")
							|| warningDB.contentEquals("SENT") || warningDB.contentEquals("") || warningDB == null)) {
						warning = "THIRD";
						es.emailClient(EMAIL, SUBJECT, EMAIL_TEXT);
					} else {
						execute = false;
					}
					if (execute) {
						PreparedStatement statement = conne.prepareStatement(update);
						statement.setString(1, warning);
						statement.setString(2, resultSet.getString(1));
						statement.setString(3, resultSet.getString(2));
						statement.executeUpdate();
						statement.close();
					}
				}
			}
			conne.close();
			st.close();
			rs.close();
			resultSet.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String resultArray = null;
		ObjectMapper mapper = new ObjectMapper();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
		EntityManager manager = emf.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Object[]> objects = manager.createQuery(
				"SELECT UID, Administrator, Username, Passwords, FirstName, LastName, Telephone, Address FROM Autorisation WHERE Username= :username AND Passwords= :password")
				.setParameter("username", username).setParameter("password", password).getResultList();
		List<Autorisation> user = new ArrayList<>(objects.size());
		for (Object[] obj : objects) {
			user.add(new Autorisation((String) obj[0], (String) obj[1], (String) obj[2], (String) obj[3],
					(String) obj[4], (String) obj[5], (String) obj[6], (String) obj[7]));
		}
		try {
			if (user.toString().equals("") || user.toString() == null || user.toString().equals("[]")) {
				user.add(new Autorisation("false", "false", "false", "false", "false", "false", "false", "false"));
			}
			resultArray = mapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} finally {
			manager.close();
			emf.close();
		}

		return resultArray;
	}

	@Override
	public String addNewBook(String title, String writerLast, String writerFirst, String genre)
			throws JsonProcessingException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
		EntityManager manager = emf.createEntityManager();
		String BID = "000000000";
		JSONObject object = new JSONObject();
		while (true) {
			BID = Long.toString((long) ((Math.random() * (1000000000 - 99999999)) + 99999999));
			@SuppressWarnings("unchecked")
			List<Object[]> objects = manager.createQuery("SELECT BID FROM Bookshelf WHERE BID = :bid")
					.setParameter("bid", BID).getResultList();
			List<Bookshelf> book = new ArrayList<>(objects.size());
			for (Object[] obj : objects) {
				book.add(new Bookshelf((String) obj[0]));
			}
			if (book.size() == 0) {
				break;
			}
		}
		try {
			manager.getTransaction().begin();
			Bookshelf newBook = new Bookshelf(BID, title, writerLast, writerFirst, genre, "in library");
			object.put("id", BID);
			manager.persist(newBook);
			manager.getTransaction().commit();
			manager.close();
			emf.close();
		} catch (Exception e) {
			object.put("id", "0");
		}

		return object.toString();
	}

	@Override
	public String addNewUser(String admin, String username, String password, String name, String surname,
			String telephone, String address, String email) throws JsonProcessingException {
		admin = admin.contentEquals("administrator") ? "true" : "false";
		username = username.replaceAll(" ", "");
		password = password.replaceAll(" ", "");
		email = email.replaceAll(" ", "");
		String UID = Long.toString((long) ((Math.random() * (1000000000 - 99999999)) + 99999999));
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
		EntityManager manager = emf.createEntityManager();
		while (true) {
			@SuppressWarnings("unchecked")
			List<Object[]> objects = manager.createQuery("SELECT UID FROM Autorisation WHERE UID= :uid")
					.setParameter("uid", UID).getResultList();
			List<Autorisation> auth = new ArrayList<>(objects.size());
			for (Object[] obj : objects) {
				auth.add(new Autorisation((String) obj[0]));
			}
			if (auth.size() != 0) {
				UID = Long.toString((long) ((Math.random() * (1000000000 - 99999999)) + 99999999));
			} else {
				break;
			}
		}
		try {
			manager.getTransaction().begin();
			Autorisation autorisation = new Autorisation(UID, admin, username, password, name, surname, telephone,
					address, email);
			manager.persist(autorisation);
			manager.getTransaction().commit();
			manager.close();
			emf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "OK";
	}

	@Override
	public String getBooksList() throws JsonProcessingException {
		String resultArray = "[]";
		ObjectMapper mapper = new ObjectMapper();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
		EntityManager manager = emf.createEntityManager();
		@SuppressWarnings("unchecked")
		List<Object[]> objects = manager
				.createQuery("SELECT BID, UID, BookTitle, AuthorLastName, AuthorFirstName, BookGenre, "
						+ "IssuedDate, Period, FINE, Warning FROM Bookshelf")
				.getResultList();
		List<Bookshelf> books = new ArrayList<>(objects.size());
		for (Object[] obj : objects) {
			books.add(new Bookshelf((String) obj[0], (String) obj[1], (String) obj[2], (String) obj[3], (String) obj[4],
					(String) obj[5], (String) obj[6], (String) obj[7], (String) obj[8], (String) obj[9]));
		}
		for (Bookshelf b : books) {
			if (!b.getIssuedDate().equals("in library")) {
				b.setIssuedDate("borrowed");
			}
		}
		resultArray = mapper.writeValueAsString(books);
		manager.close();
		emf.close();
		return resultArray;
	}

	@Override
	public String getLoanedBooks(String user) throws SQLException {
		user = user.replaceAll(" ", "");
		String sqlSelect = "select tab.UID, tab.FirstName, tab.LastName, tab.BookTitle, tab.AuthorLastName, tab.BID, tab.IssuedDate, tab.Period, tab.FINE, tab.Warning, tab.LoanExtendRequest from (\n"
				+ "SELECT Autorisation.UID, Bookshelf.BookTitle, Bookshelf.AuthorLastName, Autorisation.FirstName, Autorisation.LastName, Bookshelf.BID, Bookshelf.IssuedDate, Bookshelf.Period, Bookshelf.FINE, Bookshelf.Warning, Bookshelf.LoanExtendRequest\n"
				+ "FROM Autorisation\n" + "INNER JOIN Bookshelf ON Autorisation.UID = Bookshelf.UID\n"
				+ ") tab where UID = ?";
		LocalDate issuedDate;
		LocalDate today = LocalDate.now();
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		today.format(formater);
		Connection conne = connect();
		PreparedStatement ps = conne.prepareStatement(sqlSelect);
		ps.setString(1, user);
		ResultSet rs = ps.executeQuery();
		JSONArray array = new JSONArray();
		while (rs.next()) {
			JSONObject output = new JSONObject();
			long period = 0;
			float fine = 0;
			output.put("uid", rs.getString(1));
			output.put("name", rs.getString(2));
			output.put("surname", rs.getString(3));
			output.put("book", rs.getString(4));
			output.put("author", rs.getString(5));
			output.put("bid", rs.getString(6));
			issuedDate = LocalDate.parse(rs.getString(7));
			period = ChronoUnit.DAYS.between(issuedDate, today);
			output.put("issuedDate", rs.getString(7));
			output.put("period", period);
			output.put("warning", rs.getString(10));
			output.put("extend", rs.getString(11));
			if (period > 30) {
				fine += 0.50 * (period - 30);
			}
			output.put("fine", fine);
			array.put(output);
			Statement st = conne.createStatement();
			st.executeUpdate("UPDATE currencyconverter.Bookshelf SET FINE = '" + String.valueOf(fine) + "', Period = '"
					+ period + "' WHERE BID = '" + rs.getString(6) + "' AND UID = '" + rs.getString(1) + "'");
			st.close();
		}
		conne.close();
		return array.toString();
	}

	@Override
	public String verifyUser(String user) throws SQLException {
		user = user.replaceAll(" ", "");
		String select = "SELECT UID, FirstName, LastName, Telephone, Address, Email FROM AUTORISATION WHERE UID = ?";
		Connection conn = connect();
		PreparedStatement ps = conn.prepareStatement(select);
		ps.setString(1, user);
		ResultSet rs = ps.executeQuery();
		JSONArray array = new JSONArray();
		while (rs.next()) {
			JSONObject obj = new JSONObject();
			obj.put("name", rs.getString(2));
			obj.put("surname", rs.getString(3));
			obj.put("Telephone", rs.getString(4));
			obj.put("Address", rs.getString(5));
			obj.put("email", rs.getString(6));
			array.put(obj);
		}
		conn.close();
		return array.toString();
	}

	@Override
	public String loanBook(String user, String book) throws SQLException {
		JSONObject obj = new JSONObject();
		LocalDate today = LocalDate.now();
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		today.format(formater);
		Connection conn = connect();
		String sqlVerify = "select (SELECT IssuedDate FROM Bookshelf WHERE BID = ? ) as IssuedDate, (\n"
				+ "select count(*) as BooksBorrowed from Bookshelf where UID = ? ) as BooksBorrowed";
		String sql = "UPDATE currencyconverter.Bookshelf SET UID = ? , IssuedDate = ?, Warning = '' WHERE BID = ?";
		PreparedStatement psVerify = conn.prepareStatement(sqlVerify);
		psVerify.setString(1, book);
		psVerify.setString(2, user);
		ResultSet rs = psVerify.executeQuery();
		while (rs.next()) {
			if (rs.getInt(2) == 3) {
				obj.put("return", "maximumThreeBorrowedBooks");
				break;
			}
			if (rs.getString(1).equals("in library")) {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, user);
				ps.setString(2, today.toString());
				ps.setString(3, book);
				ps.executeUpdate();
				ps.close();
				psVerify.close();
				conn.close();
				obj.put("return", "updated");
				break;
			} else {
				obj.put("return", "exist");
				break;
			}
		}
		return obj.toString();

	}

	@Override
	public String returnBook(String book) throws SQLException {
		book = book.replaceAll(" ", "");
		JSONObject obj = new JSONObject();
		Connection conn = connect();
		String sql = "UPDATE currencyconverter.Bookshelf SET UID = '' , IssuedDate = 'in library', Period = 0 , FINE = 0, Warning = '' WHERE BID = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, book);
		int result = ps.executeUpdate();
		ps.close();
		conn.close();
		obj.put("result", result);
		return obj.toString();
	}

	@Override
	public String deleteUser(String user) throws SQLException {
		JSONObject obj = new JSONObject();
		Connection conn = connect();
		String sql = "delete from autorisation where UID = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, user);
		int result = ps.executeUpdate();
		ps.close();
		conn.close();
		obj.put("result", result);
		return obj.toString();
	}

	@Override
	public String extendLoan(String user, String book, String admin) throws SQLException {
		EmailService es = new EmailService();
		String EMAIL_ADDRESS_RECIPIENT = "madagasakaria@gmail.com";
		String SUBJECT = "User extend loan request";
		String EMAIL_TEXT = "New user extend loan request: \n\n\n User ID: " + user + " \n Book ID: " + book;
		JSONObject obj = new JSONObject();
		Connection conn = connect();
		LocalDate today = LocalDate.now();
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		today.format(formater);
		String sqlUpdate = "UPDATE currencyConverter.Bookshelf SET IssuedDate = ? , LoanExtendRequest = ? WHERE BID = ? AND UID = ?";
		String sql = "UPDATE currencyConverter.Bookshelf SET LoanExtendRequest = ? WHERE BID = ? AND UID = ?";
		if (admin.equals("true")) {
			PreparedStatement ps = conn.prepareStatement(sqlUpdate);
			ps.setString(1, today.toString());
			ps.setString(2, "EXTENDED");
			ps.setString(3, book);
			ps.setString(4, user);
			int rs = ps.executeUpdate();
			obj.put("response", String.valueOf(rs));
			ps.close();
			conn.close();
		} else if (admin.contentEquals("false")) {
			PreparedStatement psInsert = conn.prepareStatement(sql);
			psInsert.setString(1, "SENT");
			psInsert.setString(2, book);
			psInsert.setString(3, user);
			psInsert.executeUpdate();
			es.emailClient(EMAIL_ADDRESS_RECIPIENT, SUBJECT, EMAIL_TEXT);
			obj.put("response", "sent");
			psInsert.close();
			conn.close();
		} else {
			obj.put("response", "error");
			conn.close();
		}
		return obj.toString();
	}

	@Override
	public String registerNewUser(String name, String surname, String email, String telephone, String address,
			String username, String password) throws SQLException {
		String admin = "false";
		JSONObject object = new JSONObject();
		String sql = "SELECT COUNT(*) FROM Autorisation where Username = ?";
		Connection conn = connect();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			if(rs.getInt(1) == 0) {
				String UID = Long.toString((long) ((Math.random() * (1000000000 - 99999999)) + 99999999));
				EntityManagerFactory emf = Persistence.createEntityManagerFactory("converterPersistence");
				EntityManager manager = emf.createEntityManager();
				while (true) {
					@SuppressWarnings("unchecked")
					List<Object[]> objects = manager.createQuery("SELECT UID FROM Autorisation WHERE UID= :uid")
							.setParameter("uid", UID).getResultList();
					List<Autorisation> auth = new ArrayList<>(objects.size());
					for (Object[] obj : objects) {
						auth.add(new Autorisation((String) obj[0]));
					}
					if (auth.size() != 0) {
						UID = Long.toString((long) ((Math.random() * (1000000000 - 99999999)) + 99999999));
					} else {
						break;
					}
				}
				try {
					manager.getTransaction().begin();
					Autorisation autorisation = new Autorisation(UID, admin, username, password, name, surname, telephone,
							address, email);
					manager.persist(autorisation);
					manager.getTransaction().commit();
					manager.close();
					emf.close();
					object.put("response",UID.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				object.put("response", "exist");

			}
			
		}
		conn.close();
		ps.close();
		rs.close();
		return object.toString();
	}
}