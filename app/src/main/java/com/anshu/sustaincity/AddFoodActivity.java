package com.anshu.sustaincity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddFoodActivity extends AppCompatActivity {
    private EditText etFoodName, etFoodQuantity, etFoodExpiry;
    private Button btnAddFood;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();

        // Bind UI components
        etFoodName = findViewById(R.id.et_food_name);
        etFoodQuantity = findViewById(R.id.et_food_quantity);
        etFoodExpiry = findViewById(R.id.et_food_expiry);
        btnAddFood = findViewById(R.id.btn_add_food);

        // Set Click Listener
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFoodToFirestore();
            }
        });

    }
    private void addFoodToFirestore() {
        String foodName = etFoodName.getText().toString().trim();
        String quantity = etFoodQuantity.getText().toString().trim();
        String expiryDate = etFoodExpiry.getText().toString().trim();

        if (TextUtils.isEmpty(foodName) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(expiryDate)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> food = new HashMap<>();
        food.put("name", foodName);
        food.put("quantity", quantity);
        food.put("expiryDate", expiryDate);

        db.collection("food_items")
                .add(food)
                .addOnSuccessListener(documentReference ->{
                        Toast.makeText(AddFoodActivity.this, "Food Added Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddFoodActivity.this,NGODashboardActivity.class));
                        finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(AddFoodActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}