package com.tayyab.users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

	@Value("${tokenSecret}")
	private String tokenSecret;
	
	public String getTokenSecret(){
		return tokenSecret;
	}
}
