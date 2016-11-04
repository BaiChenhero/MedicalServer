package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.Doctor;
import com.example.service.DoctorService;
import com.example.service.EvaluateService;

@Controller
@RequestMapping(value = "/doctorInfoByClass")
public class DoctorInfoController {
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private EvaluateService evaluateService;
	// 找医生模块
	@RequestMapping(value = "/getDoctorInfoByClass", method = RequestMethod.GET)
	public @ResponseBody List<Doctor> getDoctorInfoByClass(
			@RequestParam(value = "classType", defaultValue = "内科") String classType) {
		List<Doctor> doctors = doctorService.findRelativedDoctors(classType);
		return doctors;
	}

	// 医生上传头像模块
	@RequestMapping(value = "/savePic", method = RequestMethod.POST)
	public void savePic(@RequestParam("files") MultipartFile file,
			@RequestParam("doctorId") long doctorId, HttpServletRequest request) {
//Web应用根目录：request.getServletContext().getRealPath("/")
		Doctor doctor = doctorService.findDoctorById(doctorId);
		if (doctor != null) {
			doctorService.uploadPortrait(doctor, file);
		}
	}
	
	

}
