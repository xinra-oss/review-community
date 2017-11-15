package com.xinra.reviewcommunity.android;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;

import com.google.common.base.Optional;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;

import java.util.Collections;
import java.util.Set;

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

  /**
   * Current permissions of the user. Updated automatically (get notified by overriding
   * {@link #onPermissionsUpdated()}).
   */
  protected Set<Permission> permissions = Collections.emptySet();

  protected AppState getState() {
    return ((ReviewCommunityApplication) getApplication()).getState();
  }

  protected Api getApi() {
    return ((ReviewCommunityApplication) getApplication()).getApi();
  }

  protected DtoFactory getDtoFactory() {
    return ((ReviewCommunityApplication) getApplication()).getDtoFactory();
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    subscriptions.add(getState().permissions.subscribe( permissions -> {
      this.permissions = permissions;
      onPermissionsUpdated();
    }));
    subscriptions.add(getState().initialized.subscribe(initialized -> {
      if (initialized) {
        onInitialized();
      } else {
        init();
      }
    }));
  }

  /**
   * Called when permissions of the user are updated in {@link AppState}. New permissions are
   * available in {@link #permissions}.
   */
  protected void onPermissionsUpdated() {}

  private void init() {
    getApi().getInit().subscribe(initDto -> {
      getState().csrfToken = initDto.getCsrfToken();
      getState().authenticatedUser.onNext(Optional.fromNullable(initDto.getAuthenticatedUser()));
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
   * Called once the activity has been created and the state is initialized. Usually you should NOT
   * dispatch API calls until this is called.
   */
  protected void onInitialized() {}

  /**
   * Sets the searchable configuration on the given SearchView.
   */
  protected void configureSearchView(SearchView searchView) {
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    ComponentName searchActivity = new ComponentName(getApplicationContext(), SearchActivity.class);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(searchActivity));
    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
  }
}
