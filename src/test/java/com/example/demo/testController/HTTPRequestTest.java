package com.example.demo.testController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HTTPRequestTest {

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