package com.main.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCourse;

	@Column(columnDefinition = "nvarchar(100)")
	private String title;

	@ManyToOne
	@JoinColumn(name = "userName")
	private Account account;

	@Column(columnDefinition = "nvarchar(max)")
	private String content;

	private String image;

	private Boolean typeLearn;

	private float price;

	private String linkCourse;

	private Boolean status;

	private LocalDate createdAt;
	
	@ManyToOne
	private Post post;

	public Course(int idCourse) {
		super();
		this.idCourse = idCourse;
	}
	
	
}
