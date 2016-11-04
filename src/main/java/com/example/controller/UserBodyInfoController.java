package com.example.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.InformationInfo;
import com.example.model.HeartRate;
import com.example.model.Temperature;
import com.example.service.UserService;
import com.example.util.DateUtil;

@Controller
@RequestMapping(value="/userInfo")
public class UserBodyInfoController {
	@Autowired
	private UserService userService;
	
	//根据给出的时间段，获取用户在该时间段内的心率信息
	@RequestMapping(value="/getHeartRate",method=RequestMethod.POST)
	public @ResponseBody List<HeartRate> getUserHeartRateByTime(@ModelAttribute InformationInfo informationInfo){
		String startDate = informationInfo.getStartDate();
		String endDate = informationInfo.getEndDate();
		Date startDate1 = null;
		if( startDate != null){
			startDate1 = DateUtil.transferToDate(startDate);
		}
		
		Date endDate1 = null;
		if(endDate != null){
			endDate1 = DateUtil.transferToDate(endDate);
		}
		List<HeartRate> userHeartRates = userService.getUserHeartRatesByTime(startDate1,endDate1,informationInfo.getUserId());
		return userHeartRates;
	}
	
	//根据给出的时间段，获取用户在该时间段内的体温信息
		@RequestMapping(value="/getTemperature")
		public @ResponseBody List<Temperature> getUserTemperatureByTime(@ModelAttribute InformationInfo informationInfo){
			String startDate = informationInfo.getStartDate();
			String endDate = informationInfo.getEndDate();
			Date startDate1 = null;
			if(startDate != null){
				startDate1 = DateUtil.transferToDate(startDate);
			}
			Date endDate1 = null;
			if(endDate != null){
				endDate1 = DateUtil.transferToDate(endDate);
			}
			List<Temperature> userTemperatures = userService.getUserTemperatureByTime(startDate1,endDate1,informationInfo.getUserId());
			return userTemperatures;
		}
}
