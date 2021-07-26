package com.main.model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.main.entity.TypePost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class PostPostDto implements Serializable {
	@JsonProperty(access = Access.READ_ONLY)
	private int idPost;

	@NotEmpty(message = "name is required")
	private String name;

	@NotEmpty(message = "content is required")
	private String content;

	@JsonProperty(access = Access.READ_ONLY)
	private String userName;

	@NotNull
	private Integer idCategory;

	private Integer idPostSelf;

	@NotNull
	private TypePost typePost;

	@NotEmpty(message = "image is required")
	private String image;

	@JsonProperty(access = Access.READ_ONLY)
	private LocalDate dateCreated;

	@JsonProperty(access = Access.READ_ONLY)
	private LocalDate dateUpdated;
	
	private Boolean status;
	
	@JsonProperty(access = Access.READ_ONLY)
	private String nameCategory;

	public PostPostDto(@NotEmpty(message = "name is required") String name,
			@NotEmpty(message = "content is required") String content, String userName, @NotNull Integer idCategory,
			Integer idPostSelf, @NotNull TypePost typePost, @NotEmpty(message = "image is required") String image) {
		super();
		this.name = name;
		this.content = content;
		this.userName = userName;
		this.idCategory = idCategory;
		this.idPostSelf = idPostSelf;
		this.typePost = typePost;
		this.image = image;
	}


}
