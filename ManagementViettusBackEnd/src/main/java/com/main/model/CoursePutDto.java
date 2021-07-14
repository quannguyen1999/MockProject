package com.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CoursePutDto {
	private String title;

	private String content;

	private String image;

	private Boolean typeLearn;
	
	private Boolean status;

	private String linkCourse;
	
	private int idPost;
}
