package com.devonfw.java.training.async.spring.person;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonRestController {
    @GetMapping("hello/{personName}")
    public String hello(@PathVariable String personName) throws InterruptedException {
        Thread.sleep(4000);
        return personName + ": Hello to you, too";
    }
}
