package com.example.fatihcemildemirhomework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder> {
    List<CommentItem> commentItems;
    Context context;
    CommentItemClickListener listener;

    public CommentAdapter(List<CommentItem> commentItems, Context context, CommentItemClickListener listener) {
        this.commentItems = commentItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comments_row_layout,parent,false);
        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, final int position) {
        holder.comment.setText(commentItems.get(position).getMessage());
        holder.name.setText(commentItems.get(position).getName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.CommentItemClicked(commentItems.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }
    public interface CommentItemClickListener{
        public void CommentItemClicked(CommentItem selectedCommentItem);
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        TextView comment;
        TextView name;
        ConstraintLayout root;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.comment_txt);
            name = itemView.findViewById(R.id.commenter_name);
            root = itemView.findViewById(R.id.comment_container);

        }
    }
}
