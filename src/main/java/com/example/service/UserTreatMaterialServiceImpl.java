package com.example.service;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.dao.DoctorDao;
import com.example.dao.UserDAO;
import com.example.dao.UserTreatMaterialDAO;
import com.example.domain.UploadMaterial;
import com.example.domain.UserTreatMaterial;
import com.example.model.Doctor;
import com.example.model.Material;
import com.example.model.User;
import com.example.model.UserTreat;
import com.example.util.SendMail;

@Service(value = "userTreatMaterialService")
@Transactional(readOnly = true)
public class UserTreatMaterialServiceImpl implements UserTreatMaterialService {
	private static final String SUBJECT = "病人病情信息";
	private static final String PICBASEPATH = "/Users/fukaiming/Development/JavaWorkspace/MedicalServerFileServer/fileUpload/";
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private DoctorDao doctorDao;
	@Autowired
	private UserTreatMaterialDAO userTreatMaterialDAO;

	private Set<Material> picPaths = new HashSet<Material>();

	@Transactional(readOnly = false)
	public void save(UserTreatMaterial userTreatMaterial,
			MultipartFile[] files, HttpServletRequest request) {
		// 判断file数组不能为空并且长度大于0
		if (files != null && files.length > 0) {
			// 循环获取file数组中得文件
			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				// 保存文件
				String picPath = saveFile(file, request);
				if (picPath != null) {
					Material material = new Material(picPath);
					picPaths.add(material);
				}
			}
		}
		UserTreat userTreat = new UserTreat();
		long doctorId = userTreatMaterial.getDoctorId();
		if (doctorId > 0) {
			Doctor doctor = doctorDao.findDoctorById(doctorId);
			userTreat.setDoctor(doctor);
		}
		userTreat.setClassType(userTreatMaterial.getClassType());
		userTreat.setDescription(userTreatMaterial.getDescription());
		userTreat.setMaterials(picPaths);
		long userId = userTreatMaterial.getUserId();
		if (userId > 0) {
			User user = userDAO.findUser(userId);
			userTreat.setUser(user);
		}
		userTreatMaterialDAO.save(userTreat);
	}

	private String saveFile(MultipartFile file, HttpServletRequest request) {
		// 判断文件是否为空
		String picPath = null;
		if (!file.isEmpty()) {
			try {
				// 文件保存路径
				picPath = PICBASEPATH + file.getOriginalFilename();
				// 转存文件
				file.transferTo(new File(picPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return picPath;
	}

	// 给医生发邮件
	public void sendEmailToDoctor(UserTreatMaterial userTreatMaterial,
			List<String> picPath) {
		Doctor doctor = doctorDao.findDoctorById(userTreatMaterial
				.getDoctorId());
		User user = userDAO.findUser(userTreatMaterial.getUserId());
		String doctorEmail = doctor.getEmail();
		String username = user.getUsername();
		String subject = username + ":" + SUBJECT;
		StringBuilder sb = new StringBuilder();
		sb.append("病情类型：" + userTreatMaterial.getClassType() + "\n");
		sb.append(username + "的病情描述：\n" + userTreatMaterial.getDescription());
		String content = sb.toString();
		boolean isAddAttachment = false;
		File[] attachments = new File[picPath.size()];
		int index = 0;
		if (picPath.size() > 0) {
			for (String dir : picPath) {
				File file = new File(dir);
				attachments[index++] = file;
			}
			isAddAttachment = true;
		} else {
			isAddAttachment = false;
		}
		SendMail sm = new SendMail(doctorEmail, subject, content,
				isAddAttachment, attachments);
		sm.send();

	}
	
	@Transactional(readOnly = false)
	public void saveUserMaterials(UploadMaterial uploadtMaterial,
			HttpServletRequest request) {
		UserTreat userTreat = new UserTreat();
		String classType = uploadtMaterial.getClassType();
		userTreat.setClassType(uploadtMaterial.getClassType());
		userTreat.setDescription(uploadtMaterial.getSickDescription());
		if (uploadtMaterial.getDoctorId() == null || uploadtMaterial.getDoctorId().equals("")) {
			// 从符合该classType的医生中随机挑选出其中的一个
			List<Doctor> doctors = doctorDao.findDoctorsByClassType(classType);
			Doctor doctor = getRandomDoctorByClassType(doctors);
			userTreat.setDoctor(doctor);
		}
		
		// 设置材料所在的地址
		Set<Material> materials = new HashSet<Material>();
		String picPath1 = uploadtMaterial.getPicRemotePath1();
		if (picPath1 != null && !picPath1.equals("")) {
			Material material = new Material();
			material.setPicPath(picPath1);
			materials.add(material);
		}
		
		String picPath2 = uploadtMaterial.getPicRemotePath2();
		if (picPath2 != null && !picPath2.equals("")) {
			Material material = new Material();
			material.setPicPath(picPath1);
			materials.add(material);
		}
		
		String picPath3 = uploadtMaterial.getPicRemotePath3();
		if (picPath3 != null && !picPath3.equals("")) {
			Material material = new Material();
			material.setPicPath(picPath1);
			materials.add(material);
		}
		userTreat.setMaterials(materials);
		
		userTreat.setSickTime(uploadtMaterial.getSickTime());
		
		//设置User
		User user = userDAO.findUser(Long.parseLong(uploadtMaterial.getUserId()));
		userTreat.setUser(user);
		
		userTreatMaterialDAO.save(userTreat);
	}

	// 从符合该classType的医生中随机挑选出其中的一个
	private Doctor getRandomDoctorByClassType(List<Doctor> doctors) {
		Doctor doctor = null;
		int doctorNumbers = doctors.size();

		while (doctor == null) {
			int doctorId = new Random().nextInt(doctorNumbers);
			Doctor randomDoctor = doctors.get(doctorId);
			if (randomDoctor != null) {
				return randomDoctor;
			}
		}
		return null;
	}

}
