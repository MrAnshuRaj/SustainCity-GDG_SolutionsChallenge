package com.anshu.sustaincity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
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

public class BlogsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBlogs;
    private BlogAdapter blogAdapter;
    private FirebaseFirestore db;
    private List<Blog> blogList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_blogs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerViewBlogs = findViewById(R.id.recyclerViewBlogs);
        FloatingActionButton fabAddBlog = findViewById(R.id.fabAddBlog);

        db = FirebaseFirestore.getInstance();
        blogList = new ArrayList<>();
        blogAdapter = new BlogAdapter(this,blogList,false);
        recyclerViewBlogs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBlogs.setAdapter(blogAdapter);

        fabAddBlog.setOnClickListener(v -> startActivity(new Intent(this, AddBlogActivity.class)));

        fetchBlogs();
    }
    private void fetchBlogs() {
        db.collection("blogs").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                blogList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Blog blog = document.toObject(Blog.class);
                    blogList.add(blog);
                }
                blogAdapter.notifyDataSetChanged();
            }
        });
    }
}