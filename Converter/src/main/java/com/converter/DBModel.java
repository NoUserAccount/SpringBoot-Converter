package com.converter;

public class DBModel {
	private String valuta;
	private int jedinica;
	
	private String valutaOdredisna;
	private int jedinicaOdredisna;
	
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
	private float iznos;
	
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
	public float getIznos() {
		return iznos;
	}
	public void setIznos(float iznos) {
		this.iznos = iznos;
	}
	
}
