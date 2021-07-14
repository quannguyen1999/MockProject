package com.example.demo.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class RegistrationGetDto{
	private int idRegistration;

	private TypeRegistration typeRegistration;
	
	private Boolean status;
	
	private String nameCustomer;
	
	private String phone;
	
	private String email;
	
	private String URLFacebook;
	
	private TypePay typePay;
	
	private List<Integer> listComboCourse;
	
	private Date dateCreated;
}
