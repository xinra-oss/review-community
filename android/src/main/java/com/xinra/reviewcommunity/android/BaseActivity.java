package com.xinra.reviewcommunity.android;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Optional;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xinra.reviewcommunity.shared.ApiException;
import com.xinra.reviewcommunity.shared.Permission;

import java.util.Collections;

/**
 * Base class for "top-level" activities that have the toolbar and side drawer. Note that subclasses
 * must disable the default action bar.
 */
public abstract class BaseActivity extends AbstractActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private FrameLayout contentFrame;
  private TextView navUsername;

  @Override
  protected void onInitialized() {
    super.onInitialized();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.setContentView(R.layout.activity_base);
    contentFrame = (FrameLayout) findViewById(R.id.content_frame);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.addDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    MenuItem navLogin = navigationView.getMenu().findItem(R.id.nav_login);
    MenuItem navRegister = navigationView.getMenu().findItem(R.id.nav_register);
    MenuItem navLogout = navigationView.getMenu().findItem(R.id.nav_logout);
    navUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_username);

    this.subscriptions.add(getState().authenticatedUser.subscribe(user -> {
      if (user.isPresent()) {
        navLogin.setVisible(false);
        navRegister.setVisible(false);
        navLogout.setVisible(true);
        navUsername.setText(user.get().getName());
      } else {
        navLogin.setVisible(true);
        navRegister.setVisible(true);
        navLogout.setVisible(false);
      }
    }));
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.base, menu);

    MenuItem search = menu.findItem(R.id.app_bar_search);
    if (isSearchInActionBarEnabled()) {
      configureSearchView((SearchView) search.getActionView());
    } else {
      search.setVisible(false);
    }

    return true;
  }

  @Override
  protected void onScanResult(String barcode) {
    getApi().getProductSerialByBarcode(barcode).subscribe(productSerial -> {
      Intent productIntent = new Intent(getApplicationContext(), ProductActivity.class);
      productIntent.putExtra(Extras.PRODUCT, productSerial);
      startActivity(productIntent);
    }, error -> {
        if (error instanceof ApiException && ((ApiException) error).getStatus() == 404) {
          if (((ApiException) error).getException().equals(
              "com.xinra.reviewcommunity.service.BarcodeService$BarcodeNotFoundException")) {

            if (permissions.contains(Permission.CREATE_PRODUCT)) {
              new AlertDialog.Builder(this)
                  .setTitle(R.string.barcode_not_found)
                  .setNeutralButton(android.R.string.cancel, this::doNothingOnClick)
                  .setMessage(R.string.barcode_not_found_message_create)
                  .setPositiveButton(R.string.create, (dialog, which) -> {
                    Intent createProductIntent =
                        new Intent(getApplicationContext(), CreateProductActivity.class);
                    createProductIntent.putExtra(Extras.BARCODE, barcode);
                    startActivity(createProductIntent);
                  })
                  .setNegativeButton(R.string.out_of_scope, (dialog, which) -> {
                    Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
                  })
                  .show();
            } else {
              showLoginRequiredPopup(R.string.barcode_not_found_message,
                  R.string.barcode_not_found);
            }
          } else {
            new AlertDialog.Builder(this)
                .setTitle(R.string.out_of_scope_message)
                .setPositiveButton(android.R.string.ok, this::doNothingOnClick)
                .show();
          }
          return;
        }
        handleError(error);
    });
  }

  /**
   * Use this to not do anything when clicking a dialog button apart from closing the dialog.
   * Checkstyle doesn't like empty lambdas.
   */
  protected void doNothingOnClick(DialogInterface dialog, int which) {}

  /**
   * Override to hide the search widget in the action bar.
   */
  protected boolean isSearchInActionBarEnabled() {
    return true;
  }

  protected void showLoginRequiredPopup(int messageResource) {
    showLoginRequiredPopup(messageResource, R.string.authentication_required);
  }

  protected void showLoginRequiredPopup(int messageResource, int titleResource) {
    new AlertDialog.Builder(this)
        .setTitle(titleResource)
        .setNeutralButton(android.R.string.cancel, this::doNothingOnClick)
        .setMessage(messageResource)
        .setPositiveButton(R.string.sign_in, (dialog, which) -> {
          Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
          startActivity(loginIntent); // todo continue after login
        })
        .setNegativeButton(R.string.sign_up, (dialog, which) -> {
          Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
          startActivity(registerIntent); // todo continue after login
        })
        .show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void logout() {
    getApi().deleteSession().subscribe(() -> {
      getState().authenticatedUser.onNext(Optional.absent());
      getState().permissions.onNext(Collections.emptySet());
      navUsername.setText(R.string.unauthenticated_username);
      Toast.makeText(this, "Successfully signed out", Toast.LENGTH_SHORT).show();
      // When logging out the session is destroyed. This invalidates the CSRF token and we have to
      // get a new one manually.
      getApi().getCsrfToken().subscribe(token -> getState().csrfToken = token, this::handleError);
    }, this::handleError);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.addProduct) {
      if (permissions.contains(Permission.CREATE_PRODUCT)) {
        Intent createProductIntent = new Intent(getApplicationContext(), CreateProductActivity.class);
        startActivity(createProductIntent);
      } else {
        showLoginRequiredPopup(R.string.create_product_auth);
      }
    } else if (id == R.id.nav_login) {
      Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
      startActivity(loginIntent);
    } else if (id == R.id.nav_register) {
      Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
      startActivity(registerIntent);
    } else if (id == R.id.nav_logout) {
      logout();
    } else if (id == R.id.nav_manage) {
      Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
      startActivity(searchIntent);
    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {
      Intent productIntent = new Intent(getApplicationContext(), ProductActivity.class);
      startActivity(productIntent);
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    return true;
  }


  // Override all setContentView methods to put the content view to the FrameLayout contentFrame
  // so that, we can make other activity implementations looks like normal activity subclasses.

  @Override
  public void setContentView(int layoutResID) {
    if (contentFrame != null) {
      LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
      ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);
      View stubView = inflater.inflate(layoutResID, contentFrame, false);
      contentFrame.addView(stubView, lp);
    }
  }

  @Override
  public void setContentView(View view) {
    if (contentFrame != null) {
      ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT);
      contentFrame.addView(view, lp);
    }
  }

  @Override
  public void setContentView(View view, ViewGroup.LayoutParams params) {
    if (contentFrame != null) {
      contentFrame.addView(view, params);
    }
  }
}
