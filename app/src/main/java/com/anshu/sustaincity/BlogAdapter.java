package com.anshu.sustaincity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;
    private boolean isNgoDashboard;
    private FirebaseFirestore db;

    public BlogAdapter(Context context, List<Blog> blogList, boolean isNgoDashboard) {
        this.context = context;
        this.blogList = blogList;
        this.isNgoDashboard = isNgoDashboard;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        holder.tvTitle.setText(blog.getTitle());
        holder.tvContent.setText(blog.getContent());
        holder.tvAuthor.setText("By: " + blog.getAuthor());
        holder.tvDate.setText(blog.getDate());

        // Load Image if available
        if (blog.getImageUrl() != null && !blog.getImageUrl().isEmpty()) {
            holder.imgBlog.setVisibility(View.VISIBLE);
            Glide.with(context).load(blog.getImageUrl()).into(holder.imgBlog);
        } else {
            holder.imgBlog.setVisibility(View.GONE);
        }

        // Show NGO actions if it's the NGO Dashboard
        if (isNgoDashboard) {
            holder.ngoActions.setVisibility(View.VISIBLE);

            holder.btnEdit.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditBlogActivity.class);
                intent.putExtra("blogId", blog.getId());
                context.startActivity(intent);
            });

            holder.btnDelete.setOnClickListener(v -> {
                db.collection("blogs").document(blog.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(context, "Blog Deleted", Toast.LENGTH_SHORT).show();
                            blogList.remove(position);
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Failed to delete blog", Toast.LENGTH_SHORT).show());
            });
        } else {
            holder.ngoActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvAuthor, tvDate;
        ImageView imgBlog;
        ImageButton btnEdit, btnDelete;
        View ngoActions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBlogTitle);
            tvContent = itemView.findViewById(R.id.tvBlogContent);
            tvAuthor = itemView.findViewById(R.id.tvBlogAuthor);
            tvDate = itemView.findViewById(R.id.tvBlogDate);
            imgBlog = itemView.findViewById(R.id.ivBlogImage);
            btnEdit = itemView.findViewById(R.id.btnEditBlog);
            btnDelete = itemView.findViewById(R.id.btnDeleteBlog);
            ngoActions = itemView.findViewById(R.id.ngoActions);
        }
    }
}
