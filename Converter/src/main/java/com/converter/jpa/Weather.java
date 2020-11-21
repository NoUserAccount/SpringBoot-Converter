package com.converter.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Weather {
	
	@Id
	private String querye;
	private String region;
	private String observationTime;
	private String temperature;
	private String descriptions;
	private String weatherIcon;
	private String windSpeed;
	private String windDirection;
	private String pressure;
	private String precipitation;
	private String humidity;
	private String cloudcover;
	private String feelsLike;
	private String uvIndex;
	private String visibility;
	private String isDay;
	private String dates;
	private String downloadHour;

	public Weather() {};
	
	public Weather(String querye, String region, String observationTime, String temperature, String descriptions,
			String weatherIcon, String windSpeed, String windDirection, String pressure, String precipitation,
			String humidity, String cloudcover, String feelsLike, String uvIndex, String visibility, String isDay,
			String dates) {
		super();
		this.querye = querye;
		this.region = region;
		this.observationTime = observationTime;
		this.temperature = temperature;
		this.descriptions = descriptions;
		this.weatherIcon = weatherIcon;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.pressure = pressure;
		this.precipitation = precipitation;
		this.humidity = humidity;
		this.cloudcover = cloudcover;
		this.feelsLike = feelsLike;
		this.uvIndex = uvIndex;
		this.visibility = visibility;
		this.isDay = isDay;
		this.dates = dates;
	}

	public Weather(String querye, String region, String observationTime, String temperature, String descriptions,
			String weatherIcon, String windSpeed, String windDirection, String pressure, String precipitation,
			String humidity, String cloudcover, String feelsLike, String uvIndex, String visibility, String isDay,
			String dates, String downloadHour) {
		super();
		this.querye = querye;
		this.region = region;
		this.observationTime = observationTime;
		this.temperature = temperature;
		this.descriptions = descriptions;
		this.weatherIcon = weatherIcon;
		this.windSpeed = windSpeed;
		this.windDirection = windDirection;
		this.pressure = pressure;
		this.precipitation = precipitation;
		this.humidity = humidity;
		this.cloudcover = cloudcover;
		this.feelsLike = feelsLike;
		this.uvIndex = uvIndex;
		this.visibility = visibility;
		this.isDay = isDay;
		this.dates = dates;
		this.downloadHour = downloadHour;
	}

	public String getQuery() {
		return querye;
	}


	public void setQuery(String querye) {
		this.querye = querye;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getObservationTime() {
		return observationTime;
	}


	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}


	public String getTemperature() {
		return temperature;
	}


	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}


	public String getDescription() {
		return descriptions;
	}


	public void setDescription(String descriptions) {
		this.descriptions = descriptions;
	}


	public String getWeatherIcon() {
		return weatherIcon;
	}


	public void setWeatherIcon(String weatherIcon) {
		this.weatherIcon = weatherIcon;
	}


	public String getWindSpeed() {
		return windSpeed;
	}


	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}


	public String getWindDirection() {
		return windDirection;
	}


	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}


	public String getPressure() {
		return pressure;
	}


	public void setPressure(String pressure) {
		this.pressure = pressure;
	}


	public String getPrecipitation() {
		return precipitation;
	}


	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}


	public String getHumidity() {
		return humidity;
	}


	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}


	public String getCloudcover() {
		return cloudcover;
	}


	public void setCloudcover(String cloudcover) {
		this.cloudcover = cloudcover;
	}


	public String getFeelsLike() {
		return feelsLike;
	}


	public void setFeelsLike(String feelsLike) {
		this.feelsLike = feelsLike;
	}


	public String getUvIndex() {
		return uvIndex;
	}


	public void setUvIndex(String uvIndex) {
		this.uvIndex = uvIndex;
	}


	public String getVisibility() {
		return visibility;
	}


	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}


	public String getIsDay() {
		return isDay;
	}


	public void setIsDay(String isDay) {
		this.isDay = isDay;
	}


	public String getDate() {
		return dates;
	}


	public void setDate(String dates) {
		this.dates = dates;
	}
	
	public String getDownloadHour() {
		return downloadHour;
	}

	public void setDownloadHour(String downloadHour) {
		this.downloadHour = downloadHour;
	}

	@Override
	public String toString() {
		return "Weather [querye=" + querye + ", region=" + region + ", observationTime=" + observationTime
				+ ", temperature=" + temperature + ", descriptions=" + descriptions + ", weatherIcon=" + weatherIcon
				+ ", windSpeed=" + windSpeed + ", windDirection=" + windDirection + ", pressure=" + pressure
				+ ", precipitation=" + precipitation + ", humidity=" + humidity + ", cloudcover=" + cloudcover
				+ ", feelsLike=" + feelsLike + ", uvIndex=" + uvIndex + ", visibility=" + visibility + ", isDay="
				+ isDay + ", dates=" + dates + ", downloadHour=" + downloadHour + "]";
	}



	
	
	
}
