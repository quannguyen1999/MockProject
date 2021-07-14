package com.main.model;


import com.main.entity.TypeCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CategoryPutDto {
	private String content;
	private Boolean status;
	private TypeCategory typeCategory;
}
