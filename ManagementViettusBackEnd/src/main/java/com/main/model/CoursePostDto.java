package com.main.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CoursePostDto {
	@NotEmpty
	private String title;

	@JsonIgnore
	private String userName;

	@NotEmpty
	private String content;

	@NotEmpty
	private String image;

	private Boolean typeLearn;

	@NotNull
	private float price;

	@NotEmpty
	private String linkCourse;
	
	private int idPost;
}
