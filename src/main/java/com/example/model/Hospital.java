package com.example.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "hospital")
public class Hospital {
	private long id;
	private String hospitalName;
	private String hospitalAddress;
	//private Set<Doctor> doctors = new HashSet<Doctor>();

	public Hospital() {

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
	@Column(name="hospital_name",length=100)
	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	
	@Basic
	@Column(name="hospital_address",length=200)
	public String getHospitalAddress() {
		return hospitalAddress;
	}

	public void setHospitalAddress(String hospitalAddress) {
		this.hospitalAddress = hospitalAddress;
	}
	
//	@OneToMany(mappedBy="hospital")
//	public Set<Doctor> getDoctors() {
//		return doctors;
//	}
//
//	public void setDoctors(Set<Doctor> doctors) {
//		this.doctors = doctors;
//	}

}
