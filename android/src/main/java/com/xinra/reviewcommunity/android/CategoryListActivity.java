package com.xinra.reviewcommunity.android;

import android.os.Bundle;

public class CategoryListActivity extends BaseActivity {

  private CategoryListView categoryTree;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category_list);

    categoryTree = findViewById(R.id.categoryListTree);
    getState().categoryTree.subscribe(categoryTree::setContent);
  }
}
