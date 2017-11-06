package com.xinra.reviewcommunity.android;


import android.app.Application;

public class ReviewCommunityApplication extends Application {

  private AppState state;
  private Api api;

  public ReviewCommunityApplication() {

  }

  @Override
  public void onCreate() {
    super.onCreate();
    state = new AppState();
    api = new Api(state);
  }

  public AppState getState() {
    return state;
  }

  public Api getApi() {
    return api;
  }

}
