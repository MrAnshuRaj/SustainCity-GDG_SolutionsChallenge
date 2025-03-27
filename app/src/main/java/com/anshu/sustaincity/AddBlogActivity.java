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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.*;

public class AddBlogActivity extends AppCompatActivity {
    private EditText etTitle, etContent;
    private ImageView ivBlogImage;
    private Button btnUploadImage, btnSubmitBlog;
    private Uri imageUri;
    private String imageUrl;
    private FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String IMGBB_API_KEY = "99dcb8a62337dc33ea8312bf0b6211a9"; // Replace with your ImageBB API Key
    private static final String IMGBB_UPLOAD_URL = "https://api.imgbb.com/1/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_blog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        ivBlogImage = findViewById(R.id.ivBlogImage);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSubmitBlog = findViewById(R.id.btnSubmitBlog);

        db = FirebaseFirestore.getInstance();

        btnUploadImage.setOnClickListener(v -> openFileChooser());
        btnSubmitBlog.setOnClickListener(v -> submitBlog());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivBlogImage.setImageBitmap(bitmap);
                ivBlogImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submitBlog() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();

        if (title.isEmpty() || content.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            uploadImageToImgBB(bitmap, title, content);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToImgBB(Bitmap bitmap, String title, String content) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        RequestBody requestBody = new FormBody.Builder()
                .add("key", IMGBB_API_KEY)
                .add("image", encodedImage)
                .build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(IMGBB_UPLOAD_URL).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(AddBlogActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(AddBlogActivity.this, "Upload error", Toast.LENGTH_SHORT).show());
                    return;
                }

                String responseData = response.body().string();
                imageUrl = extractImageUrl(responseData);

                runOnUiThread(() -> saveBlog(title, content, imageUrl));
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

    private void saveBlog(String title, String content, String imageUrl) {
        Map<String, Object> blog = new HashMap<>();
        blog.put("title", title);
        blog.put("content", content);
        blog.put("imageUrl", imageUrl);

        db.collection("blogs").add(blog)
                .addOnSuccessListener(docRef -> runOnUiThread(() ->{
                        Toast.makeText(this, "Blog Published!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(AddBlogActivity.this,BlogsActivity.class));
                    finish();
                }))
                .addOnFailureListener(e -> Log.e("Blog", "Error: " + e.getMessage()));
    }

}
