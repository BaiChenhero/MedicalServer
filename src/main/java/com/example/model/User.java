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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user")
public class User implements Serializable {
	private long id;
	private String username;
	private String password;
	private String phone;
	private Set<Evaluate> evaluates = new HashSet<Evaluate>();// 评价信息
	private Set<HeartRate> heartRates = new HashSet<HeartRate>();// 心率信息
	private Set<Temperature> temperatures = new HashSet<Temperature>();// 体温信息
	private Set<Record> records = new HashSet<Record>();// 用户摔倒记录信息

	public User() {

	}

	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", length = 500, unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "username", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Basic
	@Column(name = "password", length = 50, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(name = "phone", length = 50)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	public Set<Evaluate> getEvaluates() {
		return evaluates;
	}

	public void setEvaluates(Set<Evaluate> evaluates) {
		this.evaluates = evaluates;
	}

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	public Set<HeartRate> getHeartRates() {
		return heartRates;
	}

	public void setHeartRates(Set<HeartRate> heartRates) {
		this.heartRates = heartRates;
	}

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	public Set<Temperature> getTemperatures() {
		return temperatures;
	}

	public void setTemperatures(Set<Temperature> temperatures) {
		this.temperatures = temperatures;
	}

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	public Set<Record> getRecords() {
		return records;
	}

	public void setRecords(Set<Record> records) {
		this.records = records;
	}
	

}
