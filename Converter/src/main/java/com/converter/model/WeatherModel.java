package com.converter.model;

import org.springframework.stereotype.Component;

@Component
public class WeatherModel {
	private String city;
	private float temp;
	private float preasure;
	private float windSpeed;
	private String windDirection;
	private String weather;
	private int moist;
	private float preasureTrend;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	public float getPreasure() {
		return preasure;
	}
	public void setPreasure(float preasure) {
		this.preasure = preasure;
	}
	public float getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public int getMoist() {
		return moist;
	}
	public void setMoist(int moist) {
		this.moist = moist;
	}
	public float getPreasureTrend() {
		return preasureTrend;
	}
	public void setPreasureTrend(float preasureTrend) {
		this.preasureTrend = preasureTrend;
	}	
}