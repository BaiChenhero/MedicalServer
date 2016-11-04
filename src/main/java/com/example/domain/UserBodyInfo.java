package com.example.domain;

import java.util.Date;

public class UserBodyInfo {
	private long user_id;
	private double temperature;
	private Date temperatureDate;// 体温测量的时间
	private int heartRate;
	private Date heartRateDate;// 心率测量的时间

	public UserBodyInfo() {

	}

	public UserBodyInfo(long user_id, float temperature, Date temperatureDate,
			int heartRate, Date heartRateDate) {
		this.user_id = user_id;
		this.temperature = temperature;
		this.temperatureDate = temperatureDate;
		this.heartRate = heartRate;
		this.heartRateDate = heartRateDate;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public Date getTemperatureDate() {
		return temperatureDate;
	}

	public void setTemperatureDate(Date temperatureDate) {
		this.temperatureDate = temperatureDate;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public Date getHeartRateDate() {
		return heartRateDate;
	}

	public void setHeartRateDate(Date heartRateDate) {
		this.heartRateDate = heartRateDate;
	}

}
