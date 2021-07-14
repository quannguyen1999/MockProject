package com.example.demo.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // : tự tạo get và set
@AllArgsConstructor // : tự tạo constructor có tham số
@NoArgsConstructor // : tự tạo constructor không có tham số
@ToString // : tự tạo to String
public class ContactPostDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8470348611343328699L;

	private String title;

	private String name;

	private String email;

	private String address;

	private String phone;

	private String content;
}
