package com.xinra.reviewcommunity.android;


import android.app.Application;

import com.xinra.reviewcommunity.shared.dto.DtoFactory;
import com.xinra.reviewcommunity.shared.dto.InstantiatingDtoFactory;

public class ReviewCommunityApplication extends Application {

  private AppState state;
  private Api api;
  private DtoFactory dtoFactory;

  public ReviewCommunityApplication() {

  }

  @Override
  public void onCreate() {
    super.onCreate();
    state = new AppState();
    api = new Api(state);
    dtoFactory = new InstantiatingDtoFactory();
  }

  public AppState getState() {
    return state;
  }

  public Api getApi() {
    return api;
  }

  public DtoFactory getDtoFactory() {
    return dtoFactory;
  }

}
