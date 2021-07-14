package com.main.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class ContactGetDto implements  Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -2271699805072847700L;
	
	private int idContact;
	
	private String title;
	
	private String name;
	
	private String email;
	
	private String address;
	
	private String phone;
	
	private String content;
	
	private LocalDate dateCreated;
}
