package com.main.security;

import static org.springframework.util.StringUtils.isEmpty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.main.entity.Account;
import com.main.entity.TypeAccount;
import com.main.repository.AccountRepository;
import com.main.service.JwtTokenService;

import lombok.AllArgsConstructor;

//check header before request
@AllArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter{

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private AccountRepository accountRepository;

	@SuppressWarnings("deprecation")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// Get authorization header and validate
		final String header = request.getHeader(SecurityConfig.DEFAULT_ACCESS_TOKEN_HEADER);
		if (isEmpty(header) || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		// Get jwt token and validate
		final String token = header.split(" ")[1].trim();
		String resultJwtTokenUtil = jwtTokenService.validate(token);
		if (resultJwtTokenUtil!=null) {
			request.setAttribute("cause",resultJwtTokenUtil);
			chain.doFilter(request, response);
			return;
		}

		// Get user identity and set it on the spring security context
		Optional<Account> accOptional = accountRepository.findById(jwtTokenService.getUserName(token));

		UserDetails userDetails = new User(accOptional.get().getUserName(),
				accOptional.get().getPassword(),
				getAuthorities(accOptional.get().getTypeAccount()));
		UsernamePasswordAuthenticationToken
		authentication = new UsernamePasswordAuthenticationToken(
				userDetails, null,
				userDetails == null ?
						new ArrayList<>() : userDetails.getAuthorities()
				);

		authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(request, response);
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(TypeAccount typeAccount) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_"+typeAccount.toString()));
		return authorities;
	}



}
