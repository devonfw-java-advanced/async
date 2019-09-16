package com.devonfw.java.training.async.spring.greeter.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class GreeterService {
    private RestTemplate personRestTemplate = new RestTemplate();

    public String greet(String name) {
        return this.greetPerson(name);
    }

    @Async
    public CompletableFuture<String> greetAsync(String name) {
        String answer = this.greetPerson(name);
        return CompletableFuture.completedFuture(answer);
    }

    private String greetPerson(String name) {
        return personRestTemplate.getForObject("http://localhost:8080/hello/{name}", String.class, name);
    }
}
