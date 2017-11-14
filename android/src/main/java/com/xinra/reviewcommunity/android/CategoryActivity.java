package com.xinra.reviewcommunity.android;

import android.os.Bundle;

import com.xinra.reviewcommunity.shared.dto.CategoryDto;

public class CategoryActivity extends BaseActivity {

  public static final String CATEGORY_SERIAL = "com.xinra.reviewcommunity.CATEGORY_SERIAL";

  private int categorySerial;
  private ProductListView productListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    categorySerial = getIntent().getIntExtra(CATEGORY_SERIAL, 0);

    setContentView(R.layout.activity_category);

    productListView = findViewById(R.id.categoryProducts);
    productListView.setDisplayCategory(false);
  }

  @Override
  protected void onInitialized() {
    super.onInitialized();
    subscriptions.add(getState().categoryMap.subscribe(categoryMap -> {
      CategoryDto category = categoryMap.get(categorySerial);
      if (category == null) {
        // todo
      }

      setTitle(category.getName());
      productListView.setCategoryMap(categoryMap);

      getApi().getCategory(categorySerial).subscribe(productListView::setContent, this::handleError);
    }));
  }
}
