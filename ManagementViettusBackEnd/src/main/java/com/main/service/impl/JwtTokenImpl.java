package com.main.service.impl;

import static java.lang.String.format;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.main.config.JwtConfig;
import com.main.entity.Account;
import com.main.service.JwtTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class JwtTokenImpl implements JwtTokenService{

	private final JwtConfig jwtConfig; 
	
	private static final String AUTHORITIES_CLAIM = "authorities";
	
	@Override
	public String generateAccessToken(Account account) {
		return jwtConfig.getTokenPrefix()+Jwts.builder()
		.setSubject(format("%s", account.getUserName()))
		.claim(AUTHORITIES_CLAIM, account.getTypeAccount().toString())
		.setIssuedAt(new Date())
		.setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(Integer.parseInt(jwtConfig.getTokenExpirationAfterDays())))) // 1 week
		.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey())
		
		.compact();
	}

	@Override
	public String getUserName(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKey())
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject().split(",")[0];
	}

	@Override
	public Date getExpirationDate(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKey())
				.parseClaimsJws(token)
				.getBody();
		return claims.getExpiration();
	}

	@Override
	public String validate(String token) {
		try {
            Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token);
//            return true;
        } catch (SignatureException ex) {
            return "Invalid JWT signature - "+ ex.getMessage();
//            logger.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            return "Invalid JWT token - "+ ex.getMessage();
//            logger.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            return "Expired JWT token - "+ ex.getMessage();
//            logger.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            return "Unsupported JWT token - "+ ex.getMessage();
//            logger.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            return "JWT claims string is empty - "+ ex.getMessage();
//            logger.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return null;

	}
	
	@Override
	public String getTypeAccount(String token) {
		token = token.split(" ")[1].trim();
		return Jwts.parser()
				.setSigningKey(jwtConfig.getSecretKey())
				.parseClaimsJws(token)
					.getBody().get(AUTHORITIES_CLAIM).toString();
	}

	@Override
	public String hashPassword(String password) {
		return "none";
	}

}
