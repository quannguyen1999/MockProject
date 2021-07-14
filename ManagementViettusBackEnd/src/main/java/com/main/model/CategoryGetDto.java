package com.main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.main.entity.TypeCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CategoryGetDto  implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idCategory;
	
	private String name;
	
	private TypeCategory typeCategory;
	
	private List<PostPostDto> listPost = new ArrayList<PostPostDto>();
}
