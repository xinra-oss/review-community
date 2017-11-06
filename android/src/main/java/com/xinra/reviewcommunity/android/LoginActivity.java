package com.xinra.reviewcommunity.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Optional;

public class LoginActivity extends AbstractActivity {

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
      getApi().getSession(usernameOrEmail, password).subscribe(auth -> {
        getState().csrfToken = auth.getCsrfToken();
        getState().permissions.onNext(auth.getPermissions());
        getState().authenticatedUser.onNext(Optional.of(auth.getUser()));
        finish();
      }, this::handleError);
    });
  }
}
