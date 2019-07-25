package com.converter.model;

import org.springframework.stereotype.Component;

@Component
public class DBModel {

	private String valuta;
	private float vrijednost;
	private int jedinica;
	
	private String valutaOdredisna;
	private float vrijednostOdredisna;
	
	public float getVrijednost() {
		return vrijednost;
	}
	public void setVrijednost(float vrijednost) {
		this.vrijednost = vrijednost;
	}
	public float getVrijednostOdredisna() {
		return vrijednostOdredisna;
	}
	public void setVrijednostOdredisna(float vrijednostOdredisna) {
		this.vrijednostOdredisna = vrijednostOdredisna;
	}
	private int jedinicaOdredisna;
	
	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
	}
	public int getJedinica() {
		return jedinica;
	}
	public void setJedinica(int jedinica) {
		this.jedinica = jedinica;
	}
	public String getValutaOdredisna() {
		return valutaOdredisna;
	}
	public void setValutaOdredisna(String valutaOdredisna) {
		this.valutaOdredisna = valutaOdredisna;
	}
	public int getJedinicaOdredisna() {
		return jedinicaOdredisna;
	}
	public void setJedinicaOdredisna(int jedinicaOdredisna) {
		this.jedinicaOdredisna = jedinicaOdredisna;
	}

	
}
