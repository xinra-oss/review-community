package com.xinra.reviewcommunity.android;

import android.content.Intent;
import android.media.Image;
import android.media.Rating;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import io.reactivex.functions.Consumer;

import io.reactivex.Single;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductActivity extends BaseActivity {

  public static final String PRODUCT_SERIAL = "com.xinra.reviewcommunity.PRODUCT_SERIAL";

    private int productSerial;
    private int categorySerial;
    private Map<Integer, CategoryDto> categoryMap = Collections.emptyMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productSerial = getIntent().getIntExtra(PRODUCT_SERIAL, 0);

        subscriptions.add(getState().categoryMap.subscribe(categoryMap -> {
            this.categoryMap = categoryMap;
            updateCategoryName();
        }));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Button addReview = findViewById(R.id.addReviewBtn);
        addReview.setOnClickListener(view -> {
            Intent addReviewIntent = new Intent(getApplicationContext(), AddReviewActivity.class);
            startActivity(addReviewIntent);
        });


    }

    private void updateCategoryName() {
        CategoryDto categoryDto = categoryMap.get(categorySerial);
        if (categoryDto != null) {
            ((TextView) findViewById(R.id.productCategory)).setText(categoryDto.getName());
        }
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();

        getApi().getProductDto(productSerial).subscribe(product -> {
            TextView productTitle = findViewById(R.id.productTitle);
            TextView productDescription = findViewById(R.id.productDescription);

            MaterialRatingBar avgRating = findViewById(R.id.productAvgRating);

            productTitle.setText(product.getName());
            productDescription.setText(product.getDescription());
            avgRating.setRating((float) product.getAvgRating());

            categorySerial = product.getCategorySerial();
            updateCategoryName();
        }, this::handleError);

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
