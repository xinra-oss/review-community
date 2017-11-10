package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.dto.ReviewDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Toby on 10.11.2017.
 */

public class ReviewListAdapter extends BaseAdapter {
    private List<ReviewDto> reviewList;
    private final LayoutInflater inflater;

    public void setReviewList(List<ReviewDto> reviewList) {
        this.reviewList = reviewList;
    }

    public ReviewListAdapter(Context context, List<ReviewDto> reviewList) {
        this.reviewList = reviewList;
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

        Context context = viewGroup.getContext();
        ReviewDto reviewDto = (ReviewDto)getItem(i);
        holder.title.setText(reviewDto.getTitle());
        holder.date.setText( "published" + reviewDto.getCreatedAt().toString().substring(0,9));
        holder.username.setText("By " + reviewDto.getUserDto().getName());
        holder.text.setText(reviewDto.getText());
        holder.upVote.setText(reviewDto.getNumUpvotes());
        holder.downVote.setText(reviewDto.getNumDownvotes());

        return view;
    }

    private static class ViewHolder {
        TextView title, date, text, username, upVote, downVote;
        Button addViewCommentBtn, upBtn, downBtn;
    }
}
