package com.example.demo.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CategoryPostDto  implements Serializable {
	private int idCategory;
	
	private String name;
	
	private String content;
	
	private Boolean status;
	
	private TypeCategory typeCategory;
}
