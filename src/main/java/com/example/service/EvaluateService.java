package com.example.service;

import java.util.List;

import com.example.model.Evaluate;

public interface EvaluateService {
	
	//获取对该医生的评价信息
	public List<Evaluate> findEvaluatesByDoctorId(long doctorId);

}
