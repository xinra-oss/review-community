package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.dto.ProductDto;

import java.util.Collections;
import java.util.List;

public class ProductListView extends ConstraintLayout implements PopupMenu.OnMenuItemClickListener {

  private List<ProductDto> products;
  private Adapter adapter;

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
    products = Collections.emptyList();
    adapter = new Adapter();

    LayoutInflater.from(getContext()).inflate(R.layout.view_product_list, this);
    ((ListView) findViewById(R.id.productList)).setAdapter(adapter);
  }

  /**
   * Must be called from the UI thread!
   */
  public void setContent(List<ProductDto> products) {
    this.products = products;
    adapter.notifyDataSetChanged();
  }

  @Override
  public boolean onMenuItemClick(MenuItem menuItem) {
    return true;
  }

  private class Adapter extends BaseAdapter {

    @Override
    public int getCount() {
      return products.size();
    }

    @Override
    public Object getItem(int i) {
      return products.get(0);
    }

    @Override
    public long getItemId(int i) {
      return products.get(i).getSerial();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
      ViewHolder holder;

      if (view == null) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.item_product, viewGroup, false);

        final ImageButton menuButton = view.findViewById(R.id.productItemMenuBtn);
        menuButton.setOnClickListener(v -> {
          PopupMenu popupMenu = new PopupMenu(getContext(), menuButton);
          popupMenu.getMenuInflater().inflate(R.menu.item_product_context, popupMenu.getMenu());
          popupMenu.setOnMenuItemClickListener(ProductListView.this);
          popupMenu.show();
        });

        holder = new ViewHolder();
        holder.name = view.findViewById(R.id.productItemName);

        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }

      ProductDto product = (ProductDto) getItem(i);
      holder.name.setText(product.getName());

      return view;
    }

  }

  private static class ViewHolder {
    TextView name;
  }

}
