package com.xinra.reviewcommunity.android;

import android.content.Intent;
import android.os.Bundle;

public class CategoryListActivity extends BaseActivity {

  private CategoryListView categoryTree;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_list);

    categoryTree = findViewById(R.id.categoryListTree);
    categoryTree.setOnCategoryClickListener(categorySerial -> {
      Intent categoryIntent = new Intent(this, CategoryActivity.class);
      categoryIntent.putExtra(Extras.CATEGORY, categorySerial);
      startActivity(categoryIntent);
    });
    subscriptions.add(getState().categoryTree.subscribe(categoryTree::setContent));
  }
}
