package com.anshu.sustaincity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NGODashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodItemAdapter adapter;
    private List<FoodItem> foodItemList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button btnAddFood, btnManageEvents, btnManageBlogs,btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ngodashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAddFood = findViewById(R.id.btn_add_food);
        btnManageEvents = findViewById(R.id.btn_manage_events);
        btnManageBlogs = findViewById(R.id.btn_manage_blogs);
        recyclerView = findViewById(R.id.recyclerView_food_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        btnLogout=findViewById(R.id.buttonlogout);
        db = FirebaseFirestore.getInstance();
        foodItemList = new ArrayList<>();
        adapter = new FoodItemAdapter(this, foodItemList);
        recyclerView.setAdapter(adapter);

        btnAddFood.setOnClickListener(v -> startActivity(new Intent(this, AddFoodActivity.class)));
        btnManageEvents.setOnClickListener(v -> startActivity(new Intent(this, ManageCleaningEventsActivity.class)));
        btnManageBlogs.setOnClickListener(v -> startActivity(new Intent(this, ManageBlogsActivity.class)));
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("SustainCity", MODE_PRIVATE);
            sharedPreferences.edit().remove("user_type").apply();
            Toast.makeText(NGODashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        loadFoodItems();
    }
    private void loadFoodItems() {
        db.collection("food_items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        foodItemList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            FoodItem foodItem = doc.toObject(FoodItem.class);
                            foodItemList.add(foodItem);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error loading food items", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}