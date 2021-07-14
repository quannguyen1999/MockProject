package com.main.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.main.entity.ComboCourse;
import com.main.mapstruct.ComboCourseMapper;
import com.main.mapstruct.CourseMapper;
import com.main.model.ComboCourseGetDto;
import com.main.model.CourseComboPutDto;
import com.main.repository.ComboCourseRepository;
import com.main.service.ComboCourseService;
import com.querydsl.core.types.Predicate;

@Service
public class ComboCourseImpl implements ComboCourseService{
	
	@Autowired
	private ComboCourseRepository comboCourseRepository;
	
	@Autowired
	private ComboCourseMapper comboCourseMapper;
	
	@Autowired
	private CourseMapper courseMapper;
	
	@Override
	public PageImpl<ComboCourseGetDto> findAll(Predicate predicate, Pageable pageable) {
		Page<ComboCourse> page = comboCourseRepository.findAll(predicate, pageable);
		return returnPage(page, pageable);
	}

	private PageImpl<ComboCourseGetDto> returnPage(Page<ComboCourse> page,Pageable paging){
		return new PageImpl<ComboCourseGetDto>(
				page.getContent().stream().map(comboCourse-> new ComboCourseGetDto(
						comboCourse.getId(),
						comboCourse.getNameCourse(),
						courseMapper.listCourseToListCourseGetDto(comboCourse.getListComboCourse()),
								comboCourse.getTotalPrice())).collect(Collectors.toList()),
				paging, page.getTotalElements());
	}

	@Override
	public List<ComboCourseGetDto> listComboCoursesByIdPostCourse(String idPost) {
		return comboCourseMapper.listComboCourseToComboCourseGetDto(comboCourseRepository.findByListComboCoursePostIdPost(Integer.parseInt(idPost)));
	}

	@Override
	public ComboCourseGetDto findById(String id) {
		try {
			return comboCourseMapper.ComboCourseToComboCourseGetDto(comboCourseRepository.findById(id).get());
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	@Override
	public ComboCourseGetDto updateComboCourse(CourseComboPutDto courseComboPutDto) {
		// TODO Auto-generated method stub
		return null;
	}
}
