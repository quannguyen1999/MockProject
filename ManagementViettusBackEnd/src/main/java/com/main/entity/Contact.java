package com.main.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // : tự tạo get và set
@AllArgsConstructor // : tự tạo constructor có tham số
@NoArgsConstructor // : tự tạo constructor không có tham số
@ToString // : tự tạo to String
@Entity
@Table
public class Contact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1746047275731676762L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int idContact;
	@Column(columnDefinition = "nvarchar(100)")
	
	private String title;
	@Column(columnDefinition = "nvarchar(100)")
	
	private String name;
	private String email;
	@Column(columnDefinition = "nvarchar(100)")
	
	private String address;
	
	private String phone;
	
	
	@Column(columnDefinition = "nvarchar(max)")
	private String content;
	
	private LocalDate dateCreated;
}
