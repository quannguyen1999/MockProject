package com.main.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
public class Registration extends Auditable<String> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idRegistration;
	
	@Column(length = 15, columnDefinition = "varchar(32) default 'COURSE'")
	@Enumerated(value = EnumType.STRING)
	private TypeRegistration typeRegistration = TypeRegistration.COURSE;
	
	@Column(columnDefinition="int default 0")
	private Boolean status = false;
	
	@Column(columnDefinition = "nvarchar(100)")
	private String nameCustomer;
	
	private String phone;
	
	private String email;
	
	private String URLFacebook;
	
	private TypePay typePay;
	
	@ManyToMany(cascade = javax.persistence.CascadeType.REMOVE)
	private List<Course> listComboCourse;
	
	@OneToOne
	private ComboCourse comboCourse;
}