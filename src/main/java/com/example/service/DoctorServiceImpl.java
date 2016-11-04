package com.example.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.dao.DoctorDao;
import com.example.model.Doctor;
import com.example.model.Evaluate;

@Service(value = "doctorService")
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {
	@Autowired
	private DoctorDao doctorDao;
	private String propertiesFile = "serverurl.properties";

	public List<Doctor> findRelativedDoctors(String classType) {
		List<Doctor> doctors = doctorDao.findDoctorsByClassType(classType);
		return doctors;
	}

	public Set<Evaluate> getEvaluateInfoByDoctorId(long doctorId) {
		Doctor doctor = doctorDao.findDoctorById(doctorId);
		Set<Evaluate> evaluates = new HashSet<Evaluate>();
		if (doctor != null) {
			evaluates = doctor.getEvaluates();
			return evaluates;
		} else {
			return null;
		}

	}

	public Doctor findDoctorById(long doctorId) {
		Doctor doctor = doctorDao.findDoctorById(doctorId);
		return doctor;
	}

	@Transactional(readOnly = false)
	public void uploadPortrait(Doctor doctor, MultipartFile file) {
		// 判断file数组不能为空并且长度大于0
		if (file != null) {
			// 保存文件
			String picPath = saveFile(file);
			if (picPath != null) {
				doctor.setPicUrl(picPath);
				doctorDao.updateDoctorInfo(doctor);
			}
		}
	}

	private String saveFile(MultipartFile file) {
		String picPath = null;
		// 读取properties文件中的内容
		String picDirPath = readFromProperties(propertiesFile);
		// 判断文件是否为空

		if (!file.isEmpty()) {
			try {
				// 文件保存路径
				 picPath = picDirPath+ file.getOriginalFilename();
				// 转存文件
				file.transferTo(new File(picPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return picPath;
	}

	private String readFromProperties(String propertiesFile) {
		String picDirPath = null;
		Properties properties = new Properties();
		InputStream in = DoctorServiceImpl.class.getResourceAsStream("/serverurl.properties");
		try {
			properties.load(in);
			picDirPath = properties.getProperty("server.pic.url");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return picDirPath;
	}

}
