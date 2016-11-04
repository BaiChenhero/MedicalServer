package com.example.mongomodel;

import java.io.Serializable;

import org.springframework.data.annotation.Id;  


public class Medication implements Serializable {  
  
    private static final long serialVersionUID = 1L;  
    
    @Id  
    String id;
    String generic_name;
    String trade_name;
    String generic_name_fixed;
    String trade_name_fixed;
    String ingredient;
    String description;
    String indications;
    String disease;
    String specification;
    String dosage;
    String adverse_reactions;
    String contraindications;
    String precautions;
    String interactions;
    String preservation;
    String term_of_validity;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGenericName() {
		return generic_name_fixed;
	}
	public void setGenericName(String generic_name) {
		this.generic_name_fixed = generic_name;
	}
	public String getTradeName() {
		return trade_name_fixed;
	}
	public void setTradName(String trade_name) {
		this.trade_name_fixed = trade_name;
	}
	public String getIngredient() {
		return ingredient;
	}
	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIndications() {
		return indications;
	}
	public void setIndications(String indications) {
		this.indications = indications;
	}
	public String getDisease() {
		return disease;
	}
	public void setDisease(String disease) {
		this.disease = disease;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getAdverse_reactions() {
		return adverse_reactions;
	}
	public void setAdverse_reactions(String adverse_reactions) {
		this.adverse_reactions = adverse_reactions;
	}
	public String getContraindications() {
		return contraindications;
	}
	public void setContraindications(String contraindications) {
		this.contraindications = contraindications;
	}
	public String getPrecautions() {
		return precautions;
	}
	public void setPrecautions(String precautions) {
		this.precautions = precautions;
	}
	public String getInteractions() {
		return interactions;
	}
	public void setInteractions(String interactions) {
		this.interactions = interactions;
	}
	public String getPreservation() {
		return preservation;
	}
	public void setPreservation(String preservation) {
		this.preservation = preservation;
	}
	public String getTermOfValidity() {
		return term_of_validity;
	}
	public void setTermOfValidity(String term_of_validity) {
		this.term_of_validity = term_of_validity;
	}
}
