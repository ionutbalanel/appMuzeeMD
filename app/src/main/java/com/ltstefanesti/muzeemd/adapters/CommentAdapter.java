package com.ltstefanesti.muzeemd.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.ltstefanesti.muzeemd.R;
import com.ltstefanesti.muzeemd.models.Comment;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final Context mContext;
    private final List<Comment> mData;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUserUID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        listener = clickListener;
    }

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Glide.with(mContext).load(mData.get(position).getuImg()).into(holder.imgUser);
        holder.tv_name.setText(mData.get(position).getuName());
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_date.setText(timestampToString((Long) mData.get(position).getTimeStamp()));
        if (mData.get(position).getuId().equals(currentUserUID)) {
            holder.deleteComment.setVisibility(View.VISIBLE);
        } else {
            holder.deleteComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView imgUser;
        TextView tv_name, tv_content, tv_date, deleteComment;

        public CommentViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.comment_user_img);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
            deleteComment = itemView.findViewById(R.id.delete_comment);
            deleteComment.setOnClickListener(view -> listener.OnItemClick(getBindingAdapterPosition()));
        }
    }

    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar).toString();
    }
}