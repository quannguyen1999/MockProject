package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class PostPostDto {
	private int idPost;

	private String name;

	private String content;

	private String userName;

	private Integer idCategory;

	private Integer idPostSelf;

	private TypePost typePost;

	private String image;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String dateCreated;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String dateUpdated;

	private Boolean status;

	private String nameCategory;
}
