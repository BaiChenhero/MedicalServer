package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.Evaluate;
import com.example.service.EvaluateService;

@Controller
@RequestMapping(value="/evaluateInfo")
public class EvaluateInfoController {
	
	@Autowired
	private EvaluateService evaluateService;
	//获得网友对该医生的评价信息
	@RequestMapping(value ="/getEvaluateInfoByDoctor",method=RequestMethod.GET)
	public @ResponseBody List<Evaluate> getEvaluateInfoByDoctor(@RequestParam("doctorId")long doctorId){
		List<Evaluate>evaluates = evaluateService.findEvaluatesByDoctorId(doctorId);
		return evaluates;
	}
}
