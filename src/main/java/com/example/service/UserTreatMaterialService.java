package com.example.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.example.domain.UploadMaterial;
import com.example.domain.UserTreatMaterial;

public interface UserTreatMaterialService {
	//保存用户就诊信息
	public void save(UserTreatMaterial userTreatMaterial, MultipartFile[] files, HttpServletRequest request);
	
	//给医生发邮件
	public void sendEmailToDoctor(UserTreatMaterial userTreatMaterial, List<String> picPath);
	
	//保存用户就诊信息
	public void saveUserMaterials(UploadMaterial uploadtMaterial,
			HttpServletRequest request);

}
