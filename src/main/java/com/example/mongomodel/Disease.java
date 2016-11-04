package com.example.mongomodel;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;  


public class Disease implements Serializable {  
  
    private static final long serialVersionUID = 1L;  
    
    @Id  
    String id;
    String url;
    String name;
    String examination;
    String summary;
    List<String> aliases;
    String symptom;
    String diet;
    String treat;
    String prevention;
    String complication;
    String identification;
    String cause;
    
    /*
    @Override
    public String toString() {
    	int maxLength = 80;
		return 
			"标识: " + (this.id + '\n') + 
			"链接: " + (this.url + '\n') +
			"名称: " + (this.name + '\n') +
			"检查：" + (this.examination != null  ? this.examination.substring(0, maxLength) : ' ') + '\n' +
			"概述: " + (this.summary != null  ? this.summary.substring(0, maxLength) : ' ') + '\n' +
			"别名: " + (this.aliases != null ? String.join(", ", this.aliases) : ' ') + '\n' +
			"症状: " + (this.symptom != null  ? this.symptom.substring(0, maxLength) : ' ') + '\n' +
			"饮食: " + (this.diet != null  ? this.diet.substring(0, maxLength) : ' ') + '\n' +
			"治疗: " + (this.treat != null  ? this.treat.substring(0, maxLength) : ' ') + '\n' +
			"预防: " + (this.prevention != null  ? this.prevention.substring(0, maxLength) : ' ') + '\n' +
			"并发症: " + (this.complication != null  ? this.complication.substring(0, maxLength) : ' ') + '\n' +
			"鉴别: " + (this.identification != null  ? this.identification.substring(0, maxLength) : ' ') + '\n' +
			"病因: " + (this.cause != null  ? this.cause.substring(0, maxLength) : ' ') + '\n';
    }
    */
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExamination() {
		return examination;
	}
	public void setExamination(String examination) {
		this.examination = examination;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public List<String> getAliases() {
		return aliases;
	}
	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}
	public String getSymptom() {
		return symptom;
	}
	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}
	public String getDiet() {
		return diet;
	}
	public void setDiet(String diet) {
		this.diet = diet;
	}
	public String getTreat() {
		return treat;
	}
	public void setTreat(String treat) {
		this.treat = treat;
	}
	public String getPrevention() {
		return prevention;
	}
	public void setPrevention(String prevention) {
		this.prevention = prevention;
	}
	public String getComplication() {
		return complication;
	}
	public void setComplication(String complication) {
		this.complication = complication;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getCause() {
		return cause;
	}
	public void setCause(String cause) {
		this.cause = cause;
	}
	
}
