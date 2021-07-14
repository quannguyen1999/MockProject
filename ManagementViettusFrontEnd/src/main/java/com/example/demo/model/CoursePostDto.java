package com.example.demo.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CoursePostDto {
	private String title;

	private String userName;

	private String content;

	private String image;

	private Boolean typeLearn;

	private float price;

	private String linkCourse;
	
	private int idPost;
}
