package com.main.security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.main.controller.AccountController;
import com.main.controller.CategoryController;
import com.main.controller.ComboCourseController;
import com.main.controller.ContactController;
import com.main.controller.CourseController;
import com.main.controller.PostController;
import com.main.controller.RegistrationController;
import com.main.controller.ReportController;
import com.main.entity.TypeAccount;

import lombok.AllArgsConstructor;


/*
 * @AllArgsContrucctor: auto generate create constructor by lombok
 * @Configuration: tell spring this is config (xml)
 * @EnableAutoConfiguration
 * @EnableWebSecurity, @EnableGlobalMethodSecurity: for spring security
 * @Order: set order when use multiple spring security (login or api)
 * */
@AllArgsConstructor
@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1000000)     
public class SecurityConfig{
	//default to set token in header
	static final String DEFAULT_ACCESS_TOKEN_HEADER = "AccessToken";

	//this class will resolve for api
	//allow authenticator to class ApiWebSecurityConfigurationAdapter first
	@Configuration
	@Order(10000000)     
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		//hash password
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		//custom when login failed
		@Bean
		public AuthenticationFailureHandler authenticationFailureHandler() {
			return new CustomAuthenticationFailureHandler();
		}

		//custom when login success 
		@Bean
		public AuthenticationSuccessHandler authenticationSuccessHandler() {
			return new CustomAuthenticationSuccessHandler();
		}

		//to get roll users
		@SuppressWarnings("unused")
		private static Collection<? extends GrantedAuthority> getAuthorities(TypeAccount typeAccount) {
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_"+typeAccount.toString()));
			return authorities;
		}

		//allow access public swagger-ui (css,)
		@Override
		public void configure(WebSecurity web) throws Exception {
//			web.ignoring().antMatchers("/v2/api-docs",
//					"/configuration/ui",
//					"/swagger-resources/**",
//					"/configuration/security",
//					"/swagger-ui.html",
//					"/webjars/**");
		}

		//to check token and authenticator
		@Autowired
		private JwtTokenFilter jwtTokenFilter;  
		
		//cấu hình bảo mật cho request
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.cors().and().csrf().disable();
			//	        		http = http
			//	        				.sessionManagement()
			//	        				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			//	        				.and();

			http.authorizeRequests()
			//
			.antMatchers(AccountController.API_ACCOUNT+"/signup").permitAll()
			.antMatchers(AccountController.API_ACCOUNT+"/findAll").permitAll()
			.antMatchers(AccountController.API_ACCOUNT+"/signin").permitAll()
			.antMatchers(AccountController.API_ACCOUNT+"/getTypeAccount").permitAll()
			.antMatchers(AccountController.API_ACCOUNT+"/getTypeAccount").permitAll()
			.antMatchers(AccountController.API_ACCOUNT+"/**").hasAnyRole(TypeAccount.ADMIN.toString())
			//
			.antMatchers(CategoryController.API_CATEGORY+"/list").permitAll()
			.antMatchers(CategoryController.API_CATEGORY+"/detail").permitAll()
			.antMatchers(CategoryController.API_CATEGORY+"/listHeader").permitAll()
			.antMatchers(CategoryController.API_CATEGORY+"/listHomePage").permitAll()
			.antMatchers(CategoryController.API_CATEGORY+"/**").hasAnyRole(TypeAccount.ADMIN.toString())
			//
			.antMatchers(PostController.API_POST+"/findAll").permitAll()
			.antMatchers(PostController.API_POST+"/listPaginationNoAuth").permitAll()
			.antMatchers(PostController.API_POST+"/detail").permitAll()
			.antMatchers(PostController.API_POST+"/listByIdCategoryAndTypePost").permitAll()
			.antMatchers(PostController.API_POST+"/findCategoryGlossaryWithNameStarting").permitAll()
			.antMatchers(PostController.API_POST+"/detailGlossary").permitAll()
			.antMatchers(PostController.API_POST+"/findName").permitAll()
			.antMatchers(PostController.API_POST+"/**").hasAnyRole(TypeAccount.ADMIN.toString(),TypeAccount.COLLABORATOR.toString())
			//
			.antMatchers(CourseController.API_COURSE+"/listByIdCategory").permitAll()
			.antMatchers(CourseController.API_COURSE+"/listCourseByIdPost").permitAll()
			.antMatchers(CourseController.API_COURSE+"/find").permitAll()
			.antMatchers(CourseController.API_COURSE+"/register").permitAll()
			.antMatchers(CourseController.API_COURSE+"/**").hasAnyRole(TypeAccount.ADMIN.toString())
			//
			.antMatchers(ContactController.API_CONTACT + "/create").permitAll()
			.antMatchers(ContactController.API_CONTACT + "/**").hasAnyRole(TypeAccount.ADMIN.toString())
			//
			.antMatchers(RegistrationController.API_REGISTRATION + "/listByIdCourse").permitAll()
			.antMatchers(RegistrationController.API_REGISTRATION + "/**").hasAnyRole(TypeAccount.ADMIN.toString())
			//
			.antMatchers(ComboCourseController.API_COMBO_COURSE+"/ListAllByIdPost").permitAll()
			.antMatchers(ComboCourseController.API_COMBO_COURSE+"/findById").permitAll()
			.antMatchers(ComboCourseController.API_COMBO_COURSE+"/**").hasAnyRole(TypeAccount.ADMIN.toString())
			//
			.antMatchers(ReportController.API_REPORT + "/**").hasAnyRole(TypeAccount.ADMIN.toString())
		
			//
			.antMatchers("/hello","topic/greetings","/topic/websocket/topic/reports",
					"/topic","/app","/gs-guide-websocket/**","/websocket/**").permitAll()
			
			.and().formLogin();
			
			//add filter before go to check
			http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		}

		//cấu hình cors
		@Bean
		public CorsConfigurationSource corsConfigurationSource() {
			final CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedMethods(ImmutableList.of("GET", "POST", "PUT", "DELETE"));
			configuration.setAllowCredentials(true);
			configuration.setAllowedOriginPatterns(ImmutableList.of("*"));
			configuration.setAllowedOriginPatterns(ImmutableList.of("*"));
			configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type","AccessToken"));
			final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}
	}

	@Configuration  
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Value("${spring.datasource.username}")
		private String userName;
		
		@Value("${spring.datasource.password}")
		private String password;
		
		//hash password
		@Bean
		public BCryptPasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		//config for form login to access swagger-ui
		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			auth
			.inMemoryAuthentication()
			.withUser(userName)
			.password(passwordEncoder().encode(password))
			.roles("ADMIN");
		}
		
		//config check swagger-ui
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			  http
			  .antMatcher("/swagger-ui/**")
              .authorizeRequests(authorize -> authorize
                  .anyRequest().authenticated()
              )
              .formLogin().loginProcessingUrl("/login")
              .successForwardUrl("/swagger-ui/#/");
		}
	}
}