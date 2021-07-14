package com.main.model;

import java.util.Date;
import java.util.List;

import com.main.entity.ComboCourse;
import com.main.entity.TypePay;
import com.main.entity.TypeRegistration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
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
	
	private ComboCourse comboCourse;
	
	private Date dateCreated;
	
	private String nameCourse;
}
