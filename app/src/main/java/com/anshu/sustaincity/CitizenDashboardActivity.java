package com.anshu.sustaincity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CitizenDashboardActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private Button btnFileComplaint, btnViewEvents, btnDonateFood, btnReadBlogs, btnLogout,btnBikeRental;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citizen_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI Elements
        tvWelcome = findViewById(R.id.tvWelcome);
        btnFileComplaint = findViewById(R.id.btnFileComplaint);
        btnViewEvents = findViewById(R.id.btnViewEvents);
        btnDonateFood = findViewById(R.id.btnDonateFood);
        btnReadBlogs = findViewById(R.id.btnReadBlogs);
        btnLogout = findViewById(R.id.btnLogout);

        // Fetch and display user name
        fetchUserName();

        // Set Click Listeners
        btnFileComplaint.setOnClickListener(view -> startActivity(new Intent(this, FileComplaintActivity.class)));
        btnViewEvents.setOnClickListener(view -> startActivity(new Intent(this, CleaningEventsActivity.class)));
        btnDonateFood.setOnClickListener(view -> startActivity(new Intent(this, FoodDonationActivity.class)));
        btnReadBlogs.setOnClickListener(view -> startActivity(new Intent(this, BlogsActivity.class)));

        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("SustainCity", MODE_PRIVATE);
            sharedPreferences.edit().remove("user_type").apply();
            Toast.makeText(CitizenDashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        btnBikeRental =findViewById(R.id.btnBikeRental);
        btnBikeRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CitizenDashboardActivity.this, CitizenBikeRental.class));
            }
        });
    }
    private void fetchUserName() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                tvWelcome.setText("Welcome, " + name + "!");
                            }
                        }
                    });
        }
    }
}