package com.example.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
	@Value("${jira.userName}")
	String userName;
	@Value("${jira.apiKey}")
	String jiraApiKey;
	@Bean
	public RestTemplate restTemplate( RestTemplateBuilder restTemplateBuilder)
	{

		//restTemplateBuilder.rootUri("https://eklas.atlassian.net/rest/api/3").basicAuthentication("eklasur.rahman@northsouth.edu", "mE3gd4uvH81thng7TeJOC2B2").build();
	    return restTemplateBuilder.basicAuthentication(userName, jiraApiKey).build();

	}

}
