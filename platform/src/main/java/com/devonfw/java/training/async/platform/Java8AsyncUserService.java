package com.devonfw.java.training.async.platform;

import java.util.concurrent.*;

class Java8AsyncUserService {
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    CompletableFuture<User> getUser(long id) {
        return CompletableFuture.supplyAsync(() -> {
            final User johnExample = new User(id, "John Example");
            System.out.println("About to return " + johnExample + " in Thread " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            return johnExample;
        }, threadPool);
    }

    CompletableFuture<User> getUser2(long id) {
        return CompletableFuture.supplyAsync(
                () -> new User(id, "John Example"),
                threadPool);
    }

    void destroy() throws InterruptedException {
        threadPool.shutdown();
        threadPool.awaitTermination(2, TimeUnit.SECONDS);
    }
}
