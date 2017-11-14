package com.xinra.reviewcommunity.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    configureSearchView(findViewById(R.id.mainSearchWidget));

    Button addCategoryListBtn = findViewById(R.id.mainCategoryListBtn);
    addCategoryListBtn.setOnClickListener(view -> {
      Intent categoriesIntent = new Intent(getApplicationContext(), CategoryListActivity.class);
      startActivity(categoriesIntent);
    });
  }

  @Override
  protected boolean isSearchInActionBarEnabled() {
    return false;
  }
}
