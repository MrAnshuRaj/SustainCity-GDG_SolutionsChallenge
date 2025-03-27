package com.anshu.sustaincity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ManageCleaningEventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnAddEvent;
    private FirebaseFirestore db;
    private List<CleaningEvent> eventList;
    private CleaningEventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_cleaning_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Bind UI elements
        recyclerView = findViewById(R.id.recycler_cleaning_events);
        btnAddEvent = findViewById(R.id.btn_add_event);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new CleaningEventsAdapter( eventList,this);
        recyclerView.setAdapter(adapter);

        // Load Events
        loadCleaningEvents();

        // Add Event Button Click
        btnAddEvent.setOnClickListener(v -> {
            Intent intent = new Intent(ManageCleaningEventsActivity.this, AddCleaningActivity.class);
            startActivity(intent);
        });
    }

    private void loadCleaningEvents() {
        db.collection("cleaning_events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CleaningEvent event = document.toObject(CleaningEvent.class);
                        event.setId(document.getId()); // Store document ID
                        eventList.add(event);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ManageCleaningEventsActivity.this, "Error loading events!", Toast.LENGTH_SHORT).show());
    }
}
