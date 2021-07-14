package com.example.demo.model;

import java.util.List;


import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CustomResponse {
	private List<ErrorMessage> listErrorMessages;
	private String message;
	private Object data;
}
