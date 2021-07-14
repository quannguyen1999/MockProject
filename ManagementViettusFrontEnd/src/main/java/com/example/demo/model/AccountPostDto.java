package com.example.demo.model;

import java.io.Serializable;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class AccountPostDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	
	private String password;
}
