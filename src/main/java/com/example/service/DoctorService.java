package com.example.service;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.example.model.Doctor;
import com.example.model.Evaluate;

public interface DoctorService {
	//获取医生擅长类型为classType的所有医生
	public List<Doctor> findRelativedDoctors(String classType);

	//获取该医生网友对其的评价信息
	public Set<Evaluate> getEvaluateInfoByDoctorId(long doctorId);
	
	//医生上传头像
	public void uploadPortrait(Doctor doctor, MultipartFile file);
	
	//根据id查找医生
	public Doctor findDoctorById(long doctorId);

	
	
}
