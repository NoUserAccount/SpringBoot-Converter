package com.converter;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@CrossOrigin(origins = "*")
@RestController
public class Controller {

	@Autowired
	ConverterService cService;
	@Autowired
	Statistika stat;
	@Autowired
	ConverterDAO cDao;

	@RequestMapping(value = "/converter/{date}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String jsonByDate(@PathVariable(value = "date") 
		String date) throws SQLException, JsonParseException, JsonMappingException, IOException {
		return cService.getCurrency(date);
	}

	@RequestMapping(value = "/converterStatistics/{mostCommonInterval}/{currencyInterval}/{currency}", method = RequestMethod.GET, produces = "application/json")
	public String getConverterStatistics(
			@PathVariable(value="mostCommonInterval") int mostCommonInterval,
			@PathVariable(value="currencyInterval") int currencyInterval,
			@PathVariable(value="currency") String currency) throws SQLException {
		return cService.getConverterStats(mostCommonInterval, currencyInterval, currency).toString();
	}

	@RequestMapping(value = "/updateCounter/{startValue}", method = RequestMethod.GET)
	public @ResponseBody String statisticUpdate(@PathVariable(value = "startValue") String value) throws ParseException, SQLException {
		stat.updateCounter(value);
		return value + " updated!";
	}
	
	@RequestMapping(value = "/contact/{name}/{surname}/{contact}/{message}", method = RequestMethod.GET)
	public @ResponseBody String contactInfo(
			@PathVariable(value = "name") String name, 
			@PathVariable(value = "surname") String surname,
			@PathVariable(value = "contact") String contact, 
			@PathVariable(value = "message") String message) throws SQLException {
		return cService.contactInfo(name, surname, contact, message);
	}
	
	@RequestMapping(value = "/weather/{grad}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getWeatherStatus(
			@PathVariable(value = "grad") String grad) throws SQLException {
		return cService.getWeatherStatus(grad);
	}
	
	@RequestMapping(value = "/earthquake", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getEarthquakeData () throws SQLException {
		return cDao.getEarthquake();
	}
	
	@RequestMapping(value = "/authenticateUser/{username}/{password}", method = RequestMethod.GET)
	public @ResponseBody String authenticateUser(
			@PathVariable(value = "username") String username, 
			@PathVariable(value = "password") String password) throws SQLException {
		return cService.authenticateBookshelfUser(username, password).toString();
	}
	
	@RequestMapping(value = "/addNewBook/{title}/{writerLast}/{writerFirst}/{genre}", method = RequestMethod.GET)
	public @ResponseBody String addNewBook(
			@PathVariable(value = "title") String title,
			@PathVariable(value = "writerLast") String writerLast,
			@PathVariable(value = "writerFirst") String writerFirst,
			@PathVariable(value = "genre") String genre) throws ParseException, SQLException, JsonProcessingException {
		return cService.addBookToBookshelf(title,writerLast,writerFirst,genre).toString();
	}
	
	@RequestMapping(value = "/addNewUser/{admin}/{username}/{password}/{name}/{surname}/{telephone}/{address}/{email}", method = RequestMethod.GET)
	public @ResponseBody String addNewUser(
			@PathVariable(value = "admin") String admin,
			@PathVariable(value = "username") String username,
			@PathVariable(value = "password") String password,
			@PathVariable(value = "name") String name,
			@PathVariable(value = "surname") String surname,
			@PathVariable(value = "telephone") String telephone,
			@PathVariable(value = "address") String address,
			@PathVariable(value = "email") String email) throws ParseException, SQLException, JsonProcessingException {
		return cService.addNewUser(admin,username,password,name,surname,telephone,address,email);
	}
	
	@RequestMapping(value = "/delete/{user}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String deleteUser(
			@PathVariable(value="user") String user) throws SQLException, JsonProcessingException {
		return cService.deleteUser(user);
	}
	
	@RequestMapping(value = "/books", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getBooks() throws SQLException, JsonProcessingException {
		return cDao.getBooksList();
	}
	
	@RequestMapping(value = "/userLoan/{user}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String getLoanedBooks(@PathVariable(value="user") String user) throws SQLException, JsonProcessingException {
		return cService.getLoanedBooks(user);
	}
	
	@RequestMapping(value = "/loan/{user}/{book}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String postLoanBook(
			@PathVariable(value="user") String user,
			@PathVariable(value="book") String book) throws SQLException, JsonProcessingException {
		return cService.loanBook(user, book);
	}
	
	@RequestMapping(value = "/extendLoan/{user}/{book}/{admin}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String extendLoan(
			@PathVariable(value="user") String user,
			@PathVariable(value="book") String book,
			@PathVariable(value="admin") String admin) throws SQLException, JsonProcessingException {
		return cService.extendLoan(user, book, admin);
	}
	
	@RequestMapping(value = "/return/{book}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String postReturnBook(
			@PathVariable(value="book") String book) throws SQLException, JsonProcessingException {
		return cService.returnBook(book);
	}
	
	@RequestMapping(value = "/user/{user}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String verifyUser(@PathVariable(value="user") String user) throws SQLException {
		return cService.verifyUser(user);
	}
		
	@RequestMapping(value = "/register/{firstName}/{lastName}/{email}/{telephone}/{address}/{username}/{password}", method = RequestMethod.GET)
	public @ResponseBody String registerUser(
			@PathVariable(value = "firstName") String name,
			@PathVariable(value = "lastName") String surname,
			@PathVariable(value = "email") String email,
			@PathVariable(value = "telephone") String telephone,
			@PathVariable(value = "address") String address,
			@PathVariable(value = "username") String username,
			@PathVariable(value = "password") String password) throws ParseException, SQLException, JsonProcessingException {
		return cService.registerNewUser(name,surname,email,telephone,address,username,password);
	}
	
	@RequestMapping(value = "/tvz", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody String test() throws SQLException, IOException {
		return cDao.getTvzRss();	
	}
}