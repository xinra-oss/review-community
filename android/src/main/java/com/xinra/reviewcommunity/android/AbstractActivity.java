package com.xinra.reviewcommunity.android;

import android.support.v7.app.AppCompatActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Superclass of all activities.
 */
public abstract class AbstractActivity extends AppCompatActivity {

  /**
   * Add store subscriptions to this so that they are disposed of automatically when the activity is
   * destroyed.
   */
  protected final CompositeDisposable subscriptions = new CompositeDisposable();

  protected AppState getState() {
    return ((ReviewCommunityApplication) getApplication()).getState();
  }

  protected Api getApi() {
    return ((ReviewCommunityApplication) getApplication()).getApi();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    subscriptions.dispose();
  }
}
