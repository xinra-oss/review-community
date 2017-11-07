package com.xinra.reviewcommunity.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Optional;

public class LoginActivity extends AbstractActivity {

  // static helper that is also called by RegisterActivity
  public static void login(AbstractActivity activity, String usernameOrEmail, String password) {
    activity.getApi().getSession(usernameOrEmail, password).subscribe(auth -> {
      activity.getState().csrfToken = auth.getCsrfToken();
      activity.getState().permissions.onNext(auth.getPermissions());
      activity.getState().authenticatedUser.onNext(Optional.of(auth.getUser()));
      activity.finish();
    }, activity::handleError);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
  }

  @Override
  protected void onInitialized() {
    super.onInitialized();
    findViewById(R.id.loginBtn).setOnClickListener(view -> {
      String usernameOrEmail = ((TextView) findViewById(R.id.loginUserName)).getText().toString();
      String password = ((TextView) findViewById(R.id.loginPassword)).getText().toString();
      login(this, usernameOrEmail, password);
    });
  }
}
