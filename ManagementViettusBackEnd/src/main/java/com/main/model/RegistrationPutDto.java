package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data//: tự tạo get và set
@ToString//: tự tạo to String
public class RegistrationPutDto {
	private Boolean status;
}
