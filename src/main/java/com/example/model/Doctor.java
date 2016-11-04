package com.example.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "doctor")
public class Doctor implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String username;
	private String phone;
	private String title;
	private String office;
	private String skill;
	private String experience;
	private String email;
	private String picUrl;
	private Hospital hospital;
	private Set<ScheduleTime> scheduleTimes = new HashSet<ScheduleTime>();
	private Set<Evaluate> evaluates = new HashSet<Evaluate>();// 评价信息

	public Doctor() {

	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", length = 500, nullable = false, unique = true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "username", length = 100, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Basic
	@Column(name = "phone", length = 20)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Basic
	@Column(name = "title", length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Basic
	@Column(name = "office", length = 100)
	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	@Basic
	@Column(name = "skill", length = 100)
	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	@Basic
	@Column(name = "experience", length = 500)
	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	@Basic
	@Column(name = "email", length = 500)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Basic
	@Column(name = "pic_url", length = 500)
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "doctor_id")
	@JsonIgnore
	public Set<ScheduleTime> getScheduleTimes() {
		return scheduleTimes;
	}

	public void setScheduleTimes(Set<ScheduleTime> scheduleTimes) {
		this.scheduleTimes = scheduleTimes;
	}

	@OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "doctor_id")
	@JsonIgnore
	public Set<Evaluate> getEvaluates() {
		return evaluates;
	}

	public void setEvaluates(Set<Evaluate> evaluates) {
		this.evaluates = evaluates;
	}

}
