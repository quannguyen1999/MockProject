package com.main.model;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.intellij.lang.annotations.RegExp;

import com.main.entity.TypePay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class RegistrationPostDto {
	@NotEmpty(message = "nameCustomer is required")
	private String nameCustomer;
	
	@RegExp(prefix = "(09|03|07|08|05)+([0-9]{8})\\\\b")
	private String phone;
	
	@Email(message = "email is invalid")
	private String email;
	
	@NotEmpty(message = "url is required")
	private String urlFacebook;
	
	private TypePay typePay;
	
	@NotNull(message = "listComboCourse is required")
	private List<Integer> listComboCourse;
}
