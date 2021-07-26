package com.main.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.main.entity.Category;
import com.main.entity.Post;
import com.main.entity.TypeCategory;
import com.main.entity.TypePost;
import com.main.mapstruct.PostMapper;
import com.main.model.PostPostDto;
import com.main.repository.PostRepository;
import com.main.service.PostService;
import com.querydsl.core.types.Predicate;

@Service
public class PostImpl implements PostService{

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostMapper postMapper;

	@Override
	public Boolean savePost(PostPostDto postPostDto) {
		Post post = postMapper.postToPostPostDto(postPostDto);
		post.setDateCreated(LocalDate.now());
		post.setDateUpdated(LocalDate.now());
		post.setStatus(true);
		if(post.getPost().getIdPost() == 0) {
			post.setPost(null);
		}else {
			post.setPost(postRepository.findById(postPostDto.getIdPostSelf()).get());
		}

		try {
			postRepository.save(post);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public PostPostDto findPostById(int id) {
		Optional<Post> postFind = postRepository.findById(id);
		return postFind.isPresent() ? postMapper.postPostDtoToPost(postFind.get()) : null;
	}

	@Override
	public Boolean deletePostById(int id) {
		try {
			postRepository.deleteById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@CacheEvict(value = "listHeader",key = "1")
	@Override
	public PostPostDto updatePost(PostPostDto postPostDto, int idPost) {
		Post post = postRepository.findById(idPost).get();
		if(postPostDto.getName() != null && postPostDto.getName().isEmpty() == false) {
			post.setName(postPostDto.getName());
		}

		if(postPostDto.getContent() != null && postPostDto.getContent().isEmpty() == false) {
			post.setContent(postPostDto.getContent());
		}

		if(postPostDto.getIdCategory() != null) {
			post.setCategory(new Category(postPostDto.getIdCategory()));
		}

		if(postPostDto.getIdPostSelf() != null) {
			post.setPost(postRepository.findById(postPostDto.getIdPostSelf()).get());
		}

		if(postPostDto.getTypePost() != null) {
			post.setTypePost(postPostDto.getTypePost());
		}

		if(postPostDto.getImage() != null && postPostDto.getImage().isEmpty() == false) {
			post.setImage(postPostDto.getImage());
		}

		if(postPostDto.getStatus() != null) {
			post.setStatus(postPostDto.getStatus());
		}

		post.setDateUpdated(LocalDate.now());

		return postMapper.postPostDtoToPost(postRepository.save(post));
	}

	@Override
	public PageImpl<PostPostDto> listPagination(Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Post> page =  postRepository.findAll(paging);
		return returnPage(page, paging);
	}

	@Override
	public List<PostPostDto> list() {
		return postMapper.listPostToListPostDto(postRepository.findAll());
	}

	@Override
	public List<PostPostDto> findByIdCategoryAndTypePost(int idCategory, TypePost typePost) {
		return postMapper.listPostToListPostDto(postRepository.findByCategoryIdCategoryAndTypePost(idCategory, typePost));
	}

	@Override
	public PageImpl<PostPostDto> listPaginationWithIdPostAndStatusTrue(Integer pageNo, Integer pageSize,
			int idPost) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Post> page =  postRepository.findByPostIdPostAndTypePostAndStatusIsTrue(
				idPost, TypePost.POST, paging);
		return returnPage(page, paging);
	}

	@Override
	public PageImpl<PostPostDto> listPaginationWithIdCategoryAndStatusTrue(Integer pageNo, Integer pageSize,
			int idCategory) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Post> page =  postRepository.findByCategoryIdCategoryAndTypePostAndStatusIsTrue(
				idCategory, TypePost.POST, paging);
		return returnPage(page, paging);
	}

	private PageImpl<PostPostDto> returnPage(Page<Post> page,Pageable paging){
		return new PageImpl<PostPostDto>(
				page.getContent().stream().map(post-> new PostPostDto(
						post.getIdPost(),
						post.getName(), post.getContent(), post.getAccount().getUserName(),
						post.getCategory().getIdCategory(), post.getPost()!=null ? post.getPost().getIdPost() : null,
								post.getTypePost(), post.getImage(), post.getDateCreated(), post.getDateUpdated(),
								post.getStatus(),post.getCategory().getName())).collect(Collectors.toList()),
				paging, page.getTotalElements()
				);
	}

	@Override
	public PostPostDto findPostByIdNotMenu(int id) {
		return postMapper.postPostDtoToPost(postRepository.findByIdPostAndTypePostAndStatusIsTrue(id, TypePost.POST));
	}

	@Override
	public PageImpl<PostPostDto> listPaginationWithIdCategoryAndStatusTrueAndNameStartWith(Integer pageNo,
			Integer pageSize, int idCategory, String name) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Post> page =  postRepository.findByCategoryIdCategoryAndStatusIsTrueAndCategoryTypeCategoryAndNameStartingWith(
				idCategory,TypeCategory.NOMENCLATURE, name, paging);
		return returnPage(page, paging);
	}

	@Override
	public PostPostDto detailPostGlossary(int idPostGlossary) {
		return postMapper.postPostDtoToPost(postRepository.findByIdPostAndStatusIsTrueAndCategoryTypeCategoryAndCategoryStatusIsTrue(idPostGlossary, TypeCategory.NOMENCLATURE));
	}

	@Override
	public PostPostDto findPostByIdTypeMenuAndCategoryIsCourse(Integer idPost) {
		Post postFind = postRepository.findByIdPostAndStatusIsTrueAndTypePostAndCategoryTypeCategoryAndCategoryStatusIsTrue(idPost, TypePost.MENU, TypeCategory.COURSE);
		return postMapper.postPostDtoToPost(postFind);
	}

	@Override
	public PageImpl<PostPostDto> listPaginationByUserName(Integer pageNo, Integer pageSize, String username) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Post> page =  postRepository.findByAccountUserName(username, paging);
		return returnPage(page, paging);
	}

	@Override
	public Post findPostByIdAndReturnPost(int id) {
		Optional<Post> postFind = postRepository.findById(id);
		return postFind.isPresent() ? postFind.get() : null;
	}

	@Override
	public List<PostPostDto> listAllPostTypeMenuAndCategoryIsCourseAndCategoryStatusIsTrue() {
		// TODO Auto-generated method stub
		return postMapper.listPostToListPostDto(postRepository.findByTypePostAndCategoryTypeCategoryAndCategoryStatusIsTrue(TypePost.MENU,TypeCategory.COURSE));
	}

	@Override
	public PageImpl<PostPostDto> findAll(Predicate predicate, Pageable pageable) {
		Page<Post> page = postRepository.findAll(predicate, pageable);
		return returnPage(page, pageable);
	}

	@Override
	public PageImpl<PostPostDto> findName(String name, Pageable pageable) {
		return returnPage(
				postRepository.findByNameContainingAndStatusIsTrue(name,pageable),
				pageable);
	}
}
