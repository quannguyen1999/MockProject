package com.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.entity.Account;
import com.main.entity.Report;
import com.main.service.ReportService;
import com.main.service.ResponseService;
import com.querydsl.core.types.Predicate;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(ReportController.API_REPORT)
public class ReportController {
	//đường link chung api cho danh sach dang ky
	public final static String API_REPORT = "/api/v1/report";

	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ResponseService responseService;

	//return list
	@GetMapping(value = "/count")
	public Object returnList() {
		return responseService.getResponseCustom("request.getListSuccess",reportService.countList(), HttpStatus.OK);
	}
	
	//find all version 2 
	@Operation(summary = "find all report by pagination, you can find with query what you want")
	@GetMapping(value = "/findAll")
	public Object findAll(@QuerydslPredicate(root = Report.class) Predicate predicate,
			Pageable pageable) {
		return responseService.getResponseCustom("request.updateSuccess",reportService.findAll(predicate, pageable), HttpStatus.OK);
	}
	
	@Operation(summary = "delete all repor")
	@GetMapping(value = "/deleteAll")
	public Object deleteAll() {
		return responseService.getResponseCustom("request.updateSuccess",reportService.deleteAll(), HttpStatus.OK);
	}
	
	
}
