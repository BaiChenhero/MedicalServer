package com.example.domain;

public class UploadMaterial {
	private String userId;
	private String doctorId;
	private String username;
	private String sickTime;
	private String sickDescription;
	private String classType;
	private String picRemotePath1;
	private String picRemotePath2;
	private String picRemotePath3;

	public UploadMaterial() {

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSickTime() {
		return sickTime;
	}

	public void setSickTime(String sickTime) {
		this.sickTime = sickTime;
	}

	public String getSickDescription() {
		return sickDescription;
	}

	public void setSickDescription(String sickDescription) {
		this.sickDescription = sickDescription;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public String getPicRemotePath1() {
		return picRemotePath1;
	}

	public void setPicRemotePath1(String picRemotePath1) {
		this.picRemotePath1 = picRemotePath1;
	}

	public String getPicRemotePath2() {
		return picRemotePath2;
	}

	public void setPicRemotePath2(String picRemotePath2) {
		this.picRemotePath2 = picRemotePath2;
	}

	public String getPicRemotePath3() {
		return picRemotePath3;
	}

	public void setPicRemotePath3(String picRemotePath3) {
		this.picRemotePath3 = picRemotePath3;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

}
