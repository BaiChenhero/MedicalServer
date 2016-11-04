package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "knowledgeManageService")
public class KnowledgeManageServiceImpl implements KnowledgeManageService {
	@Autowired
	private OntologyService ontologyService;
	
	private String KB_MODEL_FILE = "file:E:/Codes/Java/medical-kb/src/main/resources/OMKB.owl";
	private String KB_RULE_FILE = "file:E:/Users/whypro/Desktop/omkb/owl/OMKB.rules";
	private String KB_MODEL_FILE_OUT = "file:E:/Codes/Java/medical-kb/src/main/resources/OMKB_out.owl";
	private String QUERY_STRING = "PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/OMKB#> " +  
            "SELECT ?症状 ?检查 " +  
            "WHERE {?症状 ont:参考检查 ?检查} ";  

	public void createOntologyInstance() {
		
		

		long begin = System.currentTimeMillis();
		ontologyService.readFromFile(KB_MODEL_FILE);
    	long stamp_1 = System.currentTimeMillis();
    	System.out.println(stamp_1-begin);
    	ontologyService.loadDepartmentIndividualFromDB();
    	ontologyService.buildIndividuals();
    	ontologyService.buildProperties();
    	long stamp_2 = System.currentTimeMillis();
    	System.out.println(stamp_2-stamp_1);
    	ontologyService.writeToFile(KB_MODEL_FILE_OUT);
    	long stamp_3 = System.currentTimeMillis();
    	System.out.println(stamp_3-stamp_2);
		
	}
	
}
