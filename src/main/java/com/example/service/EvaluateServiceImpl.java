package com.example.service;


import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.DoctorDao;
import com.example.dao.EvaluateDao;
import com.example.model.Doctor;
import com.example.model.Evaluate;

@Service(value = "evaluateService")
@Transactional(readOnly = true)
public class EvaluateServiceImpl implements EvaluateService {
	
	@Autowired
	private EvaluateDao evaluateDao;
	
	@Autowired
	private DoctorDao doctorDao;
	
	public List<Evaluate> findEvaluatesByDoctorId(long doctorId) {
		Doctor doctor = doctorDao.findDoctorById(doctorId);
		List<Evaluate> evaluates = evaluateDao.findEvaluatesByDoctor(doctor);
		return evaluates;
	}

}
