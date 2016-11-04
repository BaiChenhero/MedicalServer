package com.example.dao;

import java.util.List;

import com.example.model.Doctor;
import com.example.model.Evaluate;

public interface DoctorDao {

	public List<Doctor> findDoctorsByClassType(String classType);
	
	public Doctor findDoctorById(long doctorId);
	
	public List<Evaluate> findEvaluateInfoByDoctor(long doctorId);

	public void updateDoctorInfo(Doctor doctor);

}
