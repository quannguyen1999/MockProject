package com.main.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.main.entity.Account;
import com.main.entity.Report;
import com.main.mapstruct.ReportMapper;
import com.main.model.AccountGetDto;
import com.main.model.ReportGetDto;
import com.main.model.ReportPostDto;
import com.main.repository.ReportRepository;
import com.main.service.ReportService;
import com.querydsl.core.types.Predicate;

@Service
public class ReportImpl implements ReportService{
	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private ReportMapper reportMapper;
	
	@Override
	public boolean create(ReportPostDto reportPostDtoe) {
		try {
			reportRepository.save(reportMapper.reportPostDtoToReport(reportPostDtoe));
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	@Override
	public List<ReportGetDto> getList() {
		return reportMapper.listReportToListReportGetDto(reportRepository.findAll());
	}

	@Override
	public long countList() {
		return reportRepository.findAll().size();
	}

	@Override
	public PageImpl<ReportGetDto> findAll(Predicate predicate, Pageable pageable) {
		Page<Report> page = reportRepository.findAll(predicate,pageable.getPageSize() >= 6 ? PageRequest.of(0, 5) : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"createdDate")));
		return new PageImpl<ReportGetDto>(
				page.getContent().stream().map(report-> new ReportGetDto(report.getMessage(),report.getCreatedDate())).collect(Collectors.toList()),
				pageable, page.getTotalElements()
				);
	}

	@Override
	public Boolean deleteAll() {
		try {
			 reportRepository.deleteAll();;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	@Override
	public ReportGetDto getNewReport() {
		return reportMapper.reportToReportGetDto(reportRepository.findTop1ByOrderByCreatedDateDesc());
	}
	
	
}
