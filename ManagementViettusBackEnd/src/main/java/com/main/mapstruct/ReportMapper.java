package com.main.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.main.entity.Report;
import com.main.model.ReportGetDto;
import com.main.model.ReportPostDto;

@Mapper(
        componentModel = "spring"
)
public interface ReportMapper {
	@Mappings({
		@Mapping(target = "createdBy", ignore = true),
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "lastModifiedBy", ignore = true),
		@Mapping(target = "lastModifiedDate", ignore = true),
		@Mapping(target = "id", ignore = true)
	})
	Report reportGetDtoToReport(ReportGetDto reportGetDto);
	
	@Mappings({
		@Mapping(target = "createdBy", ignore = true),
		@Mapping(target = "createdDate", ignore = true),
		@Mapping(target = "lastModifiedBy", ignore = true),
		@Mapping(target = "lastModifiedDate", ignore = true),
		@Mapping(target = "id", ignore = true)
	})
	Report reportPostDtoToReport(ReportPostDto reportPostDto);
	
	ReportGetDto reportToReportGetDto(Report report);
	
	List<ReportGetDto> listReportToListReportGetDto(List<Report> listReports);
}
