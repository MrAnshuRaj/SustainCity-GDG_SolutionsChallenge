package com.anshu.sustaincity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CleaningEventsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CleaningEventsAdapter adapter;
    private List<CleaningEvent> eventList;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cleaning_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        eventList = new ArrayList<>();
        adapter = new CleaningEventsAdapter(eventList, this);
        recyclerView.setAdapter(adapter);

        fetchCleaningEvents();
    }
    private void fetchCleaningEvents() {
        db.collection("cleaning_events").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                eventList.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    CleaningEvent event = doc.toObject(CleaningEvent.class);
                    event.setId(doc.getId()); // Store document ID
                    eventList.add(event);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show();
                Log.e("CleaningEvents", "Error: " + task.getException());
            }
        });
    }
}