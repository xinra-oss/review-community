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
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;
import com.xinra.reviewcommunity.shared.dto.ReviewVoteDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import io.reactivex.functions.Consumer;

import io.reactivex.Single;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductActivity extends BaseActivity implements ReviewListAdapter.OnVoteListener, ReviewListAdapter.CommentSupplier {

    private int productSerial;
    private int categorySerial;
    private Map<Integer, CategoryDto> categoryMap = Collections.emptyMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        productSerial = getIntent().getIntExtra(Extras.PRODUCT, 0);

        subscriptions.add(getState().categoryMap.subscribe(categoryMap -> {
            this.categoryMap = categoryMap;
            updateCategoryName();
        }));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Button addReview = findViewById(R.id.addReviewBtn);
        addReview.setOnClickListener(view -> {
            Intent addReviewIntent = new Intent(getApplicationContext(), AddReviewActivity.class);
            addReviewIntent.putExtras(getIntent());
            startActivityForResult(addReviewIntent, 1);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
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
        refresh();
    }

    private void refresh() {
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
            ListAdapter reviewAdapter = new ReviewListAdapter(this, reviews, productSerial, this, this);
            ListView listView = findViewById(R.id.reviewListView);
            listView.setAdapter(reviewAdapter);
        }, this::handleError);
    }

    @Override
    public void getComments(int reviewSerial, Consumer<List<ReviewCommentDto>> callback) {
        getApi().getCommentList(productSerial, reviewSerial).subscribe(callback, this::handleError);
    }

    @Override
    public void onVote(ReviewDto reviewDto, boolean isUpvote) {
      if (permissions.contains(Permission.VOTE)) {
        ReviewVoteDto voteDto = getDtoFactory().createDto(ReviewVoteDto.class);
        voteDto.setUpvote(isUpvote);
        getApi().createOrUpdateReviewVote(productSerial, reviewDto.getSerial(), voteDto).subscribe(this::refresh, this::handleError);
        // todo update without re-fetch
      } else {
        showLoginRequiredPopup(R.string.review_vote_auth);
      }

    }

  @Override
  protected void onPermissionsUpdated() {
    super.onPermissionsUpdated();
    refresh();
  }
}
