package com.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FormModel {

	private String valutaP;
	private String valutaO;
	private String datum;
	private String iznos;
	private Map<String, ArrayList<String>> formKV = new HashMap<>();
	
	


	public Map<String, ArrayList<String>> getFormKV() {
		return formKV;
	}

	public void setFormKV(Map<String, ArrayList<String>> formKV) {
		this.formKV = formKV;
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
