package com.example.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CourseGetDto {
	private int idCourse;

	private String title;

	private String userName;

	private String content;

	private String image;

	private Boolean typeLearn;

	private float price;

	private String linkCourse;

	private Boolean status;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String createdAt;
	
	private int idPost;
	
	private String namePost;
}
