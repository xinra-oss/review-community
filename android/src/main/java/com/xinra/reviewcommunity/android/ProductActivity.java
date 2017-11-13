package com.xinra.reviewcommunity.android;

import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;

public class ProductActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        getApi().getReviewList(3, OrderBy.RATING).subscribe(reviews -> {
            ListAdapter reviewAdapter = new ReviewListAdapter(this, reviews);
            ListView listView = findViewById(R.id.reviewListView);
            listView.setAdapter(reviewAdapter);
        }, this::handleError);
    }
}
