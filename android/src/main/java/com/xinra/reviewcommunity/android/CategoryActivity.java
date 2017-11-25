package com.xinra.reviewcommunity.android;

import android.os.Bundle;

import com.xinra.reviewcommunity.shared.dto.CategoryDto;

public class CategoryActivity extends BaseActivity {

  private int categorySerial;
  private ProductListView productListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    categorySerial = getIntent().getIntExtra(Extras.CATEGORY, 0);

    setContentView(R.layout.activity_category);

    productListView = findViewById(R.id.categoryProducts);
  }

  @Override
  protected void onInitialized() {
    super.onInitialized();
    subscriptions.add(getState().categoryMap.subscribe(categoryMap -> {
      CategoryDto category = categoryMap.get(categorySerial);
      if (category == null) {
        // TODO what happens if the category is removed while we are on this activity or come back to it?
        return;
      }

      setTitle(category.getName());
      productListView.setDisplayCategory(!category.getChildren().isEmpty());
      productListView.setCategoryMap(categoryMap);

      getApi().getProductsByCategory(categorySerial).subscribe(productListView::setContent, this::handleError);
    }));
  }
}
