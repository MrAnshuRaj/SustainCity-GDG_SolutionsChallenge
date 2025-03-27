package com.anshu.sustaincity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FoodDonationActivity extends AppCompatActivity {
    private EditText etFoodName, etQuantity, etExpiryDate;
    private TextView tvSelectedLocation;
    private ImageView ivFoodImage;
    private Button btnUploadImage, btnSelectLocation, btnSubmitFoodDonation;
    private Uri imageUri;
    private String imageUrl;
    private double latitude, longitude;
    private FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LOCATION_REQUEST_CODE = 2;
    private static final String IMGBB_API_KEY = "99dcb8a62337dc33ea8312bf0b6211a9"; // Replace with your API key
    private static final String IMGBB_UPLOAD_URL = "https://api.imgbb.com/1/upload";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_donation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etFoodName = findViewById(R.id.etFoodName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        tvSelectedLocation = findViewById(R.id.tvSelectedLocation);
        ivFoodImage = findViewById(R.id.ivFoodImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        btnSubmitFoodDonation = findViewById(R.id.btnSubmitFoodDonation);

        db = FirebaseFirestore.getInstance();

        btnUploadImage.setOnClickListener(v -> openFileChooser());
        btnSelectLocation.setOnClickListener(v -> openMapActivity());
        btnSubmitFoodDonation.setOnClickListener(v -> submitFoodDonation());
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivityForResult(intent, LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivFoodImage.setImageBitmap(bitmap);
                ivFoodImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == LOCATION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            tvSelectedLocation.setText("Lat: " + latitude + ", Lng: " + longitude);
        }
    }

    private void submitFoodDonation() {
        String foodName = etFoodName.getText().toString();
        String quantity = etQuantity.getText().toString();
        String expiryDate = etExpiryDate.getText().toString();

        if (foodName.isEmpty() || quantity.isEmpty() || expiryDate.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload Image to ImageBB
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            uploadImageToImgBB(bitmap, foodName, quantity, expiryDate);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToImgBB(Bitmap bitmap, String foodName, String quantity, String expiryDate) {
        // Convert Bitmap to Base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        // Prepare request body
        RequestBody requestBody = new FormBody.Builder()
                .add("key", IMGBB_API_KEY)
                .add("image", encodedImage)
                .build();

        // Make HTTP Request
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(IMGBB_UPLOAD_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(FoodDonationActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(FoodDonationActivity.this, "Upload error", Toast.LENGTH_SHORT).show());
                    return;
                }

                String responseData = response.body().string();
                Log.d("ImgBB Response", responseData);

                // Extract Image URL
                imageUrl = extractImageUrl(responseData);
                runOnUiThread(() -> saveFoodDonation(foodName, quantity, expiryDate, imageUrl, latitude, longitude));
            }
        });
    }

    private String extractImageUrl(String responseData) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(responseData);
            return jsonObject.getJSONObject("data").getString("url");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveFoodDonation(String foodName, String quantity, String expiryDate, String imageUrl, double lat, double lng) {
        Map<String, Object> donation = new HashMap<>();
        donation.put("foodName", foodName);
        donation.put("quantity", quantity);
        donation.put("expiryDate", expiryDate);
        donation.put("imageUrl", imageUrl);
        donation.put("latitude", lat);
        donation.put("longitude", lng);

        db.collection("food_donations").add(donation)
                .addOnSuccessListener(docRef -> runOnUiThread(() ->{
                        Toast.makeText(this, "Donation Submitted!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(FoodDonationActivity.this,CitizenDashboardActivity.class));
                })
                )
                .addOnFailureListener(e -> Log.e("FoodDonation", "Error: " + e.getMessage()));
    }
}