package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.ProductDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;
import com.xinra.reviewcommunity.shared.dto.ReviewVoteDto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;
import io.reactivex.functions.BiConsumer;

import io.reactivex.functions.Consumer;

public class ReviewListAdapter extends BaseAdapter implements PopupMenu.OnMenuItemClickListener {

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return true;
    }

    public interface CommentSupplier {
        void getComments(int reviewSerial, Consumer<List<ReviewCommentDto>> callback);
    }

    public interface  OnVoteListener {
      void onVote(ReviewDto reviewDto, boolean isUpvote);
    }

    private List<ReviewDto> reviewList;
    private CommentSupplier commentSupplier;
    private LayoutInflater inflater;
    private Context context;
    private int productSerial;
    private OnVoteListener onVoteListener;

    public ReviewListAdapter(Context context, List<ReviewDto> reviewList, int productSerial, CommentSupplier commentSupplier, OnVoteListener onVoteListener) {
      this.context = context;
      this.reviewList = reviewList;
      this.productSerial = productSerial;
      this.commentSupplier = commentSupplier;
      this.inflater = LayoutInflater.from(context);
      this.onVoteListener = onVoteListener;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int i) {
        return reviewList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.item_review, viewGroup, false);

            final ImageButton menuButton = view.findViewById(R.id.reviewMenuBtn);
            menuButton.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, menuButton);
                popupMenu.getMenuInflater().inflate(R.menu.item_interaction_context, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()) {
                        case R.id.report:
                            Intent reportingIntent = new Intent(context, ReportActivity.class);
                            reportingIntent.putExtra(Extras.PRODUCT, productSerial);
                            reportingIntent.putExtra("ReviewSerial", reviewList.get(i).getSerial());
                            context.startActivity(reportingIntent);
                    }
                    return true;
                });
                popupMenu.show();
            });

            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.reviewTitle);
            holder.username = (TextView) view.findViewById(R.id.reviewUsername);
            holder.date = (TextView) view.findViewById(R.id.reviewDate);
            holder.text = (TextView) view.findViewById(R.id.reviewText);
            holder.addViewCommentBtn = (Button) view.findViewById(R.id.addOrViewCommentBtn);
            holder.upBtn = (Button) view.findViewById(R.id.upVoteReviewBtn);
            holder.downBtn = (Button) view.findViewById(R.id.downVoteReviewBtn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final View finalView = view; // for use in lambda

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Context context = viewGroup.getContext();
        ReviewDto reviewDto = (ReviewDto)getItem(i);
        holder.title.setText(reviewDto.getTitle());
        holder.date.setText( " published " + dateFormat.format(reviewDto.getCreatedAt()));
        holder.username.setText("By " + reviewDto.getUserDto().getName());
        holder.text.setText(reviewDto.getText());
        holder.upBtn.setText(reviewDto.getNumUpvotes() + "");
        holder.downBtn.setText(reviewDto.getNumDownvotes() + "");
        holder.addViewCommentBtn.setText("View Comments");

        if (reviewDto.getAuthenticatedUserVote() != null) {
          if (reviewDto.getAuthenticatedUserVote().isUpvote()) {
            holder.upBtn.setEnabled(false);
            holder.upBtn.setTextColor(Color.BLUE);
          } else {
            holder.downBtn.setEnabled(false);
            holder.downBtn.setTextColor(Color.BLUE);
          }
        }

        holder.addViewCommentBtn.setOnClickListener(v -> {
            commentSupplier.getComments(reviewDto.getSerial(), comments -> {
                ListAdapter commentListAdapter = new CommentListAdapter(context, comments, productSerial, reviewList.get(i).getSerial());
                ListView listView = finalView.findViewById(R.id.commentListView);

                if(holder.addViewCommentBtn.getText() == "View Comments") {
                    listView.setVisibility(View.VISIBLE);
                    listView.setAdapter(commentListAdapter);
                    holder.addViewCommentBtn.setText("Close Comments");
                }else{
                    listView.setVisibility(View.GONE);
                    holder.addViewCommentBtn.setText("View Comments");
                }
            });
        });

        holder.upBtn.setOnClickListener(v -> onVoteListener.onVote(reviewDto, true));
        holder.downBtn.setOnClickListener(v -> onVoteListener.onVote(reviewDto, false));

        return view;
    }

    private static class ViewHolder {
        TextView title, date, text, username;
        Button addViewCommentBtn, upBtn, downBtn;
    }
}
