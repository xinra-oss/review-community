package com.xinra.reviewcommunity.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.xinra.reviewcommunity.shared.dto.CreateReviewDto;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class AddReviewActivity extends BaseActivity {

    private int productSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productSerial = getIntent().getIntExtra(ProductActivity.PRODUCT_SERIAL, 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
    }

    @Override
    protected void onInitialized() {
        super.onInitialized();

        findViewById(R.id.saveBtn).setOnClickListener(view -> {
            CreateReviewDto dto = getDtoFactory().createDto(CreateReviewDto.class);
            dto.setRating((int) ((MaterialRatingBar) findViewById(R.id.reviewRating)).getRating());
            dto.setText(((EditText) findViewById(R.id.reviewText)).getText().toString());
            dto.setTitle(((EditText) findViewById(R.id.reviewTitle)).getText().toString());

            getApi().createReview(dto, productSerial).subscribe(this::finish, this::handleError);
        });
    }
}
