package com.main.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
@Entity
@Table
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private int idCategory;
	
	@Column(columnDefinition = "nvarchar(100)")
	private String name;
	
	@Column(columnDefinition = "nvarchar(500)")
	private String content;
	
	private Boolean status;
	
	private TypeCategory typeCategory;
	
	public Category(int idCategory) {
		super();
		this.idCategory = idCategory;
	}
}
