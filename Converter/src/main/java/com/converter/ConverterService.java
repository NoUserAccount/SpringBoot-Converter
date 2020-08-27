package com.converter;

import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class ConverterService {

	@Autowired
	public ConverterDAO cDao;
	
	@Autowired
	public Statistika stats;
	
	public String getCurrency(String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cDao.getCurrency(date);
	}

	
	public String contactInfo(String name, String surname, String contact, String message) throws SQLException {
		return cDao.contactInfo(name, surname, contact, message);
	}
	
	public JSONArray login(String user, String psw) throws SQLException, JSONException {
		return cDao.login(user, psw);
	}

	public String getWeatherStatus(String grad) {
		return cDao.getWeatherStatus(grad);
	}

	public JSONArray getConverterStats(int mostCommonInterval, int currencyInterval, String currency) {
		return stats.getConverterStatistics(mostCommonInterval, currencyInterval, currency);
	}

	public String authenticateBookshelfUser(String username, String password) {
		return cDao.autheticateUser(username, password);
	}

	public String addBookToBookshelf(String title, String writerLast, String writerFirst, String genre) throws JsonProcessingException {
		return cDao.addNewBook(title, writerLast, writerFirst, genre);
	}

	public String addNewUser(String admin, String username, String password, String name, String surname,
			String telephone, String address, String email) throws JsonProcessingException {
		return cDao.addNewUser(admin, username, password, name, surname, telephone, address, email);
	}


	public String getLoanedBooks(String user) throws SQLException {
		return cDao.getLoanedBooks(user);
	}

	public String verifyUser(String user) throws SQLException {
		return cDao.verifyUser(user);
	}

	public String loanBook(String user, String book) throws SQLException {
		user = user.replaceAll(" ", "");
		book = book.replaceAll(" ", "");
		return cDao.loanBook(user, book);
	}

	public String returnBook(String book) throws SQLException {
		return cDao.returnBook(book);
	}

	public String deleteUser(String user) throws SQLException {
		user = user.replaceAll(" ", "");
		return cDao.deleteUser(user);
	}

	public String extendLoan(String user, String book, String admin) throws SQLException {
		user = user.replaceAll(" ", "");
		book = book.replaceAll(" ", "");
		admin = admin.replaceAll(" ", "");
		return cDao.extendLoan(user, book, admin);
	}

	public String registerNewUser(String name, String surname, String email, String telephone, String address,
			String username, String password) throws SQLException {
		username.replaceAll(" ","");
		password.replaceAll(" ","");
		email.replaceAll(" ","");
		return cDao.registerNewUser(name, surname, email, telephone, address, username, password);
	}	
}