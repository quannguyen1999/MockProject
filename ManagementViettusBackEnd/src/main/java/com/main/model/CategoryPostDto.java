package com.main.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.main.entity.TypeCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data//: tự tạo get và set
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class CategoryPostDto  implements Serializable {
	private static final long serialVersionUID = 1L;

	private int idCategory;
	
	@NotEmpty(message = "name is required")
	private String name;
	
	@NotEmpty(message = "content is required")
	private String content;
	
	private Boolean status;
	
	@NotNull
	private TypeCategory typeCategory;

	public CategoryPostDto(@NotEmpty(message = "name is required") String name,
			@NotEmpty(message = "content is required") String content) {
		super();
		this.name = name;
		this.content = content;
	}

	public CategoryPostDto(int idCategory, @NotEmpty(message = "name is required") String name,
			@NotEmpty(message = "content is required") String content, @NotNull TypeCategory typeCategory) {
		super();
		this.idCategory = idCategory;
		this.name = name;
		this.content = content;
		this.typeCategory = typeCategory;
	}

	


	
	
}
