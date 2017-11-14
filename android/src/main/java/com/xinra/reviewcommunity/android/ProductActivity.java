package com.xinra.reviewcommunity.android;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;

import java.util.List;
import io.reactivex.functions.Consumer;

import io.reactivex.Single;

public class ProductActivity extends BaseActivity {

  public static final String PRODUCT_SERIAL = "com.xinra.reviewcommunity.PRODUCT_SERIAL";

    private int productSerial = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Button addReview = findViewById(R.id.addReviewBtn);
        addReview.setOnClickListener(view -> {
            Intent addReviewIntent = new Intent(getApplicationContext(), CategoryListActivity.class);
            startActivity(addReviewIntent);
        });


    }

    @Override
    protected void onInitialized() {
        super.onInitialized();
        getApi().getReviewList(productSerial, OrderBy.RATING).subscribe(reviews -> {
            ListAdapter reviewAdapter = new ReviewListAdapter(this, reviews, this::loadComments);
            ListView listView = findViewById(R.id.reviewListView);
            listView.setAdapter(reviewAdapter);
        }, this::handleError);
    }

    private void loadComments(int reviewSerial, Consumer<List<ReviewCommentDto>> callback) {
        getApi().getCommentList(productSerial, reviewSerial).subscribe(callback, this::handleError);
    }

}
