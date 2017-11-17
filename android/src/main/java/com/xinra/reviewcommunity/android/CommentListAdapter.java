package com.xinra.reviewcommunity.android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentListAdapter extends BaseAdapter implements PopupMenu.OnMenuItemClickListener {
    private List<ReviewCommentDto> commentList;
    private final LayoutInflater inflater;
    private Context context;
    private int productSerial;
    private int reviewSerial;

    public void setCommentList(List<ReviewCommentDto> commentList) {
        this.commentList = commentList;
    }

    public CommentListAdapter(Context context, List<ReviewCommentDto> commentList, int productSerial, int reviewSerial) {
        this.context = context;
        this.commentList = commentList;
        this.productSerial = productSerial;
        this.reviewSerial = reviewSerial;
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

            final ImageButton menuButton = view.findViewById(R.id.commentMenuBtn);
            menuButton.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, menuButton);
                popupMenu.getMenuInflater().inflate(R.menu.item_interaction_context, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch(menuItem.getItemId()) {
                        case R.id.report:
                            Intent reportingIntent = new Intent(context, ReportActivity.class);
                            reportingIntent.putExtra(Extras.PRODUCT, productSerial);
                            reportingIntent.putExtra("ReviewSerial", reviewSerial);
                            reportingIntent.putExtra("CommentSerial", commentList.get(i).getSerial());
                            context.startActivity(reportingIntent);
                            Toast.makeText(context,
                                    menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                });
                popupMenu.show();
            });

            holder = new ViewHolder();
            holder.commentUsername = (TextView) view.findViewById(R.id.commentUsername);
            holder.commentDate = (TextView) view.findViewById(R.id.commentDate);
            holder.commentText = (TextView) view.findViewById(R.id.commentText);
            holder.commentInteractionBtn = (ImageButton) view.findViewById(R.id.commentMenuBtn);

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

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    private static class ViewHolder {
        TextView commentUsername, commentDate, commentText;
        ImageButton commentInteractionBtn;
    }
}
