package com.main.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.entity.Post;
import com.main.entity.TypeAccount;
import com.main.entity.TypePost;
import com.main.model.ErrorMessage;
import com.main.model.PostPostDto;
import com.main.service.JwtTokenService;
import com.main.service.PostService;
import com.main.service.ResponseService;
import com.main.validator.PostValidator;
import com.querydsl.core.types.Predicate;

@RestController
@RequestMapping(PostController.API_POST)
public class PostController {
	//đường link chung api cho bài đăng
	public final static String API_POST = "/api/v1/post";

	//default token
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	//to crud
	@Autowired
	private PostService postService;

	//to validate
	@Autowired
	private PostValidator postValidator;

	//to get username, typeAccount,... from token
	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private ResponseService responseService;

	//create post base username on token
	@PostMapping(value = "/create")
	public Object createCategory(@Valid @RequestBody PostPostDto postPostDto,
			BindingResult result,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = postValidator.validatePostPost(postPostDto, result);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		postPostDto.setUserName(jwtTokenService.getUserName(authorizationV2.replace("Bearer ", "")));
		postService.savePost(postPostDto);
		return responseService.getResponseCustom("request.createSuccess", null, HttpStatus.CREATED);
	}

	//delete post through id
	@DeleteMapping(value = "/delete")
	public Object createCategory(@Param("id") String id,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = postValidator.validateIdPost(id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		postService.deletePostById(Integer.parseInt(id));
		return responseService.getResponseCustom("request.deleteSuccess", null, HttpStatus.CREATED);
	}

	//list post
	@GetMapping(value = "/list")
	public Object getList() {
		return responseService.getResponseCustom("request.getListSuccess", 
				postService.list(), HttpStatus.OK);
	}

	//list post by idCategory and typePost
	@GetMapping(value = "/listByIdCategoryAndTypePost")
	public Object getListByIdCategoryAndTypePost(
			@RequestParam(defaultValue = "1") Integer idCategory, 
			@RequestParam(defaultValue ="MENU") TypePost typePost
			) {

		return responseService.getResponseCustom("request.getListSuccess", 
				postService.findByIdCategoryAndTypePost(idCategory, typePost), HttpStatus.OK);
	}

	//list post pagination by pageNo and pageSize
	@GetMapping(value = "/listPagination")
	public Object getListCategoryPagination(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2,
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize
			){
		TypeAccount typeAccount = TypeAccount.valueOf(jwtTokenService.getTypeAccount(authorizationV2)); 
		Page<PostPostDto> pageImpl = null;
		if(typeAccount == TypeAccount.COLLABORATOR) {
			pageImpl=postService.listPaginationByUserName(pageNo,pageSize,jwtTokenService.getUserName(authorizationV2.replace("Bearer ", "")));
		}else {
			pageImpl=postService.listPagination(pageNo,pageSize);
		}
		return responseService.getResponseCustom("request.getListSuccess", pageImpl, HttpStatus.OK);
	}

	//findAll version 2
	@GetMapping(value = "/findAll")
	public Object findAll(
			@QuerydslPredicate(root = Post.class) Predicate predicate,
			Pageable pageable
			){
		return responseService.getResponseCustom("request.getListSuccess", 	postService.findAll(predicate, pageable), HttpStatus.OK);

	}

	//findName version 2
	@GetMapping(value = "/findName")
	public Object findName(
			@RequestParam(defaultValue = "") String name,
			Pageable pageable
			){
		if(pageable.getPageSize() >= 6) {
			pageable =	PageRequest.of(pageable.getPageNumber(), 5);
		}
		return responseService.getResponseCustom("request.getListSuccess", 	
				postService.findName(name.replaceAll("-"," "), pageable), HttpStatus.OK);

	}

	//for admin
	@GetMapping(value = "/listPostCourse")
	public Object getListPostCourse(@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2
			){
		return responseService.getResponseCustom("request.getListSuccess", 	
				postService.listAllPostTypeMenuAndCategoryIsCourseAndCategoryStatusIsTrue(), HttpStatus.OK);
	}

	//detail post by idPost
	@GetMapping(value = "/detail")
	public Object getListCategoryPagination(
			Model model,
			@RequestParam(defaultValue = "0") String idPost
			){
		List<ErrorMessage> listErrorResponses = postValidator.validateDetailPost(idPost);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		}
		return responseService.getResponseCustom("request.getDetailSuccess", 	
				postService.findPostById(Integer.parseInt(idPost)), HttpStatus.OK);
	}

	//list post pagination with idPost or idCategory with no secure
	@GetMapping(value = "/listPaginationNoAuth")
	public Object getListPostPaginationWithNoAuth(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize,
			@RequestParam(defaultValue = "-1") Integer idPost,
			@RequestParam(defaultValue = "-1") Integer idCategory
			){
		return responseService.getResponseCustom("request.getListSuccess", 	
				idPost != -1 ? postService.listPaginationWithIdPostAndStatusTrue(pageNo,pageSize, idPost) : postService.listPaginationWithIdCategoryAndStatusTrue(pageNo,pageSize, idCategory),
						HttpStatus.OK);
	}

	//find all post pagination by id category and name start with 
	@GetMapping(value = "/findCategoryGlossaryWithNameStarting")
	public Object getListCategoryPaginationWithNoAuth(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "2") Integer pageSize,
			@RequestParam(defaultValue = "1") Integer idCategory,
			@RequestParam(defaultValue = "A") String name
			){
		//return default id category
		return responseService.getResponseCustom("request.getListSuccess", 	
				postService.listPaginationWithIdCategoryAndStatusTrueAndNameStartWith(pageNo,
						pageSize, idCategory, name),
							HttpStatus.OK);
	}

	//detail glossary by idPost
	@GetMapping(value = "/detailGlossary")
	public Object getListDetailGlossary(
			@RequestParam(defaultValue = "1") String idPost
			){
		List<ErrorMessage> listErrorResponses = postValidator.validateIdPostGlossary(idPost);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		};
		//return default id category
		return responseService.getResponseCustom("request.getDetailSuccess", 	
				postService.detailPostGlossary(Integer.parseInt(idPost)),
							HttpStatus.OK);
	}


	//update post by id
	@PutMapping(value = "/update")
	public Object updatePost(@Param("id") String id,
			@RequestBody PostPostDto postPostDto,
			@RequestHeader(DEFAULT_ACCESS_TOKEN_HEADER) String authorizationV2) {
		List<ErrorMessage> listErrorResponses = postValidator.validatePostPut(postPostDto, id);
		if(listErrorResponses.size() > 0) {
			return responseService.getResponseBadRequest("request.badRequest", listErrorResponses);
		};
		return responseService.getResponseCustom("request.updateSuccess", 	
				postService.updatePost(postPostDto,Integer.parseInt(id)),
							HttpStatus.OK);
	}
}
