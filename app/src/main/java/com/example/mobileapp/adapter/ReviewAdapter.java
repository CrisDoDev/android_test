package com.example.mobileapp.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobileapp.R;
import com.example.mobileapp.model.Review;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        // Dựa vào thuộc tính getUserId() có sẵn để đặt tên động
        if (review.getUserName() != null && !review.getUserName().trim().isEmpty()) {
            holder.txtReviewUserName.setText(review.getUserName());
        } else {
            holder.txtReviewUserName.setText("Khách hàng Oasis");
        }
        holder.txtReviewComment.setText(review.getComment());


        if (review.getCreatedAt() != null && review.getCreatedAt().length() >= 10) {
            holder.txtReviewDate.setText(review.getCreatedAt().substring(0, 10));
        } else {
            holder.txtReviewDate.setText("Vừa xong");
        }

        // =========================================================================
        // ĐỔI SAO ĐỘNG
        // =========================================================================
        int actualStars = review.getRatingStars();

        for (int i = 0; i < 5; i++) {
            ImageView imgStar = (ImageView) holder.layoutStarsContainer.getChildAt(i);
            if (imgStar != null) {
                if (i < actualStars) {
                    // Sao hợp lệ: Đổi sang ảnh sao đặc VÀ ép màu vàng
                    imgStar.setImageResource(android.R.drawable.btn_star_big_on);
                    imgStar.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FFC107")));
                } else {
                    // Sao thừa: Đổi sang ảnh sao rỗng VÀ ép màu xám
                    imgStar.setImageResource(android.R.drawable.btn_star_big_off);
                    imgStar.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        }

        // =========================================================================
        // Xử lý hiển thị phản hồi của Quán
        // =========================================================================
        if (review.getAdminReply() != null && !review.getAdminReply().trim().isEmpty()) {
            holder.txtAdminReplyContent.setText(review.getAdminReply());

            //set cứng dòng chữ trạng thái thông báo
            holder.txtAdminRepliedAt.setText("Quán đã phản hồi");

            holder.layoutAdminReplyContainer.setVisibility(View.VISIBLE);
        } else {
            holder.layoutAdminReplyContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView txtReviewUserName, txtReviewDate, txtReviewComment, txtAdminRepliedAt, txtAdminReplyContent;
        LinearLayout layoutStarsContainer, layoutAdminReplyContainer;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReviewUserName = itemView.findViewById(R.id.txtReviewUserName);
            txtReviewDate = itemView.findViewById(R.id.txtReviewDate);
            txtReviewComment = itemView.findViewById(R.id.txtReviewComment);
            layoutStarsContainer = itemView.findViewById(R.id.layoutStarsContainer);
            layoutAdminReplyContainer = itemView.findViewById(R.id.layoutAdminReplyContainer);
            txtAdminRepliedAt = itemView.findViewById(R.id.txtAdminRepliedAt);
            txtAdminReplyContent = itemView.findViewById(R.id.txtAdminReplyContent);
        }
    }
}