package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Toby on 13.11.2017.
 */

public class CommentListAdapter extends BaseAdapter {
    private List<ReviewCommentDto> commentList;
    private final LayoutInflater inflater;

    public void setCommentList(List<ReviewCommentDto> commentList) {
        this.commentList = commentList;
    }

    public CommentListAdapter(Context context, List<ReviewCommentDto> commentList) {
        this.commentList = commentList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if(view == null) {
            view = inflater.inflate(R.layout.item_comments, viewGroup, false);

            holder = new ViewHolder();
            holder.commentUsername = (TextView) view.findViewById(R.id.commentUsername);
            holder.commentDate = (TextView) view.findViewById(R.id.commentDate);
            holder.commentText = (TextView) view.findViewById(R.id.commentText);
            holder.commentInteractionBtn = (Button) view.findViewById(R.id.commentInteractionBtn);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Context context = viewGroup.getContext();
        ReviewCommentDto commentReviewDto = (ReviewCommentDto)getItem(i);
        holder.commentDate.setText(" published " + dateFormat.format(commentReviewDto.getCreatedAt()));
        holder.commentUsername.setText("By " + commentReviewDto.getUserDto().getName());
        holder.commentText.setText(commentReviewDto.getText());

        return view;
    }

    private static class ViewHolder {
        TextView commentUsername, commentDate, commentText;
        Button commentInteractionBtn;
    }
}
