package com.xinra.reviewcommunity.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

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

    findViewById(R.id.mainScanBtn).setOnClickListener(this::startBarcodeScan);
  }

  @Override
  protected boolean isSearchInActionBarEnabled() {
    return false;
  }
}
