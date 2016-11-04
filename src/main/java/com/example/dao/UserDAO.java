package com.example.dao;

import java.util.Date;
import java.util.List;

import com.example.domain.UserInfo;
import com.example.model.ContractPerson;
import com.example.model.HeartRate;
import com.example.model.Record;
import com.example.model.Temperature;
import com.example.model.User;

public interface UserDAO {

	//保存用户
	public void save(User user);
	
	//根据user_id查找用户
	public User findUser(long userId);
	
	//查找用户
	public User findUser(UserInfo userInfo);

	//保存用户体温
	public void saveUserTemperature(Temperature temperature);
	
	//保存用户心率
	public void saveUserHeartRate(HeartRate heartRate);
	
	//保存用户摔倒时的信息
	public void saveUserTumbleInfo(Record userRecord);
	
	//获取用户在该时间段内的心率信息
	public List<HeartRate> getUserHeartRateByTime(Date startDate, Date endDate,String userId);
	
	//获取用户在该时间段内的体温信息
	public List<Temperature> getUserTemperatureByTime(Date startDate, Date endDate,String userId);
	
	//保存紧急联系人信息
	public boolean saveEmergencyContractPerson(ContractPerson contractPerson);
	
	

}
