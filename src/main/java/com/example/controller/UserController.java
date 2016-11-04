package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.EmergencyContractPerson;
import com.example.domain.UserInfo;
import com.example.model.ContractPerson;
import com.example.model.User;
import com.example.service.UserService;
import com.example.service.UserTreatService;

@Controller
@RequestMapping(value = "/userInfo",method = RequestMethod.POST)
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserTreatService userTreatService;

	/**
	 * description:将注册的用户添加到数据库中
	 * 
	 * @return 添加是否成功
	 */
	@ResponseBody
	@RequestMapping(value = "/addUser",method=RequestMethod.POST)
	public String addUser(@ModelAttribute UserInfo userInfo) {
		String result = null;
		boolean isExist = userService.checkUser(userInfo);
		if (isExist) {
			result = "false";
		} else {
			User user = new User();
			user.setUsername(userInfo.getUsername());
			user.setPassword(userInfo.getPassword());
			user.setPhone(userInfo.getPhone());
			userService.addUser(user);
			result = "true";
		}
		return result;
	}

	/**
	 * descrpiton:用户登录
	 * 
	 * @return 成功：true 失败：用户已存在，请重新注册
	 */
	@ResponseBody
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public String login(@ModelAttribute UserInfo userInfo,
			HttpServletRequest request) {
		System.out.println("------");
		String userId = "-1";
		System.out.println(userInfo.getUsername());
		User user = userService.login(userInfo);
		if (user != null) {
			//用该登录的用户保存在session中
			request.getSession().setAttribute("user", user);
			userId = Long.toString(user.getId());
		}
		return userId;
	}
	
	/**
	 * 保存紧急联系人
	 * @return 成功：true 失败：false
	 */
	@ResponseBody
	@RequestMapping(value = "/saveContractPerson",method = RequestMethod.GET)
	public boolean saveEmergencyContractPerson(@ModelAttribute EmergencyContractPerson emergencyContractPerson){
		long user_id = emergencyContractPerson.getUserId();
		User user = userService.findUserById(user_id);
		ContractPerson contractPerson = new ContractPerson();
		contractPerson.setRelationship(emergencyContractPerson.getRelationship());
		contractPerson.setTelephone(emergencyContractPerson.getTelephone());
		contractPerson.setUsername(emergencyContractPerson.getUsername());
		contractPerson.setUser(user);
		boolean result = userService.saveEmergencyContractPerson(contractPerson);
		return result;
	}
	
	
}
