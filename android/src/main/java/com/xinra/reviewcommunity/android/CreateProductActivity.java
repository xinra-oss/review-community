package com.xinra.reviewcommunity.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.dto.CategoryDto;

import java.util.Collections;
import java.util.Map;

public class CreateProductActivity extends AbstractActivity {

  private static final int SELECT_CATEGORY = 1;

  private int categorySerial;
  private Map<Integer, CategoryDto> categoryMap = Collections.emptyMap();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    categorySerial = getIntent().getIntExtra(Extras.CATEGORY, 0);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_product);

    subscriptions.add(getState().categoryMap.subscribe(m -> {
      categoryMap = m;
      updateCategoryName();
    }));
  }

  @Override
  protected void onInitialized() {
    super.onInitialized();

    findViewById(R.id.productBrand).setOnClickListener(view -> {
      Intent selectCategoryIntent = new Intent(getApplicationContext(), SelectCategoryActivity.class);
      selectCategoryIntent.putExtra(Extras.CATEGORY, categorySerial);
      startActivityForResult(selectCategoryIntent, SELECT_CATEGORY);
    });

    updateCategoryName();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SELECT_CATEGORY) {
      if (resultCode == Activity.RESULT_OK) {
        categorySerial = data.getIntExtra(Extras.CATEGORY, categorySerial);
        updateCategoryName();
      }
      return;
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  private void updateCategoryName() {
    CategoryDto category = categoryMap.get(categorySerial);
    if (category != null) {
      ((TextView) findViewById(R.id.productBrand)).setText(category.getName());
    }
  }

}
