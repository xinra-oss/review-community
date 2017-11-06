package com.xinra.reviewcommunity.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Base class for "top-level" activities that have the toolbar and side drawer. Note that subclasses
 * must disable the default action bar.
 */
public abstract class BaseActivity extends AbstractActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  private FrameLayout contentFrame;

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
    TextView navUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_username);

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
    return true;
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

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_login) {
      Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
      startActivity(loginIntent);
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

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
