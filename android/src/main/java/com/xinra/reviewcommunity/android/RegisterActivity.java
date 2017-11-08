package com.xinra.reviewcommunity.android;

import android.os.Bundle;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.dto.RegistrationDto;

public class RegisterActivity extends AbstractActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);
  }

  @Override
  protected void onInitialized() {
    super.onInitialized();
    findViewById(R.id.registerBtn).setOnClickListener(view -> {
      TextView username = findViewById(R.id.registerUserName);
      TextView email = findViewById(R.id.registerEmail);
      TextView password = findViewById(R.id.registerPassword);
      TextView confirmPassword =  findViewById(R.id.registerConfirmPassword);

      String usernameText = username.getText().toString();
      String passwordText = password.getText().toString();

      if (!passwordText.equals(confirmPassword.getText().toString())) {
        confirmPassword.setError("Passwords are not the same!");
        return;
      }



      RegistrationDto dto = getDtoFactory().createDto(RegistrationDto.class);
      dto.setUsername(usernameText);
      dto.setPassword(passwordText);
      dto.setEmail(email.getText().toString());

      getApi().createUser(dto).subscribe(() -> {
        LoginActivity.login(this, usernameText, passwordText);
      }, this::handleError);
    });
  }
}
