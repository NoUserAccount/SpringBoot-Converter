package com.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validacije {
	//2019-10-08
	public boolean validacijaDatuma(String datum) {
		String regex = "^(?:[0-9]{2})?[0-9]{2}-[0-3]?[0-9]-[0-3]?[0-9]$";
		Pattern pattern = Pattern.compile(regex);		
		Matcher matcher = pattern.matcher(datum);
		if(matcher.matches()) {
			if(datum.substring(0,4).equals("0000")) {
				return false;
			}
			else if(datum.substring(0,3).equals("000")) {
				return false;
			}
			else if(datum.substring(0,2).equals("00")) {
				return false;
			}
			else if(datum.substring(0,1).equals("0")) {
				return false;
			}
			else if(datum.substring(5,7).equals("00")) {
				return false;
			}
			else if(datum.substring(8,10).equals("00")) {
				return false;
			}
			else return true;
		}
		return false;
	}
}
