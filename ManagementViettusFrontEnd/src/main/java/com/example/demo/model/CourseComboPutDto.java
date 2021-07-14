package com.example.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // : tự tạo get và set
@AllArgsConstructor // : tự tạo constructor có tham số
@NoArgsConstructor // : tự tạo constructor không có tham số
@ToString // : tự tạo to String
public class CourseComboPutDto {
	private String id;
	
	private Boolean status;
}
