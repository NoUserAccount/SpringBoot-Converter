package com.converter.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Currency {
	
	@Id
	private String Valuta;
	private float Vrijednost;
	private int Jedinica;
	private String Drzava;
	
	public Currency() {};
	
	public Currency(String Valuta, int Jedinica, float Srednji, String Drzava) {
		super();
		this.Valuta = Valuta;
		this.Vrijednost = Srednji;
		this.Jedinica = Jedinica;
		this.Drzava = Drzava;
	}
	
	public String getValuta() {
		return Valuta;
	}
	public void setValuta(String Valuta) {
		this.Valuta = Valuta;
	}
	public float getVrijednost() {
		return Vrijednost;
	}
	public void setVrijednost(float Vrijednost) {
		this.Vrijednost = Vrijednost;
	}
	public int getJedinica() {
		return Jedinica;
	}
	public void setJedinica(int Jedinica) {
		this.Jedinica = Jedinica;
	}
	public String getDrzava() {
		return Drzava;
	}
	public void setDrzava(String Drzava) {
		this.Drzava = Drzava;
	}
	@Override
	public String toString() {
		return "Currency [Valuta=" + Valuta + ", Vrijednost=" + Vrijednost + ", Jedinica=" + Jedinica
				 + ", Drzava=" + Drzava + "]";
	}
}
