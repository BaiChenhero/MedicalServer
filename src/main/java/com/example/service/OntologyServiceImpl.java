package com.example.service;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mongomodel.Department;
import com.example.mongomodel.Disease;
import com.example.mongomodel.Examination;
import com.example.mongomodel.Medication;
import com.example.mongomodel.Surgery;
import com.example.mongomodel.Symptom;



@Service(value = "ontologyService")
@Transactional(readOnly = true)
public class OntologyServiceImpl implements OntologyService {
	
	
	private OntModel ontModel;
	private InfModel infModel;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static String DISEASE_COLLECTION = "disease";
	private static String DEPARTMENT_COLLECTION = "department";
	private static String SYMPTOM_COLLECTION = "symptom";
	private static String EXAMINATION_COLLECTION = "examination";
	private static String SURGERY_COLLECTION = "surgery";
	private static String MEDICATION_COLLECTION = "medication";
	
	private static String NS_URI_PREFIX = "OMKB";
	
	private static Map<String, List<String>> terminologyMap = new HashMap<String, List<String>>();;
	
	public InfModel createInfModel(String rulePath, String modelPath) {
		List<Rule> rules = Rule.rulesFromURL(rulePath);
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setOWLTranslation(true);
		reasoner.setDerivationLogging(true);
		reasoner.setTransitiveClosureCaching(true);
		
		Model model = ModelFactory.createDefaultModel();
		model.read(modelPath);
		ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF, model);
		Resource configuration = ontModel.createResource();
		configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
		
		infModel = ModelFactory.createInfModel(reasoner, ontModel);
		return infModel;
	}

	public void readFromFile(String filename) {
		ontModel.read("file:"+filename);
	}
	
	public void statistics() {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		OntClass departmentOntClass = ontModel.getOntClass(baseURI+"科室");	
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		OntClass symptomOntClass = ontModel.getOntClass(baseURI+"症状");
		OntClass examinationOntClass = ontModel.getOntClass(baseURI+"检查");
		OntClass surgeryOntClass = ontModel.getOntClass(baseURI+"手术");
		OntClass medicationOntClass = ontModel.getOntClass(baseURI+"药物");	
		
		System.out.println(departmentOntClass.listInstances().toList().size());
		System.out.println(diseaseOntClass.listInstances().toList().size());
		System.out.println(symptomOntClass.listInstances().toList().size());
		System.out.println(examinationOntClass.listInstances().toList().size());
		System.out.println(surgeryOntClass.listInstances().toList().size());
		System.out.println(medicationOntClass.listInstances().toList().size());

	}
	
	public void writeToFile(String filename) {
		
		// statistics();
		
		try {
			FileOutputStream writerStream = new FileOutputStream(filename);
			ontModel.write(writerStream, "RDF/XML");
			writerStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadDepartmentIndividualFromDB() {
		// 从 MongoDB 数据库中读取科室信息，然后将其转化为实例加入本体模型
		
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		OntClass c = ontModel.getOntClass(baseURI+"科室");
		
		List<Department> departments = mongoTemplate.findAll(Department.class, DEPARTMENT_COLLECTION);
		for (Department department : departments) {
			Individual childIndividual = null;
			// 循环直到没有父科室为止
			while (true) {
				String departmentName = department.getName();
				// 不优雅的过滤方式
				if (departmentName.endsWith("科") && !departmentName.equals("生活百科")) {
					// System.out.println(departmentName);
					// 判断个体是否已存在
					if (ontModel.getIndividual(baseURI+departmentName) == null) {
						Individual individual = c.createIndividual(baseURI+departmentName);
						// System.out.println(departmentName);
						if (childIndividual != null) {
							individual.addProperty(ontModel.getObjectProperty(baseURI+"包含科室"), childIndividual);
							childIndividual.addProperty(ontModel.getObjectProperty(baseURI+"属于科室"), individual);
						}
						String parentDepartmentName = department.getParent();
						// 是否存在父科室
						if (parentDepartmentName != null) {
							Department parentDepartment = mongoTemplate.findOne(
								new Query(Criteria.where("name").is(parentDepartmentName)), 
								Department.class, 
								DEPARTMENT_COLLECTION
							);
							if (parentDepartment != null) {
								childIndividual = individual;
								department = parentDepartment;
							} else {
								break;
							}
						} else {
							break;
						}
					} else {
						break;
					}
				} else {
					break;
				}
			}
		}
	}
	
	private boolean isValidName(String name) {
		if (name == null) {
			return false;
		}
		else if (
			name.contains("(") || name.contains(")") || name.contains("　") || 
			name.contains("“") || name.contains("”") || name.contains("°")
		) {
			// System.out.println(name);
			return false;
		}
		return true;
	}
	
	public void buildIndividuals() {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		OntClass symptomOntClass = ontModel.getOntClass(baseURI+"症状");
		OntClass examinationOntClass = ontModel.getOntClass(baseURI+"检查");
		OntClass surgeryOntClass = ontModel.getOntClass(baseURI+"手术");
		OntClass medicationOntClass = ontModel.getOntClass(baseURI+"药物");	
		
		List<Disease> diseases = mongoTemplate.findAll(Disease.class, DISEASE_COLLECTION);
		for (Disease disease : diseases) {
			// diseaseOntClass.createIndividual(baseURI+disease.getName());
			
			List<String> names = new ArrayList<String>();
			names.add(disease.getName());
			if (disease.getAliases() != null) {
				names.addAll(disease.getAliases());
			}
			for (String name : names) {
				if (!isValidName(name)) {
					continue;
				}
				// ontModel.createIndividual(baseURI+name, diseaseOntClass);
				diseaseOntClass.createIndividual(baseURI+name);
			}
		}
		
		List<Symptom> symptoms = mongoTemplate.findAll(Symptom.class, SYMPTOM_COLLECTION);
		for (Symptom symptom : symptoms) {
			String name = symptom.getName();
			if (!isValidName(name)) {
				continue;
			}
			symptomOntClass.createIndividual(baseURI+name);
		}
		
		List<Examination> examinations = mongoTemplate.findAll(Examination.class, EXAMINATION_COLLECTION);
		for (Examination examination : examinations) {
			String name = examination.getName();
			if (!isValidName(name)) {
				continue;
			}
			examinationOntClass.createIndividual(baseURI+name);
		}

		List<Surgery> surgeries = mongoTemplate.findAll(Surgery.class, SURGERY_COLLECTION);
		for (Surgery surgery : surgeries) {
			String name = surgery.getName();
			if (!isValidName(name)) {
				continue;
			}
			surgeryOntClass.createIndividual(baseURI+name);
		}
		
		List<Medication> medications = mongoTemplate.findAll(Medication.class, MEDICATION_COLLECTION);
		for (Medication medication : medications) {
			String genericName = medication.getGenericName();
			if (isValidName(genericName)) {
				medicationOntClass.createIndividual(baseURI+genericName);
			}
			String tradeName = medication.getTradeName();
			if (isValidName(tradeName)) {
				medicationOntClass.createIndividual(baseURI+tradeName);
			}
		}
		
		List<String> diseaseTerminologyList = new ArrayList<String>();
		for (ExtendedIterator<?> i = diseaseOntClass.listInstances(true); i.hasNext();) {
			Individual diseaseIndividual = (Individual) i.next();
			diseaseTerminologyList.add(diseaseIndividual.getLocalName());
			terminologyMap.put("disease", diseaseTerminologyList);
		}
		
		List<String> symptomTerminologyList = new ArrayList<String>();
		for (ExtendedIterator<?> i = symptomOntClass.listInstances(true); i.hasNext();) {
			Individual symptomIndividual = (Individual) i.next();
			// System.out.println(symptomIndividual.getLocalName());
			symptomTerminologyList.add(symptomIndividual.getLocalName());
			terminologyMap.put("symptom", symptomTerminologyList);
		}
		
		List<String> examinationTerminologyList = new ArrayList<String>();
		for (ExtendedIterator<?> i = examinationOntClass.listInstances(true); i.hasNext();) {
			Individual examinationIndividual = (Individual) i.next();
			// System.out.println(symptomIndividual.getLocalName());
			examinationTerminologyList.add(examinationIndividual.getLocalName());
			terminologyMap.put("examination", examinationTerminologyList);
		}
		
		List<String> surgeryTerminologyList = new ArrayList<String>();
		for (ExtendedIterator<?> i = surgeryOntClass.listInstances(true); i.hasNext();) {
			Individual surgeryIndividual = (Individual) i.next();
			surgeryTerminologyList.add(surgeryIndividual.getLocalName());
			terminologyMap.put("surgery", surgeryTerminologyList);
		}
		
		List<String> medicationTerminologyList = new ArrayList<String>();
		for (ExtendedIterator<?> i = medicationOntClass.listInstances(true); i.hasNext();) {
			Individual medicationIndividual = (Individual) i.next();
			medicationTerminologyList.add(medicationIndividual.getLocalName());
			terminologyMap.put("medication", medicationTerminologyList);
		}
	}
	
	private void buildDiseaseProperties(Disease disease) {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		// System.out.println(baseURI);
		
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		OntClass symptomOntClass = ontModel.getOntClass(baseURI+"症状");
		OntClass examinationOntClass = ontModel.getOntClass(baseURI+"检查");
		OntClass surgeryOntClass = ontModel.getOntClass(baseURI+"手术");
		
		ObjectProperty surgeryProperty = ontModel.getObjectProperty(baseURI+"进行手术");
		ObjectProperty surgeryPropertyReverse = ontModel.getObjectProperty(baseURI+"手术适应症");
		ObjectProperty medicationProperty = ontModel.getObjectProperty(baseURI+"可应用药物");
		ObjectProperty medicationPropertyReverse = ontModel.getObjectProperty(baseURI+"可治疗疾病");
		ObjectProperty symptomProperty = ontModel.getObjectProperty(baseURI+"包含症状");
		ObjectProperty symptomPropertyReverse = ontModel.getObjectProperty(baseURI+"属于疾病");
		ObjectProperty examinationProperty = ontModel.getObjectProperty(baseURI+"进行检查");
		ObjectProperty examinationPropertyReverse = ontModel.getObjectProperty(baseURI+"检查适应症");
		ObjectProperty departmentProperty = ontModel.getObjectProperty(baseURI+"参考科室");
		ObjectProperty identificationProperty = ontModel.getObjectProperty(baseURI+"鉴别疾病");
		ObjectProperty complicationProperty = ontModel.getObjectProperty(baseURI+"并发疾病");
		
		// System.out.println(disease.getName());
		// Individual individual = diseaseOntClass.createIndividual(baseURI+disease.getName());
		Individual individual = ontModel.getIndividual(baseURI+disease.getName());
		if (individual == null) {
			return;
		}
		// System.out.println(individual.getLocalName());
		if (disease.getAliases() != null) {
			for (String alias : disease.getAliases()) {
				Individual sameIndividual = ontModel.getIndividual(baseURI+alias);
				if (sameIndividual != null) {
					individual.addSameAs(sameIndividual);
					sameIndividual.addSameAs(individual);
				}
			}
		}
		
		// 症状
		String rawSymptom = disease.getSymptom();
		if (rawSymptom != null) {
			for (String terminology : terminologyMap.get("symptom")) {
				if (rawSymptom.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual symptomIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (symptomIndividual != null) {
						if (symptomIndividual.hasOntClass(symptomOntClass)) {
							individual.addProperty(symptomProperty, symptomIndividual);
							symptomIndividual.addProperty(symptomPropertyReverse, individual);
						}
						else {
							// System.out.println("1: "+terminology);
						}
					}
					else {
						// System.out.println("2: "+terminology);
					}
				}
			}
		}
		
		
		// 检查
		String rawExamination = disease.getExamination();
		if (rawExamination != null) {
			for (String terminology : terminologyMap.get("examination")) {
				if (rawExamination.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual examinationIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (examinationIndividual != null) {
						if (examinationIndividual.hasOntClass(examinationOntClass)) {
							individual.addProperty(examinationProperty, examinationIndividual);
							examinationIndividual.addProperty(examinationPropertyReverse, individual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
		}
		
		// 手术和药物
		String rawTreat = disease.getTreat();
		if (rawTreat != null) {
			for (String terminology : terminologyMap.get("surgery")) {
				if (rawTreat.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual surgeryIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (surgeryIndividual != null) {
						if (surgeryIndividual.hasOntClass(surgeryOntClass)) {
							individual.addProperty(surgeryProperty, surgeryIndividual);
							surgeryIndividual.addProperty(surgeryPropertyReverse, individual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
			
			for (String terminology : terminologyMap.get("medication")) {
				if (rawTreat.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual medicationIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (medicationIndividual != null) {
						if (medicationIndividual.hasOntClass(medicationIndividual)) {
							individual.addProperty(medicationProperty, medicationIndividual);
							medicationIndividual.addProperty(medicationPropertyReverse, individual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
		}
		
		// 鉴别
		String rawIdentification = disease.getIdentification();
		if (rawIdentification != null) {
			for (String terminology : terminologyMap.get("disease")) {
				if (rawIdentification.contains(terminology) && terminology != individual.getLocalName()) {
					// 在本体模型中查询是否包含该疾病实例
					Individual diseaseIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (diseaseIndividual != null) {
						if (diseaseIndividual.hasOntClass(diseaseOntClass)) {
							individual.addProperty(identificationProperty, diseaseIndividual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
		}
		
		// 并发
		String rawComplication = disease.getComplication();
		if (rawComplication != null) {
			for (String terminology : terminologyMap.get("disease")) {
				if (rawComplication.contains(terminology) && terminology != individual.getLocalName()) {
					// 在本体模型中查询是否包含该疾病实例
					Individual diseaseIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (diseaseIndividual != null) {
						if (diseaseIndividual.hasOntClass(diseaseOntClass)) {
							individual.addProperty(complicationProperty, diseaseIndividual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
		}
	}
	
	
	private void buildSymptomProperties(Symptom symptom) {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		OntClass examinationOntClass = ontModel.getOntClass(baseURI+"检查");
		
		
		ObjectProperty examinationProperty = ontModel.getObjectProperty(baseURI+"进行检查");
		ObjectProperty examinationPropertyReverse = ontModel.getObjectProperty(baseURI+"检查适应症");
		// Property identificationProperty = ontModel.getProperty(baseURI+"鉴别症状");
		
		// System.out.println(symptom.getName());
		Individual individual = ontModel.getIndividual(baseURI+symptom.getName());
		if (individual == null) {
			return;
		}
		
		// 检查
		String rawExamination = symptom.getExamination();
		if (rawExamination != null) {
			for (String terminology : terminologyMap.get("examination")) {
				if (rawExamination.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual examinationIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (examinationIndividual != null) {
						if (examinationIndividual.hasOntClass(examinationOntClass)) {
							individual.addProperty(examinationProperty, examinationIndividual);
							examinationIndividual.addProperty(examinationPropertyReverse, individual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
		
					}
				}
			}
		}
	}
	
	private void buildExaminationProperties(Examination examination) {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		OntClass examinationOntClass = ontModel.getOntClass(baseURI+"检查");
		
		//Property examinationProperty = ontModel.getProperty(baseURI+"进行检查");
		//Property examinationPropertyReverse = ontModel.getProperty(baseURI+"检查适应症");
		
		// System.out.println(examination.getName());
		// Individual individual = ontModel.getIndividual(baseURI+examination.getName());
	}
	
	
	private void buildSurgeryProperties(Surgery surgery) {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		
		OntClass surgeryOntClass = ontModel.getOntClass(baseURI+"手术");
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		
		ObjectProperty diseaseProperty = ontModel.getObjectProperty(baseURI+"手术适应症");
		ObjectProperty diseasePropertyReverse = ontModel.getObjectProperty(baseURI+"进行手术");
		
		// System.out.println(surgery.getName());
		Individual individual = ontModel.getIndividual(baseURI+surgery.getName());
		if (individual == null) {
			return;
		}
		
		// 适应症
		String rawIndication = surgery.getSymptom();
		if (rawIndication != null) {
			for (String terminology : terminologyMap.get("disease")) {
				if (rawIndication.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual diseaseIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (diseaseIndividual != null) {
						if (diseaseIndividual.hasOntClass(diseaseOntClass)) {
							individual.addProperty(diseaseProperty, diseaseIndividual);
							diseaseIndividual.addProperty(diseasePropertyReverse, individual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
		}
	}
	
	
	private void buildMedicationProperties(Medication medication) {
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		
		OntClass medicationOntClass = ontModel.getOntClass(baseURI+"药物");
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
		
		ObjectProperty diseaseProperty = ontModel.getObjectProperty(baseURI+"可治疗疾病");
		ObjectProperty diseasePropertyReverse = ontModel.getObjectProperty(baseURI+"可应用药物");
		
		//System.out.println(medication.getGenericName());
		String genericName = medication.getGenericName();
		String tradeName = medication.getTradeName();
		Individual individual = null;
		Individual sameIndividual = null;
		if (genericName != null) {
			individual = ontModel.getIndividual(baseURI+genericName);
		}
		
		if (tradeName != null) {
			sameIndividual = ontModel.getIndividual(baseURI+tradeName);
		}
		
		if (individual == null) {
			return;
		} 
		
		if (sameIndividual != null) {
			individual.addSameAs(sameIndividual);
			sameIndividual.addSameAs(individual);
		}
		
		
		// 适应症
		String rawDisease = medication.getDisease();
		if (rawDisease != null) {
			for (String terminology : terminologyMap.get("disease")) {
				if (rawDisease.contains(terminology)) {
					// 在本体模型中查询是否包含该疾病实例
					Individual diseaseIndividual = ontModel.getIndividual(baseURI+terminology);
					// 存在，添加关联
					if (diseaseIndividual != null) {
						if (diseaseIndividual.hasOntClass(diseaseOntClass)) {
							individual.addProperty(diseaseProperty, diseaseIndividual);
							diseaseIndividual.addProperty(diseasePropertyReverse, individual);
						}
						else {
							
						}
					}
					// 不存在，创建
					else {
						
					}
				}
			}
		}
	}
	
		
	public void buildProperties() {
		// 从 MongoDB 数据库中读取疾病信息，然后将其转化为实例加入本体模型
		String baseURI = ontModel.getNsPrefixURI(NS_URI_PREFIX);
		OntClass diseaseOntClass = ontModel.getOntClass(baseURI+"疾病");
				
		List<Disease> diseases = mongoTemplate.findAll(Disease.class, DISEASE_COLLECTION);
		for (Disease disease : diseases) {
			buildDiseaseProperties(disease);
		}
		
		List<Symptom> symptoms = mongoTemplate.findAll(Symptom.class, SYMPTOM_COLLECTION);
		for (Symptom symptom : symptoms) {
			buildSymptomProperties(symptom);
		}
		
		List<Examination> examinations = mongoTemplate.findAll(Examination.class, EXAMINATION_COLLECTION);
		for (Examination examination : examinations) {
			buildExaminationProperties(examination);
		}
		
		List<Surgery> surgeries = mongoTemplate.findAll(Surgery.class, SURGERY_COLLECTION);
		for (Surgery surgery : surgeries) {
			buildSurgeryProperties(surgery);
		}
		
		List<Medication> medications = mongoTemplate.findAll(Medication.class, MEDICATION_COLLECTION);
		for (Medication medication : medications) {
			buildMedicationProperties(medication);
		}
		
		
	}
	
	
	public void printAllClasses() {
		for (Iterator<OntClass> i = ontModel.listClasses(); i.hasNext(); ) {  
            OntClass c = i.next();
            
            if (!c.isAnon()) {  //测试c是否匿名  
                System.out.print("Class");  
                System.out.println(c.getModel().getGraph().getPrefixMapping().shortForm(c.getURI()));  
                  
                if (c.getLocalName().equals("ConsumableThing")) {  
                    System.out.println("  URI@" + c.getURI());  
                    System.out.println("Animal's EquivalentClass is " + c.getEquivalentClass());  
                    System.out.println("[Comments:" + c.getEquivalentClass().getComment("EN")  + "]");  
                }
                for (ExtendedIterator<?> j = c.listInstances(); j.hasNext(); ) {
                	Individual individual = (Individual) j.next();  
                	System.out.println(individual.getLocalName());
                }
            }
		}
	}
	
	
	
	public String queryMedicationByDisease(String disease) {
		String queryString = 
			"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
			"SELECT ?药物 \n" +
			"WHERE {\n" +
			"    ont:"+disease+" ont:可应用药物 ?药物 .\n"+
			"}";

		return query(queryString);
	}
	
	public String queryComplicationByDisease(String disease) {
		String queryString = 
				"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
				"SELECT ?疾病 \n" +
				"WHERE {\n" +
				"    ont:"+disease+" ont:并发疾病 ?疾病 .\n"+
				"}";

		return query(queryString);
	}
	
	public String queryExaminationByDisease(String disease) {
		String queryString = 
				"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
				"SELECT ?检查 \n" +
				"WHERE {\n" +
				"    ont:"+disease+" ont:进行检查 ?检查 .\n"+
				"}";

		return query(queryString);
	}
	
	public String querySurgeryByDisease(String disease) {
		String queryString = 
				"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
				"SELECT ?手术 \n" +
				"WHERE {\n" +
				"    ont:"+disease+" ont:进行手术 ?手术 .\n"+
				"}";

		return query(queryString);
	}
	
	public String queryDiseaseBySymptoms(String[] symptoms) {
		
		String queryString = 
			"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
			"SELECT ?疾病\n" +
			"WHERE {\n";
		for (String symptomName : symptoms) {
			queryString += "    ?疾病 ont:包含症状 ont:"+ symptomName +" .\n";
		}
		queryString += "}";
		
		/*
		String queryString = 
			"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
			"SELECT ?疾病 ?症状\n" +
			"WHERE {\n" +
			"    ?疾病 ont:包含症状 ont:口渴 .\n"+
			"    ?疾病 ont:包含症状 ont:共济失调 .\n"+
			"}";
		*/
		return query(queryString);
	}
	
	public String test() {

		String queryString = 
			"PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/MKB#>\n" +
			"SELECT ?疾病 ?药物 ?手术 ?检查\n" +
			"WHERE {\n" +
			"?疾病 ont:包含症状 ont:心绞痛.\n" +
			"?疾病 ont:包含症状 ont:心悸.\n" +
			"?疾病 ont:包含症状 ont:两手颤抖.\n" +
			"?疾病 ont:包含症状 ont:眩晕.\n" +
			"OPTIONAL {?疾病 ont:进行检查 ?检查}\n"+
			"OPTIONAL {?疾病 ont:进行手术 ?手术}\n"+
			"OPTIONAL {?疾病 ont:可应用药物 ?药物}\n";
		
		queryString += "}";
		
		return query(queryString);

	}
	
	public String query(String queryString) {
		System.out.println(queryString);
		
		org.apache.jena.query.Query query = QueryFactory.create(queryString);
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, ontModel);
		ResultSet results = qe.execSelect();
		// Output query results	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		ResultSetFormatter.outputAsJSON(baos, results);
		// ResultSetFormatter.out(System.out, results, query);
		// Important - free up resources used running the query
		qe.close();
		return baos.toString();
		// return null;
	}
	
	public String queryInf(String queryString) {  
        System.out.println(queryString);
  
        org.apache.jena.query.Query query = QueryFactory.create(queryString);     
        QueryExecution qe = QueryExecutionFactory.create(query, this.infModel);  
        ResultSet results = qe.execSelect();  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		ResultSetFormatter.outputAsJSON(baos, results);
        // ResultSetFormatter.out(System.out, results, query);  
        qe.close(); 
        return baos.toString();
    }
	
	public void inference() {
		String ruleFile = "file:E:/Users/whypro/Desktop/omkb/owl/OMKB.rules";  
        String modelFile = "file:E:/Users/whypro/Desktop/omkb/owl/OMKB.owl";  
        String queryString = "PREFIX ont:<http://www.semanticweb.org/whypro/ontologies/2016/01/OMKB#> " +  
            "SELECT ?症状 ?检查 " +  
            "WHERE {?症状 ont:参考检查 ?检查} ";  

        createInfModel(ruleFile, modelFile);
        queryInf(queryString);  
		
	}
}

