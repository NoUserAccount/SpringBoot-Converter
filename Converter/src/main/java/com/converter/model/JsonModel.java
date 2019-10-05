package com.converter.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonModel {

	@JsonProperty("Srednji za devize")
	private String srednjiZaDevize;

	@JsonProperty("Država")
	private String drzava;

	@JsonProperty("Valuta")
	private String valuta;
	
	@JsonProperty("Jedinica")
	private String jedinica;


}
