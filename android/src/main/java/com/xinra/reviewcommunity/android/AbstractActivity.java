package com.xinra.reviewcommunity.android;

import android.support.v7.app.AppCompatActivity;

/**
 * Superclass of all activities.
 */
public abstract class AbstractActivity extends AppCompatActivity {

  protected AppState getState() {
    return ((ReviewCommunityApplication) getApplication()).getState();
  }

}
