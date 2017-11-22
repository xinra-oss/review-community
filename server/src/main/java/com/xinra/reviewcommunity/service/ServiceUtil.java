package com.xinra.reviewcommunity.service;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ServiceUtil {
  
  private ServiceUtil() {}

  /**
   * Executes code after a @{@link Transactional} method successfully commits.
   * Call this before doing anything that could cause a rollback.
   */
  public static void afterCommit(@NonNull Runnable runnable) {
    // adapted from https://stackoverflow.com/a/43322119/5519485
    TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            runnable.run();
          }
        });
  }
  
}
