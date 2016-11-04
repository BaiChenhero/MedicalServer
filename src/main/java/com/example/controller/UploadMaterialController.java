package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.UploadMaterial;
import com.example.domain.UserBodyInfo;
import com.example.domain.UserTreatMaterial;
import com.example.service.UserTreatMaterialService;
@Controller
@RequestMapping(value = "/uploadUserInfo")
public class UploadMaterialController {
	@Autowired
	private UserTreatMaterialService userTreatMaterialService;
	
	@RequestMapping(value="/saveUserTreatMaterial",method=RequestMethod.GET)
	//当用户点击选择指定医生时，填写病情信息后，保存到用户就诊信息表中。（1）以邮件的形式发送给指定医生
	public void saveUserTreatMaterial(@ModelAttribute UserTreatMaterial userTreatMaterial,@RequestParam("files") MultipartFile[] files,HttpServletRequest request){
		userTreatMaterialService.save(userTreatMaterial,files,request);
		String picPathDir = request.getSession().getServletContext().getRealPath("/")+ "upload/";
		List<String> picPath = new ArrayList<String>();
		for(MultipartFile file : files){
			picPath.add(picPathDir+file.getOriginalFilename());
		}
		
		userTreatMaterialService.sendEmailToDoctor(userTreatMaterial,picPath);
	}
	
	//保存图片到本地文件系统中，并返回该图片保存的路径
	@RequestMapping(value="/saveUserMaterials",method=RequestMethod.POST)
	//当用户点击选择指定医生时，填写病情信息后，保存到用户就诊信息表中。（1）以邮件的形式发送给指定医生
	public void saveUserMaterial(@ModelAttribute UploadMaterial uploadtMaterial,HttpServletRequest request){
		userTreatMaterialService.saveUserMaterials(uploadtMaterial,request);
	}
	
	
}
