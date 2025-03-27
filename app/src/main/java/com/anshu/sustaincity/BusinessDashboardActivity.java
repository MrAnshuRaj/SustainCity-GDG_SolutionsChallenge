package com.anshu.sustaincity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BusinessDashboardActivity extends AppCompatActivity {
    private EditText etBikeName, etBikeType, etBikePrice;
    private Button btnUploadImage, btnSubmitBike,btnLogOutBusiness;
    private ImageView ivBikeImage;
    private RecyclerView rvBikeList;
    private Uri imageUri;
    private FirebaseFirestore db;
    private ArrayList<Bike> bikeList;
    private BikeAdapter bikeAdapter;
    FirebaseAuth mAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_business_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
// Initialize UI Components
        etBikeName = findViewById(R.id.etBikeName);
        etBikeType = findViewById(R.id.etBikeType);
        etBikePrice = findViewById(R.id.etBikePrice);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmitBike = findViewById(R.id.btnSubmitBike);
        ivBikeImage = findViewById(R.id.ivBikeImage);
        rvBikeList = findViewById(R.id.rvBikeList);
        btnLogOutBusiness = findViewById(R.id.btnLogOutBusiness);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        bikeList = new ArrayList<>();
        bikeAdapter = new BikeAdapter(this, bikeList);
        rvBikeList.setLayoutManager(new LinearLayoutManager(this));
        rvBikeList.setAdapter(bikeAdapter);

        btnUploadImage.setOnClickListener(v -> openFileChooser());

        btnSubmitBike.setOnClickListener(v -> submitBike());
        btnLogOutBusiness.setOnClickListener(v -> {
            mAuth.signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("SustainCity", MODE_PRIVATE);
            sharedPreferences.edit().remove("user_type").apply();
            Toast.makeText(BusinessDashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        fetchBikes();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivBikeImage.setImageURI(imageUri);
            ivBikeImage.setVisibility(View.VISIBLE);
        }
    }

    private void submitBike() {
        String bikeName = etBikeName.getText().toString().trim();
        String bikeType = etBikeType.getText().toString().trim();
        String bikePrice = etBikePrice.getText().toString().trim();

        if (bikeName.isEmpty() || bikeType.isEmpty() || bikePrice.isEmpty() || imageUri == null) {
            Toast.makeText(this, "All fields and image are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Store Data in Firebase
        Map<String, Object> bike = new HashMap<>();
        bike.put("name", bikeName);
        bike.put("type", bikeType);
        bike.put("price", bikePrice);
        bike.put("imageUrl", imageUri.toString());

        db.collection("rental_bikes").add(bike)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Bike Submitted for Rental!", Toast.LENGTH_SHORT).show();
                    etBikeName.setText("");
                    etBikeType.setText("");
                    etBikePrice.setText("");
                    ivBikeImage.setVisibility(View.GONE);
                    fetchBikes(); // Refresh list
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Error: " + e.getMessage()));
    }

    private void fetchBikes() {
        db.collection("rental_bikes").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bikeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Bike bike = document.toObject(Bike.class);
                            bikeList.add(bike);
                        }
                        bikeAdapter.notifyDataSetChanged();
                    }
                });
    }
}