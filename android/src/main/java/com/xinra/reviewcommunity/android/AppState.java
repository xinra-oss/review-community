package com.xinra.reviewcommunity.android;

import io.reactivex.subjects.BehaviorSubject;

public class AppState {

  public final BehaviorSubject<String> csrfToken = BehaviorSubject.create();

}
