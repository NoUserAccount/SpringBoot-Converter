package com.converter.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PopulateDropdownModel {

	private List<String> populateDD;

	public List<String> getPopulateDD() {
		return populateDD;
	}

	public void setPopulateDD(List<String> populateDD) {
		this.populateDD = populateDD;
	}
	
	
}
