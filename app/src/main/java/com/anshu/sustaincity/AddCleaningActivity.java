package com.anshu.sustaincity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddCleaningActivity extends AppCompatActivity {
    private EditText etEventName, etEventDate, etEventLocation;
    private Button btnCreateEvent;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_cleaning);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = FirebaseFirestore.getInstance();

        // Bind UI components
        etEventName = findViewById(R.id.et_event_name);
        etEventDate = findViewById(R.id.et_event_date);
        etEventLocation = findViewById(R.id.et_event_location);
        btnCreateEvent = findViewById(R.id.btn_create_event);

        // Create Event Button Click
        btnCreateEvent.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        String name = etEventName.getText().toString().trim();
        String date = etEventDate.getText().toString().trim();
        String location = etEventLocation.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || TextUtils.isEmpty(location)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("name", name);
        event.put("date", date);
        event.put("location", location);

        db.collection("cleaning_events")
                .add(event)
                .addOnSuccessListener(documentReference ->{
                        Toast.makeText(AddCleaningActivity.this, "Event Created Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddCleaningActivity.this,NGODashboardActivity.class));
                        finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(AddCleaningActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}