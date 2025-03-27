package com.anshu.sustaincity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FileComplaintActivity extends AppCompatActivity {
    private static final int MAP_REQUEST_CODE = 50;
    private EditText etComplaintTitle, etComplaintDescription;
    private TextView tvSelectedLocation;
    private ImageView ivComplaintPhoto;
    private Button btnPickLocation, btnUploadPhoto, btnSubmitComplaint;

    private Uri imageUri;
    private double latitude, longitude;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int LOCATION_REQUEST = 2;
    private static final String IMGBB_API_KEY = "99dcb8a62337dc33ea8312bf0b6211a9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_file_complaint);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etComplaintTitle = findViewById(R.id.etComplaintTitle);
        etComplaintDescription = findViewById(R.id.etComplaintDescription);
        tvSelectedLocation = findViewById(R.id.tvSelectedLocation);
        ivComplaintPhoto = findViewById(R.id.ivComplaintPhoto);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSubmitComplaint = findViewById(R.id.btnSubmitComplaint);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        btnPickLocation.setOnClickListener(view -> openMap());

        btnUploadPhoto.setOnClickListener(view -> openGallery());

        btnSubmitComplaint.setOnClickListener(view -> uploadComplaint());
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openMap() {
        Intent intent = new Intent(FileComplaintActivity.this, MapActivity.class);
        startActivityForResult(intent, MAP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivComplaintPhoto.setImageURI(imageUri);
        }
        if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            tvSelectedLocation.setText("Lat: " + latitude + ", Lon: " + longitude);
        }
    }

    private void uploadComplaint() {
        String title = etComplaintTitle.getText().toString().trim();
        String description = etComplaintDescription.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        new Thread(() -> {
            try {
                String imageUrl = uploadImageToImgBB(imageUri);
                if (imageUrl != null) {
                    saveComplaintToFirestore(title, description, imageUrl);
                } else {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(FileComplaintActivity.this, "Image upload failed!", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(FileComplaintActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private String uploadImageToImgBB(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            byte[] imageBytes = outputStream.toByteArray();
            inputStream.close();
            outputStream.close();

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("key", IMGBB_API_KEY)
                    .addFormDataPart("image", "complaint.jpg",
                            RequestBody.create(MediaType.parse("image/*"), imageBytes))
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            JSONObject jsonObject = new JSONObject(responseBody);

            if (jsonObject.has("data")) {
                return jsonObject.getJSONObject("data").getString("url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveComplaintToFirestore(String title, String description, String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();
        String complaintId = UUID.randomUUID().toString();

        Map<String, Object> complaintData = new HashMap<>();
        complaintData.put("id", complaintId);
        complaintData.put("userId", userId);
        complaintData.put("title", title);
        complaintData.put("description", description);
        complaintData.put("latitude", latitude);
        complaintData.put("longitude", longitude);
        complaintData.put("imageUrl", imageUrl);

        db.collection("complaints")
                .document(complaintId)
                .set(complaintData)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(FileComplaintActivity.this, "Complaint submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(FileComplaintActivity.this, "Error saving complaint!", Toast.LENGTH_SHORT).show();
                });
    }
}