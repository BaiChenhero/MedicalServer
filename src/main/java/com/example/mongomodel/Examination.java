package com.example.mongomodel;

import java.io.Serializable;

import org.springframework.data.annotation.Id;  


public class Examination implements Serializable {  
  
    private static final long serialVersionUID = 1L;  
    
    @Id  
    String id;
    String name;
    String process;
    String introduction;
    String cost;
    String normal_value;
    String clinical_significance;
    String precautions;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProcess() {
		return process;
	}
	public void setProcess(String process) {
		this.process = process;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getNormal_value() {
		return normal_value;
	}
	public void setNormal_value(String normal_value) {
		this.normal_value = normal_value;
	}
	public String getClinical_significance() {
		return clinical_significance;
	}
	public void setClinical_significance(String clinical_significance) {
		this.clinical_significance = clinical_significance;
	}
	public String getPrecautions() {
		return precautions;
	}
	public void setPrecautions(String precautions) {
		this.precautions = precautions;
	}
}
