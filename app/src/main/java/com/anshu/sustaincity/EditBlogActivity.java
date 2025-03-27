package com.anshu.sustaincity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditBlogActivity extends AppCompatActivity {
    private EditText etTitle, etContent;
    private ImageView ivBlogImage;
    private Button btnSave, btnChangeImage;
    private FirebaseFirestore db;
    private String blogId, currentImageUrl;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String IMGBB_API_KEY = "99dcb8a62337dc33ea8312bf0b6211a9";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_blog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etTitle = findViewById(R.id.etBlogTitle);
        etContent = findViewById(R.id.etBlogContent);
        ivBlogImage = findViewById(R.id.ivBlogImagePreview);
        btnSave = findViewById(R.id.btnSaveChanges);
        btnChangeImage = findViewById(R.id.btnChangeImage);

        db = FirebaseFirestore.getInstance();

        // Get Blog ID from intent
        blogId = getIntent().getStringExtra("blogId");

        if (blogId == null) {
            Toast.makeText(this, "Error loading blog", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBlogData();

        btnChangeImage.setOnClickListener(v -> selectImage());
        btnSave.setOnClickListener(v -> saveChanges());
    }
    private void loadBlogData() {
        db.collection("blogs").document(blogId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etTitle.setText(documentSnapshot.getString("title"));
                        etContent.setText(documentSnapshot.getString("content"));
                        currentImageUrl = documentSnapshot.getString("imageUrl");

                        if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                            Glide.with(this).load(currentImageUrl).into(ivBlogImage);
                        }
                    } else {
                        Toast.makeText(this, "Blog not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load blog", Toast.LENGTH_SHORT).show());
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ivBlogImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveChanges() {
        String updatedTitle = etTitle.getText().toString().trim();
        String updatedContent = etContent.getText().toString().trim();

        if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedImageUri != null) {
            uploadImageToImgBB(updatedTitle, updatedContent);
        } else {
            updateBlogInFirestore(updatedTitle, updatedContent, currentImageUrl);
        }
    }
    private void uploadImageToImgBB(String title, String content) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                // Convert Image to ByteArray
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                // OkHttp Client
                OkHttpClient client = new OkHttpClient();

                // Multipart Body (Correct way to send to ImgBB)
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("key", IMGBB_API_KEY)  // 🔹 API Key
                        .addFormDataPart("image", encodedImage) // 🔹 Image data
                        .build();

                Request request = new Request.Builder()
                        .url("https://api.imgbb.com/1/upload")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseString = response.body().string();
                    JSONObject responseJson = new JSONObject(responseString);
                    String imageUrl = responseJson.getJSONObject("data").getString("url");

                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        updateBlogInFirestore(title, content, imageUrl);
                    });
                } else {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditBlogActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditBlogActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void uploadImageToImgBB2(String title, String content) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Thread(() -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                OkHttpClient client = new OkHttpClient();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("image", encodedImage);

                RequestBody requestBody = RequestBody.create(
                        MediaType.parse("application/json"),
                        jsonObject.toString()
                );

                Request request = new Request.Builder()
                        .url("https://api.imgbb.com/1/upload?key=" + IMGBB_API_KEY)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                System.out.println(response.toString());
                if (response.isSuccessful() && response.body() != null) {
                    String responseString = response.body().string();
                    JSONObject responseJson = new JSONObject(responseString);
                    String imageUrl = responseJson.getJSONObject("data").getString("url");

                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        updateBlogInFirestore(title, content, imageUrl);
                    });
                } else {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditBlogActivity.this, "Image upload failed"+response.toString(), Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditBlogActivity.this, "Error uploading image"+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void updateBlogInFirestore(String title, String content, String imageUrl) {
        DocumentReference blogRef = db.collection("blogs").document(blogId);
        Map<String, Object> updatedBlog = new HashMap<>();
        updatedBlog.put("title", title);
        updatedBlog.put("content", content);
        updatedBlog.put("imageUrl", imageUrl);

        blogRef.update(updatedBlog)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Blog updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditBlogActivity.this,NGODashboardActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update blog", Toast.LENGTH_SHORT).show());
    }
}