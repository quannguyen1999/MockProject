package com.main.mapstruct;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.main.entity.Post;
import com.main.model.PostPostDto;

@Mapper(
		componentModel = "spring"
		)
public interface PostMapper {
	@Mappings({
		@Mapping(target = "idCategory", source = "category.idCategory"),
		@Mapping(target = "idPostSelf", source = "post.idPost"),
		@Mapping(target = "userName", source = "account.userName"),
		@Mapping(target = "nameCategory", source = "category.name"),
	})
	PostPostDto postPostDtoToPost(Post post);

	@Mappings({
		@Mapping(target = "category.idCategory", source = "idCategory"),
		@Mapping(target = "post.idPost", source = "idPostSelf"),
		@Mapping(target = "account.userName", source = "userName"),
		@Mapping(target = "listPostSelf", ignore = true)
	})
	Post postToPostPostDto(PostPostDto postPostDto);
	
	@Mappings({
		@Mapping(target = "idCategory", source = "category.idCategory"),
		@Mapping(target = "idPostSelf", source = "post.idPost"),
		@Mapping(target = "userName", source = "account.userName")
	})
	List<PostPostDto> listPostToListPostDto(List<Post> listPosts);
}
