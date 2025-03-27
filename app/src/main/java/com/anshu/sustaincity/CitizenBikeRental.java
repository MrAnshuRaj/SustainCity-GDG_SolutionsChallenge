package com.anshu.sustaincity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CitizenBikeRental extends AppCompatActivity {
    private RecyclerView rvCitizenBikeList;
    private CitizenBikeAdapter bikeAdapter;
    private List<Bike> bikeList;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_citizen_bike_rental);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rvCitizenBikeList = findViewById(R.id.rvCitizenBikeList);
        rvCitizenBikeList.setLayoutManager(new LinearLayoutManager(this));

        bikeList = new ArrayList<>();
        bikeAdapter = new CitizenBikeAdapter(this, bikeList);
        rvCitizenBikeList.setAdapter(bikeAdapter);

        db = FirebaseFirestore.getInstance();
        fetchBikes();
    }

    private void fetchBikes() {
        CollectionReference bikesRef = db.collection("rental_bikes");
        bikesRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firebase", "Error fetching bikes", error);
                return;
            }
            bikeList.clear();
            if (value != null) {
                for (var doc : value.getDocuments()) {
                    Bike bike = doc.toObject(Bike.class);
                    bikeList.add(bike);
                }
                bikeAdapter.notifyDataSetChanged();
            }
        });
    }
}