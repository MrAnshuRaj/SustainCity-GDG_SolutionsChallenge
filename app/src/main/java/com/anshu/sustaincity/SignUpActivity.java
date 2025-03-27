package com.anshu.sustaincity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword,etCity;
    private Spinner spinnerRole;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Button btnSignup;
    private TextView tvLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences= getSharedPreferences("SustainCity",MODE_PRIVATE);
        editor= sharedPreferences.edit();
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String role = sharedPreferences.getString("role", "");
            if(!role.isEmpty()) {
                navigateToDashboard(role);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Bind UI Elements
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignup = findViewById(R.id.btnSignup);
        etCity = findViewById(R.id.etCity);
        spinnerRole = findViewById(R.id.spinnerRole);
        tvLogin = findViewById(R.id.tvLogin);

        String[] roles = {"Citizen", "Volunteer", "NGO", "Business"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinnerRole.setAdapter(adapter);
        // Sign Up Button Click
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Redirect to Login
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();
        editor.putString("role",role);
        editor.apply();
        // Validate Inputs
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (TextUtils.isEmpty(city)) {
            etCity.setError("City is required");
            return;
        }

        // Register User with Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserData(user.getUid(), name, email, city, role);
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void saveUserData(String userId, String name, String email, String city, String role) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("city", city);
        user.put("role", role);

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUpActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                    navigateToDashboard(role);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(SignUpActivity.this, "Error saving user: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
    private void navigateToDashboard(String role) {
        Intent intent;
        switch (role) {
            case "Citizen":
                intent = new Intent(this, CitizenDashboardActivity.class);
                break;
            case "Volunteer":
                intent = new Intent(this, VolunteerDashboardActivity.class);
                break;
            case "NGO":
                intent = new Intent(this, NGODashboardActivity.class);
                break;
            case "Business":
                intent = new Intent(this, BusinessDashboardActivity.class);
                break;
            default:
                intent = new Intent(this, CitizenDashboardActivity.class);
        }
        startActivity(intent);
        finish();
    }
}

