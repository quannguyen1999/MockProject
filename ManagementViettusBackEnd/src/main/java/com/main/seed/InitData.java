package com.main.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.main.entity.TypeAccount;
import com.main.entity.TypeCategory;
import com.main.model.AccountPostDto;
import com.main.model.CategoryPostDto;
import com.main.service.AccountService;
import com.main.service.CategoryService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class InitData implements CommandLineRunner{

	private final AccountService accountService;
	
	private final CategoryService categoryService;
	
	static AccountPostDto accountAdmin = new AccountPostDto("admin", "Khanhhoa123@",TypeAccount.ADMIN);
	static AccountPostDto accountUser = new AccountPostDto("quannda1", "Khanhhoa123@",TypeAccount.COLLABORATOR);
	
	static CategoryPostDto categoryPostDto0 = new CategoryPostDto(1,"Lập trình", "Chuyên mục tổng hợp các bài viết về interview, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.",TypeCategory.PROGRAMMING);
	static CategoryPostDto categoryPostDto1 = new CategoryPostDto(2,"Interview", "Chuyên mục tổng hợp các bài viết về interview, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto2 = new CategoryPostDto(3,"Tản Mạn", "Chuyên mục tổng hợp các bài viết về Tản Mạn, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto3 = new CategoryPostDto(4,"Khóa học", "Chuyên mục tổng hợp các bài viết về Khóa học, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.COURSE);
	static CategoryPostDto categoryPostDto4 = new CategoryPostDto(5,"Web/Hosting", "Chuyên mục tổng hợp các bài viết về Web/Hosting, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto5 = new CategoryPostDto(6,"Tin học", "Chuyên mục tổng hợp các bài viết về Tin học, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto6 = new CategoryPostDto(7,"Công nghệ", "Chuyên mục tổng hợp các bài viết về Công nghệ, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto7 = new CategoryPostDto(8,"Interview", "Chuyên mục tổng hợp các bài viết về Interview, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto8 = new CategoryPostDto(9,"Download", "Chuyên mục tổng hợp các bài viết về Download, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto9 = new CategoryPostDto(10,"Môn học", "Chuyên mục tổng hợp các bài viết về Môn học, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.POST);
	static CategoryPostDto categoryPostDto10 = new CategoryPostDto(11,"Thuật ngữ", "Chuyên mục tổng hợp các bài viết về Môn học, bạn có thể tham khảo và sử dụng tài liệu interview hoàn toàn miễn phí.", TypeCategory.NOMENCLATURE);
	
	@Override
	public void run(String... args) throws Exception {
//		addAccount();
//		addCategory();
	}
	
	@SuppressWarnings("unused")
	private void addAccount() {
		if(accountService.findByUserName(accountAdmin.getUserName()) == null) {
			accountService.insert(accountAdmin);
		}
		
		if(accountService.findByUserName(accountUser.getUserName()) == null) {
			accountService.insert(accountUser);
		}
	}
	
	@SuppressWarnings("unused")
	private void addCategory() {
		if(categoryService.findCategoryById(categoryPostDto1.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto1);
		}
		
		if(categoryService.findCategoryById(categoryPostDto2.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto2);
		}
		
		if(categoryService.findCategoryById(categoryPostDto3.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto3);
		}
		
		if(categoryService.findCategoryById(categoryPostDto4.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto4);
		}
		
		if(categoryService.findCategoryById(categoryPostDto5.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto5);
		}
		
		if(categoryService.findCategoryById(categoryPostDto6.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto6);
		}
		
		if(categoryService.findCategoryById(categoryPostDto7.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto7);
		}
		
		if(categoryService.findCategoryById(categoryPostDto8.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto8);
		}
		
		if(categoryService.findCategoryById(categoryPostDto9.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto9);
		}
		
		if(categoryService.findCategoryById(categoryPostDto10.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto10);
		}
		
		if(categoryService.findCategoryById(categoryPostDto0.getIdCategory()) == null) {
			categoryService.saveCategory(categoryPostDto0);
		}
	}
}
