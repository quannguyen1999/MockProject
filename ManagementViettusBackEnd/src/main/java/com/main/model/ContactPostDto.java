package com.main.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data // : tự tạo get và set
@AllArgsConstructor // : tự tạo constructor có tham số
@NoArgsConstructor // : tự tạo constructor không có tham số
@ToString // : tự tạo to String
public class ContactPostDto implements Serializable {
	/**
	 * 
	 */
private static final long serialVersionUID = 8470348611343328699L;
	
	@NotEmpty(message = "title is required")
	private String title;
	
	@NotEmpty(message = "name is required")
	private String name;
	
	@NotEmpty(message = "email is required")
	@Pattern(regexp = "^([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22))*\\x40([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d))*(\\.\\w{2,})+$", message = "Email is invalid. Check again")
	private String email;
	
	private String address;
	
	@NotEmpty(message = "phone is required")
	@Pattern(regexp = "^(03|05|07|08|09|01[2|6|8|9])+([0-9]){8}$", message = "phone is invalid. Check again")
	private String phone;
	
	@NotEmpty(message = "content is required")
	private String content;
}
