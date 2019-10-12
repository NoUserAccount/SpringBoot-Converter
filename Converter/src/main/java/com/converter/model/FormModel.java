package com.converter.model;

import org.springframework.stereotype.Component;

@Component
public class FormModel {

	private String valutaP;
	private String valutaO;
	private String datum;
	private String iznos;
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValutaP() {
		return valutaP;
	}

	public void setValutaP(String valutaP) {
		this.valutaP = valutaP;
	}

	public String getValutaO() {
		return valutaO;
	}

	public void setValutaO(String valutaO) {
		this.valutaO = valutaO;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public String getIznos() {
		return iznos;
	}

	public void setIznos(String iznos) {
		this.iznos = iznos;
	}
	

}
