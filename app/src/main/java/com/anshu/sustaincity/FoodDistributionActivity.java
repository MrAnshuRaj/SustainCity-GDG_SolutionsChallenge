package com.anshu.sustaincity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FoodDistributionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodDistributionAdapter adapter;
    private List<FoodDonation> centerList;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_distribution);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerView_food_distribution);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        centerList = new ArrayList<>();
        adapter = new FoodDistributionAdapter(this, centerList);
        recyclerView.setAdapter(adapter);

        loadFoodDistributionCenters();
    }
    private void loadFoodDistributionCenters() {
        db.collection("food_donations")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        centerList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            FoodDonation center = doc.toObject(FoodDonation.class);
                            centerList.add(center);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error loading centers", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}