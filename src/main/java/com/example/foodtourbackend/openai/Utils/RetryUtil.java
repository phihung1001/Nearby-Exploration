package com.example.foodtourbackend.openai.Utils;

import java.util.concurrent.Callable;

public class RetryUtil {

  public static <T> T retryWithBackoff(Callable<T> action, int maxRetries, long delay) throws Exception {
    int retryCount = 0;
    while (retryCount < maxRetries) {
      try {
        return action.call();
      } catch (Exception e) {
        Thread.sleep(delay * (long) Math.pow(2, retryCount)); // Exponential Backoff
        retryCount++;
      }
    }
    throw new RuntimeException("Max retry attempts reached");
  }
}