package com.example.service;

import org.apache.jena.rdf.model.InfModel;


public interface OntologyService {
	public InfModel createInfModel(String rulePath, String modelPath);
	public void readFromFile(String filename);
	public void statistics();
	public void writeToFile(String filename);
	public void loadDepartmentIndividualFromDB();
	public void buildIndividuals();
	public void buildProperties();
	public String query(String queryString);
	public String queryInf(String queryString);
}