package com.anshu.sustaincity;

import android.os.Bundle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ManageBlogsActivity extends AppCompatActivity {

    private RecyclerView rvBlogs;
    private Button btnAddBlog;
    private FirebaseFirestore db;
    private BlogAdapter blogAdapter;
    private List<Blog> blogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_blogs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rvBlogs = findViewById(R.id.rv_blogs);
        btnAddBlog = findViewById(R.id.btn_add_blog);

        db = FirebaseFirestore.getInstance();
        blogList = new ArrayList<>();
        blogAdapter = new BlogAdapter(this, blogList, true); // true for edit/delete option

        rvBlogs.setLayoutManager(new LinearLayoutManager(this));
        rvBlogs.setAdapter(blogAdapter);

        btnAddBlog.setOnClickListener(v -> {
            Intent intent = new Intent(ManageBlogsActivity.this, AddBlogActivity.class);
            startActivity(intent);
        });

        fetchBlogs();
    }
    private void fetchBlogs() {
        db.collection("blogs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    blogList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Blog blog = doc.toObject(Blog.class);
                        blog.setId(doc.getId());
                        blogList.add(blog);
                    }
                    blogAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ManageBlogsActivity.this, "Failed to load blogs", Toast.LENGTH_SHORT).show());
    }

    public void deleteBlog(String blogId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Blog")
                .setMessage("Are you sure you want to delete this blog?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.collection("blogs").document(blogId)
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(ManageBlogsActivity.this, "Blog Deleted", Toast.LENGTH_SHORT).show();
                                fetchBlogs();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(ManageBlogsActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("No", null)
                .show();
    }
}