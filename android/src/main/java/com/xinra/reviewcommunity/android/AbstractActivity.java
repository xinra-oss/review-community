package com.xinra.reviewcommunity.android;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Optional;

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
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    subscriptions.add(getState().initialized.subscribe(initialized -> {
      if (initialized) {
        onInitialized();
      } else {
        init();
      }
    }));
  }

  private void init() {
    getApi().getInit().subscribe(initDto -> {
      getState().csrfToken = initDto.getCsrfToken();
      getState().authenticatedUser.onNext(Optional.ofNullable(initDto.getAuthenticatedUser()));
      getState().permissions.onNext(initDto.getPermissions());
      getState().availableMarkets.onNext(initDto.getMarkets());
      getState().categoryTree.onNext(initDto.getCategoryTree());
      getState().initialized.onNext(true); // this will trigger onInitialized()
    }, this::handleError);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    subscriptions.dispose();
  }

  protected void handleError(Throwable error) {
    new AlertDialog.Builder(this)
        .setTitle(error.getClass().getSimpleName())
        .setMessage(error.getMessage())
        .show();
  }

  /**
   * This is called once the activity has been created and the state is initialized. Usually you
   * should NOT dispatch API calls until this is called.
   */
  protected void onInitialized() {}
}
