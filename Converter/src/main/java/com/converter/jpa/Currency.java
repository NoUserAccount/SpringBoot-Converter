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
	private String SelectOption;
	
	public Currency() {};
	
	public Currency(String Valuta, int Jedinica, float Srednji, String Drzava, String SelectOption) {
		super();
		this.Valuta = Valuta;
		this.Vrijednost = Srednji;
		this.Jedinica = Jedinica;
		this.Drzava = Drzava;
		this.SelectOption = SelectOption;
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
	public String getSelectOption() {
		return SelectOption;
	}

	public void setSelectOption(String SelectOption) {
		this.SelectOption = SelectOption;
	}

	public void setDrzava(String Drzava) {
		this.Drzava = Drzava;
	}

	@Override
	public String toString() {
		return "Currency [Valuta=" + Valuta + ", Vrijednost=" + Vrijednost + ", Jedinica=" + Jedinica + ", Drzava="
				+ Drzava + ", SelectOption=" + SelectOption + "]";
	}

}
