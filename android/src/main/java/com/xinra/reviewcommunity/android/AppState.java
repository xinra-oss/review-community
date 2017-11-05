package com.xinra.reviewcommunity.android;

import io.reactivex.subjects.BehaviorSubject;

/**
 * This is the global application state. Everything that is not local UI state is managed here.
 * {@link BehaviorSubject}s are used so that the current state is pushed to new subscribers. Make
 * sure to properly dispose of subscriptions as to not leak them.
 */
public class AppState {

  public final BehaviorSubject<String> csrfToken = BehaviorSubject.create();

}
