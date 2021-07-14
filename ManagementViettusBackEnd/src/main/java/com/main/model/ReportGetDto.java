package com.main.model;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportGetDto {
	private String message;

	private Date createdDate;
}
