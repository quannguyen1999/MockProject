package com.main.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.main.entity.TypeAccount;
import com.main.model.ErrorMessage;
import com.main.model.PostPostDto;
import com.main.service.CategoryService;
import com.main.service.JwtTokenService;
import com.main.service.PostService;
import com.main.service.ResponseService;
import com.main.service.ValidatorService;

@Component
public class PostValidator {

	private static final String FIELD_ID = "id";
	
	private static final String FIELD_USERNAME = "userName";
	
	private static final String FIELD_IDCATEGORY = "idCategory";
	
	private static  final String FIELD_IDPOSTSELF = "idPostSelf";
	
	@Autowired
	private ValidatorService validatorService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private ResponseService responseService;
	
	public List<ErrorMessage> validatePostPost(PostPostDto postPostDto, BindingResult result) {
		List<ErrorMessage> listErrorResponses = validatorService.checkBindingResult(result);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		if(categoryService.findCategoryById(postPostDto.getIdCategory()) == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_IDCATEGORY,
					responseService.getMessage("category.id.notFound")));
		}
		if(postPostDto.getIdPostSelf() != null) {
			if(postService.findPostById(postPostDto.getIdPostSelf()) == null) {
				listErrorResponses.add(new ErrorMessage(FIELD_IDPOSTSELF,
						responseService.getMessage("post.idPostSelf.notFound")));
			}
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validatePostPut(PostPostDto postPostDto, String id) {
		List<ErrorMessage> listErrorResponses = validateIdPost(id);
		if(listErrorResponses.size()>=1) {
			return listErrorResponses;
		}
		if(postPostDto.getIdCategory() != null) {
			if(categoryService.findCategoryById(postPostDto.getIdCategory()) == null) {
				listErrorResponses.add(new ErrorMessage(FIELD_IDCATEGORY,
						responseService.getMessage("category.id.notFound")));
			}
		}
		
		if(postPostDto.getIdPostSelf() != null) {
			if(postPostDto.getIdPostSelf() != null) {
				if(postService.findPostById(postPostDto.getIdPostSelf()) == null) {
					listErrorResponses.add(new ErrorMessage(FIELD_IDPOSTSELF,
							responseService.getMessage("listErrorResponses")));
				}
			}
		}
		
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateIdPost(String idPost) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idPostConvert = 0;
		try {
			idPostConvert=Integer.parseInt(idPost);
		} catch (Exception e) {
			// TODO: handle exception
				listErrorResponses.add(new ErrorMessage(FIELD_ID,
						responseService.getMessage("post.id.invalid")));
				return listErrorResponses;
		}
		if(postService.findPostById(idPostConvert) == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("post.id.notFound")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateIdPostGlossary(String idPost){
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idPostConvert = 0;
		try {
			idPostConvert=Integer.parseInt(idPost);
		} catch (Exception e) {
			// TODO: handle exception
				listErrorResponses.add(new ErrorMessage(FIELD_ID,
						responseService.getMessage("post.id.invalid")));
				return listErrorResponses;
		}
		if(postService.detailPostGlossary(idPostConvert) == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("post.id.notFound")));
		}
		
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateDetailPost(String idPost) {
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idPostConvert = 0;
		try {
			idPostConvert=Integer.parseInt(idPost);
		} catch (Exception e) {
			// TODO: handle exception
				listErrorResponses.add(new ErrorMessage(FIELD_ID,
						responseService.getMessage("post.id.invalid")));
				return listErrorResponses;
		}
		if(postService.findPostById(idPostConvert) == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("post.id.notFound")));
		}
		return listErrorResponses;
	}
	
	public List<ErrorMessage> validateDeletePost(String idPost, String token){
		List<ErrorMessage> listErrorResponses = new ArrayList<ErrorMessage>();
		int idPostConvert = 0;
		try {
			idPostConvert=Integer.parseInt(idPost);
		} catch (Exception e) {
				listErrorResponses.add(new ErrorMessage(FIELD_ID,
						responseService.getMessage("post.id.invalid")));
				return listErrorResponses;
		}
		
		String username = jwtTokenService.getUserName(token.replace("Bearer ", ""));
		String typeAccount = jwtTokenService.getTypeAccount(token.replace("Bearer ", ""));
		
		PostPostDto postPostDtoFind = postService.findPostById(idPostConvert);
		if(postPostDtoFind == null) {
			listErrorResponses.add(new ErrorMessage(FIELD_ID,
					responseService.getMessage("post.id.notFound")));
		}else if(TypeAccount.ADMIN.toString().equalsIgnoreCase(typeAccount) == false &&
				username.equalsIgnoreCase(postPostDtoFind.getUserName()) == false) {
			listErrorResponses.add(new ErrorMessage(FIELD_USERNAME,
					responseService.getMessage("post.id.unauthor")));
		}
		return listErrorResponses;
	}
}
