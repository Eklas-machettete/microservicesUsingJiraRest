package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

	@LocalServerPort
    private int localPort;

    @Autowired
    private RestTemplate testRestTemplate;

    @Test
    public void testShouldReturnMessage() {
    assertThat(testRestTemplate.getForObject("http://localhost:8080/response/getCommentsByUserName/issueKey=MFP-27/userName=Md.Eklasur.Rahman", String.class)
    .contains("Md.Eklasur.Rahman"));
}
	
}
