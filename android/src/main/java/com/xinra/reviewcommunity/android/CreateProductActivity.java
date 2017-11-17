package com.xinra.reviewcommunity.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.CreateProductDto;

import java.util.Collections;
import java.util.Map;

public class CreateProductActivity extends AbstractActivity {

  private static final int SELECT_CATEGORY = 1;

  private int categorySerial;
  private TextView barcode;
  private ImageButton removeBarcode;
  private Map<Integer, CategoryDto> categoryMap = Collections.emptyMap();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    categorySerial = getIntent().getIntExtra(Extras.CATEGORY, 0);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_product);

    barcode = findViewById(R.id.createProductBarcode);
    removeBarcode = findViewById(R.id.createProductRemoveBarcode);

    setBarcode(getIntent().getStringExtra(Extras.BARCODE));

    subscriptions.add(getState().categoryMap.subscribe(m -> {
      categoryMap = m;
      updateCategoryName();
    }));
  }

  private void setBarcode(String barcode) {
    this.barcode.setText(barcode);
    if (barcode == null) {
      removeBarcode.setEnabled(false);
      removeBarcode.setAlpha(.3f);
    } else {
      removeBarcode.setEnabled(true);
      removeBarcode.setAlpha(1f);
    }
  }

  @Override
  protected void onInitialized() {
    super.onInitialized();

    findViewById(R.id.createProductCategory).setOnClickListener(view -> {
      Intent selectCategoryIntent = new Intent(getApplicationContext(),
          SelectCategoryActivity.class);
      selectCategoryIntent.putExtra(Extras.CATEGORY, categorySerial);
      startActivityForResult(selectCategoryIntent, SELECT_CATEGORY);
    });

    findViewById(R.id.createProductBrand).setOnClickListener(view -> {
      Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
    });

    findViewById(R.id.createProductScan).setOnClickListener(this::startBarcodeScan);
    findViewById(R.id.createProductRemoveBarcode).setOnClickListener(view -> setBarcode(null));
    findViewById(R.id.createProductSaveBtn).setOnClickListener(view -> save());

    updateCategoryName();
  }

  private void save() {
    CreateProductDto dto = getDtoFactory().createDto(CreateProductDto.class);
    dto.setBarcode(getText(barcode));
    dto.setCategorySerial(categorySerial);
    dto.setName(getText(findViewById(R.id.createProductName)));
    dto.setDescription(getText(findViewById(R.id.createProductDescription)));

    getApi().createProduct(dto).subscribe(productSerialDto -> {
      Toast.makeText(this, R.string.create_product_success, Toast.LENGTH_SHORT).show();
      Intent productIntent = new Intent(this, ProductActivity.class);
      productIntent.putExtra(Extras.PRODUCT, productSerialDto.getSerial());
      startActivity(productIntent);
      finish();
    }, this::handleError);
  }

  @Override
  protected void onScanResult(String barcode) {
    setBarcode(barcode);
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
      ((TextView) findViewById(R.id.createProductCategory)).setText(category.getName());
    }
  }

}
