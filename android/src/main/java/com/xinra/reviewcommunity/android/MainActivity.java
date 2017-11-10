package com.xinra.reviewcommunity.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Button addCategoryListBtn = findViewById(R.id.mainCategoryListBtn);
    addCategoryListBtn.setOnClickListener(view -> {
      Intent categoriesIntent = new Intent(getApplicationContext(), CategoryListActivity.class);
      startActivity(categoriesIntent);
    });
  }

  
}
