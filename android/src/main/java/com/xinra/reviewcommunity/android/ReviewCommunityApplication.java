package com.xinra.reviewcommunity.android;


import android.app.Application;

public class ReviewCommunityApplication extends Application {

  private final AppState state = new AppState();

  public AppState getState() {
    return state;
  }

}
