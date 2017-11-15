package com.xinra.reviewcommunity.android;

import android.os.Bundle;

public class CreateProductActivity extends AbstractActivity {

  public static final String BARCODE = "com.xinra.reviewcommunity.BARCODE";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_product);
  }
}
