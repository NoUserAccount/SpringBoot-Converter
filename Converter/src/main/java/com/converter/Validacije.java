package com.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacije {
	//19.07.2019.
	public boolean validacijaDatuma(String datum) {
		String regex = "^[0-3]?[0-9]/[0-3]?[0-9]/(?:[0-9]{2})?[0-9]{2}$";
		 
		Pattern pattern = Pattern.compile(regex);		
		Matcher matcher = pattern.matcher(datum);
		
		return matcher.matches();
	}
}
