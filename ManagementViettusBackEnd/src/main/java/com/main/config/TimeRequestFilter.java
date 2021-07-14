package com.main.config;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.model.CustomResponse;
import io.github.bucket4j.local.LocalBucket;

//get time each request
@Component
@WebFilter("/*")
public class TimeRequestFilter implements Filter {

	//use rate limiting
	@Autowired
	private LocalBucket localBucket;

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeRequestFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		//to count start time
		Instant start = Instant.now();
		try {
			//if rate limitting is not allow ,send error 429
			if (localBucket.tryConsume(1) == false) {
				throw new Exception("too many request");
			}else {
				chain.doFilter(req, resp);
			}
		} catch (Exception e) {
			//send error 429
			HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
			CustomResponse customResponse = new CustomResponse(null, "too many request", null);
			httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			httpServletResponse.setContentType("application/json");
			httpServletResponse.getWriter().write(convertObjectToJson(customResponse));
		} finally {
			//count time for each request
			Instant finish = Instant.now();
			long time = Duration.between(start, finish).toMillis();
			LOGGER.info("Time response:"+time+ " | uri:"+((HttpServletRequest) req).getRequestURI());
		}
	}
	
	//convert value to Json
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

	@Override
	public void destroy() {
		// empty
	}
}
