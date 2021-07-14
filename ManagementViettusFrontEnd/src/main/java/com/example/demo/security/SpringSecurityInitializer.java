package com.example.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/*
 * 
 * “SpringSecurityInitializer” is used to register the DelegatingFilterProxy 
 * to use the springSecurityFilterChain. It avoids writing Filters configuration in web.xml file
 * */
@Configuration
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer{
}