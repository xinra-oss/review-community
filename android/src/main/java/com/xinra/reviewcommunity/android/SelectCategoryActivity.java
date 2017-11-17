package com.xinra.reviewcommunity.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SelectCategoryActivity extends AbstractActivity {

  private CategoryListView categoryTree;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_category);

    categoryTree = findViewById(R.id.selectCategoryTree);
    categoryTree.setOnCategoryClickListener(categorySerial -> {
      Intent data = new Intent();
      data.putExtra(Extras.CATEGORY, categorySerial);
      setResult(Activity.RESULT_OK, data);
      finish();
    });
    categoryTree.setSelectedCategory(getIntent().getIntExtra(Extras.CATEGORY, 0));
    subscriptions.add(getState().categoryTree.subscribe(categoryTree::setContent));
  }
}
