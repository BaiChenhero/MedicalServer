package com.example.dao;

import java.util.List;

import com.example.model.Doctor;
import com.example.model.Evaluate;

public interface EvaluateDao {
	
	//根据医生id，查找网友对其的评价信息
	public List<Evaluate> findEvaluatesByDoctor(Doctor doctor);

}
