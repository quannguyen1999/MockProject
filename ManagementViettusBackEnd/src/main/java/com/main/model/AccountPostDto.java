package com.main.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.entity.TypeAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class AccountPostDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	@ApiModelProperty(notes = "user name of user", required = true)
	@NotEmpty(message = "userName can't be null")
	private String userName;
	
//	@ApiModelProperty(notes = "password of user", required = true)
	@NotEmpty(message = "password can't be null")
	@Pattern(regexp ="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$^&+=])(?=\\S+$).{8,}$",message = "password must have a-z and A-Z and [!@#$%^&*()] and [0-9]")
	private String password;
	
	@JsonIgnore
	private TypeAccount typeAccount;
}
