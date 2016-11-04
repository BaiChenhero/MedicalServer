package com.example.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dao.UserDAO;
import com.example.domain.UserBodyInfo;
import com.example.domain.UserInfo;
import com.example.domain.UserTumbleInfo;
import com.example.model.ContractPerson;
import com.example.model.HeartRate;
import com.example.model.Record;
import com.example.model.Temperature;
import com.example.model.User;
import com.example.util.DateUtil;

@Service(value = "userService")
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDAO userDao;

	@Transactional(readOnly = false)
	public void addUser(User user) {
		userDao.save(user);
	}

	public boolean checkUser(UserInfo userInfo) {
		User findUser = userDao.findUser(userInfo);
		return findUser == null ? false : true;
	}

	public User login(UserInfo userInfo) {
		String result = null;
		User findUser = userDao.findUser(userInfo);
		return findUser;
	}

	public User findUserById(long userId) {
		User user = userDao.findUser(userId);
		return user;
	}
	
	@Transactional(readOnly = false)
	public void saveUserTemperature(UserBodyInfo userBodyInfo, User user) {
		Temperature temperature = new Temperature();
		temperature.setTemperature(userBodyInfo.getTemperature());
		temperature.setTime(userBodyInfo.getTemperatureDate());
		temperature.setUser(user);
		userDao.saveUserTemperature(temperature);
	}
	
	@Transactional(readOnly = false)
	public void saveUserHeartRate(UserBodyInfo userBodyInfo, User user) {
		HeartRate heartRate = new HeartRate();
		heartRate.setHeartRate(userBodyInfo.getHeartRate());
		heartRate.setTime(userBodyInfo.getHeartRateDate());
		heartRate.setUser(user);
		userDao.saveUserHeartRate(heartRate);
	}
	
	@Transactional(readOnly = false)
	public void saveUserTumbleInfo(UserTumbleInfo userTumbleInfo, User user) {
		Record userRecord = new Record();
		userRecord.setLatitude(userTumbleInfo.getLatitude());
		userRecord.setLongitude(userTumbleInfo.getLongitude());
		userRecord.setTime(userTumbleInfo.getTime());
		userRecord.setUser(user);
		userDao.saveUserTumbleInfo(userRecord);
	}

	public List<HeartRate> getUserHeartRatesByTime(Date startDate, Date endDate, String userId) {
		if(endDate == null){
			endDate = DateUtil.getCurrentDate();
		}
		List<HeartRate> userHeartRates = userDao.getUserHeartRateByTime(startDate,endDate,userId);
		return userHeartRates;
	}
	

	public List<Temperature> getUserTemperatureByTime(Date startDate,Date endDate,String userId) {
		if(endDate == null){
			endDate = DateUtil.getCurrentDate();
		}
		List<Temperature> userTemperatures = userDao.getUserTemperatureByTime(startDate,endDate,userId);
		return userTemperatures;
	}
	
	@Transactional(readOnly = false)
	public boolean saveEmergencyContractPerson(ContractPerson contractPerson) {
		boolean result = userDao.saveEmergencyContractPerson(contractPerson);
		return result;
	}

	
	


}
