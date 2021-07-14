package com.main.model;

import java.util.List;


import lombok.AllArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class CustomResponse {
	private List<ErrorMessage> listErrorMessages;
	private String message;
	private Object data;
}
