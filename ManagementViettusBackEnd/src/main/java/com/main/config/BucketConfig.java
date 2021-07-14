package com.main.config;

import org.springframework.context.annotation.Configuration;

import java.time.Duration;

import org.springframework.context.annotation.Bean;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucket;

//avoid attack ddns
@Configuration
public class BucketConfig {
	
	//rate limiting
	@Bean
	public LocalBucket localBucket() {
		//value 1: maximun for each request 
		//value 2 + value 2:token with per minute
		Bandwidth limit = Bandwidth.classic(50, Refill.greedy(50, Duration.ofSeconds(10)));
		return Bucket4j.builder()
				.addLimit(limit)
				.build();
	}
}
