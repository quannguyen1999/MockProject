package com.example.demo.security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import com.example.demo.model.CustomResponse;
import com.example.demo.model.ResponseToken;
import com.example.demo.model.TypeAccount;
import com.example.demo.service.AccountService;
import com.example.demo.service.ConvertService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.jsonwebtoken.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;


//to check authentication and verify user
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	//hash and check passwod
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AccountService accountService;
	
	//convert object
	@Autowired
	private ConvertService convertService;

	//add bean bscrypt
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//this will occur when in class RequestBodyReaderAuthenticationFilter is success
		auth.userDetailsService(userName -> {
			CustomResponse customResponse =  accountService.getResponseToken(userName);
			ResponseToken responseToken =convertService.convertToObject(customResponse, new ResponseToken());
			User user = new User(userName, 
					passwordEncoder.encode("Demo123@"),
					true,true,true,true,  getAuthorities(responseToken.getTypeAccount().toString()));
			return user;
		});
	}

	//to get role
	private static Collection<? extends GrantedAuthority> getAuthorities(String typeAccount) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_"+typeAccount.toString()));
		return authorities;

	}

	//custom secure login
	@Bean
	public RequestBodyReaderAuthenticationFilter authenticationFilter() throws Exception {
		RequestBodyReaderAuthenticationFilter filter = new RequestBodyReaderAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManagerBean());
		filter.setAuthenticationSuccessHandler(this::loginSuccessHandler);
		filter.setAuthenticationFailureHandler(this::loginFailureHandler);
		return filter;
	}

	//if success login then redirect to page manage
	private void loginSuccessHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, java.io.IOException {
		HttpSession session = request.getSession();
		session.setAttribute("messageSuccess","Đăng nhập thành công");
		response.sendRedirect("/demo/welcome");
	}


	//allow access file static, css, img,...
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
		.antMatchers("/static/**","/img/**");
	}

	// page allow and not allow are define in here
	//config security
	protected void configure(HttpSecurity http) throws Exception {
		 // Set unauthorized requests exception handler
		http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/*.css","/*.PNG").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/error").permitAll()
		
		.antMatchers("/").permitAll()
		
		.antMatchers("/account/**").hasAuthority("ROLE_"+TypeAccount.ADMIN.toString())
		
		.antMatchers("/category/detail").permitAll()
		.antMatchers("/category/detail/glossary").permitAll()
		.antMatchers("/category/**").hasAuthority("ROLE_"+TypeAccount.ADMIN.toString())
		
		
		.antMatchers("/post/detail").permitAll()
		.antMatchers("/post/search").permitAll()
		.antMatchers("/post/detailGlossary").permitAll()
		.antMatchers("/post/**").hasAnyAuthority("ROLE_"+TypeAccount.ADMIN.toString(),"ROLE_"+TypeAccount.COLLABORATOR.toString())
		
		.antMatchers("/course/detail").permitAll()
		.antMatchers("/course/**").hasAuthority("ROLE_"+TypeAccount.ADMIN.toString())
		
		.antMatchers("/registration/saveRegistration").permitAll()
		.antMatchers("/registration/listCustomerBuyCourse").permitAll()
		.antMatchers("/registration/**").hasAuthority("ROLE_"+TypeAccount.ADMIN.toString())
		
		.antMatchers("/contact/create").permitAll()
		.antMatchers("/contact/saveContact").permitAll()
		.antMatchers("/contact/listEmail").hasAuthority("ROLE_"+TypeAccount.ADMIN.toString())
		
		.antMatchers("/contact/**").hasAuthority("ROLE_"+TypeAccount.ADMIN.toString())
		
		.antMatchers("/comboCourse/createCombo").permitAll()
		.antMatchers("/comboCourse/verifyCombo").permitAll()
		
		.antMatchers("/testSocket/**").permitAll()
		.antMatchers("/webjars/**").permitAll()
		.anyRequest().authenticated()
		
		.and()
		.addFilterBefore(
				authenticationFilter(),
				UsernamePasswordAuthenticationFilter.class)
		.logout()
		.logoutUrl("/logout")
		//custom page return when logout
		.logoutSuccessHandler(this::logoutSuccessHandler)

		.and()
		//page access denied default
        .exceptionHandling().accessDeniedPage("/accessDenied");
	}

	//when logout, session will delete and redirect to page login
	private void logoutSuccessHandler(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, JsonGenerationException, JsonMappingException, java.io.IOException {
		
		HttpSession session= request.getSession(false);
		SecurityContextHolder.clearContext();
		if(session != null) {
			session.invalidate();
		}
		response.sendRedirect("/demo/login");
	}

	//if login failed return login
	private void loginFailureHandler(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authentication) throws java.io.IOException {
		response.sendRedirect("/demo/login");
	}

}


