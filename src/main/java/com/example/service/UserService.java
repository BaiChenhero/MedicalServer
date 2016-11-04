package com.example.service;

import java.util.Date;
import java.util.List;

import com.example.domain.UserBodyInfo;
import com.example.domain.UserInfo;
import com.example.domain.UserTumbleInfo;
import com.example.model.ContractPerson;
import com.example.model.HeartRate;
import com.example.model.Temperature;
import com.example.model.User;

public interface UserService {
	//添加用户
	public void addUser(User user);
	
	//查看该用户信息是否已经注册过
	public boolean checkUser(UserInfo userInfo);
	
	//登录验证
	public User login(UserInfo userInfo);
	
	//根据用户查找用户
	public User findUserById(long userId);
	
	//保存用户体温信息
	public void saveUserTemperature(UserBodyInfo userBodyInfo, User user);
	
	//保存用户心率信息
	public void saveUserHeartRate(UserBodyInfo userBodyInfo, User user);
	
	//保存用户摔倒时的信息
	public void saveUserTumbleInfo(UserTumbleInfo userTumbleInfo, User user);
	
	//获得用户在某一时间段内的心率信息
	public List<HeartRate> getUserHeartRatesByTime(Date startDate, Date endDate, String userId);
	
	//获得用户在某一时间段内的体温信息
	public List<Temperature> getUserTemperatureByTime(Date startDate,Date endDate,String userId);
	
	//保存紧急联系人信息
	public boolean saveEmergencyContractPerson(ContractPerson contractPerson);
	

}
