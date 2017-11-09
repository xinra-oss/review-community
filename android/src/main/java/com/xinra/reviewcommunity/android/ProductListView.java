package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class ProductListView extends ConstraintLayout {

  public ProductListView(Context context) {
    super(context);
    init();
  }

  public ProductListView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ProductListView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    LayoutInflater.from(getContext()).inflate(R.layout.view_product_list, this);
  }

}
