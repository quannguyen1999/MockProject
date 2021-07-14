package com.main.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.main.entity.TypeAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data//: tự tạo get và set
@AllArgsConstructor//: tự tạo constructor có tham số
@NoArgsConstructor//: tự tạo constructor không có tham số
@ToString//: tự tạo to String
public class AccountCreateDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "userName can't be null")
	private String userName;
	
	@NotNull(message = "typeAccount can't be null")
	private TypeAccount typeAccount;
}
