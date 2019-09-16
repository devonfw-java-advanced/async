package com.devonfw.java.training.async.platform;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class JavaAsyncSupport {
    private void inJava5() throws ExecutionException, InterruptedException {
        System.out.println("Java 5");
        Java5AsyncUserService userService = new Java5AsyncUserService();

        Future<User> userFuture = userService.getUser(1234);
        User user = userFuture.get(); // blocks the main thread so that the user is available in the next line
        System.out.println("Got: " + user + " in Thread " + Thread.currentThread().getName());

        userService.destroy(); // shutdown the thread pool
    }

    private void inJava8() throws InterruptedException {
        System.out.println("Java 8:");
        Java8AsyncUserService userService = new Java8AsyncUserService();

        CompletableFuture<User> userCompletableFuture = userService.getUser(1234);
        AtomicReference<User> user = new AtomicReference<>(); // for passing user between threads
        userCompletableFuture.thenAccept(user::set); // does not block the current thread, thus...
        System.out.println("Got: " + user.get() // ... user is null here
                + " in Thread " + Thread.currentThread().getName());
        userService.destroy(); // shutdown the thread pool and flush all submitted tasks
        System.out.println("Got: " + user.get() + " in Thread " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final JavaAsyncSupport javaAsyncSupport = new JavaAsyncSupport();
        javaAsyncSupport.inJava5();
        javaAsyncSupport.inJava8();
    }
}