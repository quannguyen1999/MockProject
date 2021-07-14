package com.main.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.main.entity.TypeAccount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountGetDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotEmpty(message = "userName can't be null")
	private String userName;
	
	private TypeAccount typeAccount;
}
