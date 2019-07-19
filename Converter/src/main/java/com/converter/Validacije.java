package com.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacije {
	//2019-07-19
	public boolean validacijaDatuma(String datum) {
								//yyyy              
		String regex = "^(?:[0-9]{2})?[0-9]{2}-[0-3]?[0-9]-[0-3]?[0-9]$";
		 
		Pattern pattern = Pattern.compile(regex);		
		Matcher matcher = pattern.matcher(datum);
		
		return matcher.matches();
	}
}
