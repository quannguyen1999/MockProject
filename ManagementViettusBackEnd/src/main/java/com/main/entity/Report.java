package com.main.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
@Entity
@Table
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Report extends Auditable<String>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String message;
}
