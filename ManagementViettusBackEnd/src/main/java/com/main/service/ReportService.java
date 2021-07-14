package com.main.service;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.main.model.AccountGetDto;
import com.main.model.ReportGetDto;
import com.main.model.ReportPostDto;
import com.querydsl.core.types.Predicate;

public interface ReportService {
	public boolean create(ReportPostDto reportPostDto);
	
	public List<ReportGetDto> getList();
	
	public long countList();
	
	//list Account version 2
	PageImpl<ReportGetDto> findAll(Predicate predicate, Pageable pageable);
	
	public Boolean deleteAll();
	
	public ReportGetDto getNewReport();
}
