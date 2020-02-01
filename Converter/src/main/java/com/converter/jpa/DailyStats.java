package com.converter.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DailyStats {
	
	@Id
	private String Valuta;
	private int Counter;

	public DailyStats() {};
	
	public DailyStats(String valuta, int counter) {
		super();
		Valuta = valuta;
		Counter = counter;
	}
	public String getValuta() {
		return Valuta;
	}
	public void setValuta(String valuta) {
		Valuta = valuta;
	}
	public int getCounter() {
		return Counter;
	}
	public void setCounter(int counter) {
		Counter = counter;
	}
	@Override
	public String toString() {
		return "DailyStats [Valuta=" + Valuta + ", Counter=" + Counter + "]";
	}	
}