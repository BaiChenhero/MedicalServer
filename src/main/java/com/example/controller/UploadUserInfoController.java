package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.domain.UserBodyInfo;
import com.example.domain.UserTumbleInfo;
import com.example.model.User;
import com.example.service.UserService;

@Controller
@RequestMapping(value = "/upload")
public class UploadUserInfoController {
	@Autowired
	private UserService userService;

	// 将获取的用户体温和心率信息保存到数据库中
	@RequestMapping(value="/heartRatetemperatureInfo",method=RequestMethod.GET)
	public void uploadUserBodyInfo(@ModelAttribute UserBodyInfo userBodyInfo) {
		long userId = userBodyInfo.getUser_id();
		if(userId > 0){
			User user = userService.findUserById(userId);
			double temperatureValue = userBodyInfo.getTemperature();
			if (temperatureValue > 0) {
				userService.saveUserTemperature(userBodyInfo,user);
			}
			float heartRate = userBodyInfo.getHeartRate();
			if(heartRate > 0){
				userService.saveUserHeartRate(userBodyInfo,user);
			}
		}
	}
	
	//将获取的用户摔倒时的信息保存到数据库中
	public void uploadUserTumbleInfo(@ModelAttribute UserTumbleInfo userTumbleInfo){
		long userId= userTumbleInfo.getUser_id();
		if(userId > 0){
			User user = userService.findUserById(userId);
			userService.saveUserTumbleInfo(userTumbleInfo,user);
		}
	}
}
