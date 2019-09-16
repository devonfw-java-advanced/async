package com.devonfw.java.training.async.platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class Java5AsyncUserService {
    private ExecutorService threadPool = Executors.newCachedThreadPool();

//    Future<User> getUser(long id) {
//        return threadPool.submit(() -> {
//            final User johnExample = new User(id, "John Example");
//            System.out.println("About to return " + johnExample + " in Thread " + Thread.currentThread().getName());
//            Thread.sleep(1000);
//            return johnExample;
//        });
//    }

    Future<User> getUser(long id) {
        return threadPool.submit(() -> new User(id, "John Example"));
    }

    void destroy() throws InterruptedException {
        threadPool.shutdown();
        threadPool.awaitTermination(2, TimeUnit.SECONDS);
    }
}
