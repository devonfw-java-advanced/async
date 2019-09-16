package com.devonfw.java.training.async.spring.greeter.rest;

import com.devonfw.java.training.async.spring.greeter.service.GreeterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("greetings")
public class GreeterRestController {
    private static final String[] PERSONS_TO_GREET = new String[]{"Andrew", "John"};
    private final GreeterService greeterService;

    public GreeterRestController(GreeterService greeterService) {
        this.greeterService = greeterService;
    }

    @GetMapping("sync")
    public String greet() {
        final long startTime = System.currentTimeMillis();
        String answers = Stream.of(PERSONS_TO_GREET)
                .map(greeterService::greet)
                .collect(Collectors.joining(", "));
        final long endTime = System.currentTimeMillis();
        return createResponseMessage(answers, startTime, endTime);
    }

    @GetMapping("async/blocking")
    public String greetAsyncBlocking() {
        final long startTime = System.currentTimeMillis();
        String answers = greetPersonsAsync(); // run in the thread form the servlet thread pool
        final long endTime = System.currentTimeMillis();
        return createResponseMessage(answers, startTime, endTime);
    }

    @GetMapping("async/non-blocking")
    public Callable<String> greetAsyncNonBlocking() {
        final long startTime = System.currentTimeMillis();
        return () -> { // run in another thread out of the servlet thread pool
            String answers = greetPersonsAsync();
            final long endTime = System.currentTimeMillis();
            return createResponseMessage(answers, startTime, endTime);
        };
    }

    private String greetPersonsAsync() {
        CompletableFuture<String>[] answerFutures = (CompletableFuture<String>[]) Stream.of(PERSONS_TO_GREET)
                .map(greeterService::greetAsync)
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(answerFutures).join(); // blocks the current thread!

        return Arrays.stream(answerFutures)
                .map(answerFuture -> callCatchingErrors(answerFuture::get, "Error :("))
                .collect(Collectors.joining(", "));
    }

    private <T> T callCatchingErrors(Callable<T> callable, T valueOnError) {
        try {
            return callable.call();
        } catch (Exception e) {
            return valueOnError;
        }
    }

    private String createResponseMessage(String answers, long startTime, long endTime) {
        return answers + "; Collecting answers took " + (endTime - startTime) + " ms";
    }
}
