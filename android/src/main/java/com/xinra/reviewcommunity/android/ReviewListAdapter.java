package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Supplier;
import io.reactivex.functions.BiConsumer;

import io.reactivex.functions.Consumer;

/**
 * Created by Toby on 10.11.2017.
 */

public class ReviewListAdapter extends BaseAdapter {

    public static interface CommentSupplier {
        void getComments(int reviewSerial, Consumer<List<ReviewCommentDto>> callback);
    }

    private List<ReviewDto> reviewList;
    private CommentSupplier commentSupplier;
    private final LayoutInflater inflater;

    public void setReviewList(List<ReviewDto> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewListAdapter(Context context, List<ReviewDto> reviewList, CommentSupplier commentSupplier) {
        this.reviewList = reviewList;
        this.commentSupplier = commentSupplier;
        this.inflater = LayoutInflater.from(context);
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

            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.reviewTitle);
            holder.username = (TextView) view.findViewById(R.id.reviewUsername);
            holder.date = (TextView) view.findViewById(R.id.reviewDate);
            holder.text = (TextView) view.findViewById(R.id.reviewText);
            holder.upVote = (TextView) view.findViewById(R.id.upVoteCount);
            holder.downVote = (TextView) view.findViewById(R.id.downVoteCount);
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
        holder.upVote.setText(reviewDto.getNumUpvotes() + "");
        holder.downVote.setText(reviewDto.getNumDownvotes() + "");
        holder.addViewCommentBtn.setText("View Comments");

        holder.addViewCommentBtn.setOnClickListener(v -> {
            commentSupplier.getComments(reviewDto.getSerial(), comments -> {
                ListAdapter commentListAdapter = new CommentListAdapter(context, comments);
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

        return view;
    }

    private static class ViewHolder {
        TextView title, date, text, username, upVote, downVote;
        Button addViewCommentBtn, upBtn, downBtn;
    }
}
