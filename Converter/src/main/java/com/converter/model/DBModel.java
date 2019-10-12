package com.converter.model;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DBModel {

	private List<String> valuta;
	private List<Float> vrijednost;
	private List<Integer> jedinica;
	private List<String> drzava;
	
	
	
	public List<String> getDrzava() {
		return drzava;
	}
	public void setDrzava(List<String> drzava) {
		this.drzava = drzava;
	}
	public List<String> getValuta() {
		return valuta;
	}
	public void setValuta(List<String> valuta) {
		this.valuta = valuta;
	}
	public List<Float> getVrijednost() {
		return vrijednost;
	}
	public void setVrijednost(List<Float> vrijednost) {
		this.vrijednost = vrijednost;
	}
	public List<Integer> getJedinica() {
		return jedinica;
	}
	public void setJedinica(List<Integer> jedinica) {
		this.jedinica = jedinica;
	}
	


	
}
