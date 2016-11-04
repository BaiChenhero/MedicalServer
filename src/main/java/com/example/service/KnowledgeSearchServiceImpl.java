package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "knowledgeSearchService")
public class KnowledgeSearchServiceImpl implements KnowledgeSearchService {
	@Autowired
	private OntologyService ontologyService;
	
	private String KB_MODEL_FILE = "file:E:/Codes/Java/MedicalServer/src/main/resources/owl/OMKB.owl";
	private String KB_RULE_FILE = "file:E:/Codes/Java/MedicalServer/src/main/resources/owl/OMKB.rules";
	private String QUERY_STRING_DISEASE = "PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/OMKB#> " +  
            "SELECT ?症状 ?检查 ?手术 ?药物  " +  
            "WHERE { " + 
            "OPTIONAL {ont:%1$s ont:包含症状 ?症状} " +
            "OPTIONAL {ont:%1$s ont:进行检查 ?检查} " +
            "OPTIONAL {ont:%1$s ont:进行手术 ?手术} " +
            "OPTIONAL {ont:%1$s ont:可用药物 ?药物} " +
            "}";

	public String queryInference(String q) {
		ontologyService.createInfModel(KB_RULE_FILE, KB_MODEL_FILE);
		String queryString = String.format(QUERY_STRING_DISEASE, q);
		String result = ontologyService.queryInf(queryString); 
		System.out.println(result);
		return result;
	}
}
