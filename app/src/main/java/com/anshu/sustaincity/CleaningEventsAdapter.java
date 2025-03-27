package com.anshu.sustaincity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class CleaningEventsAdapter extends RecyclerView.Adapter<CleaningEventsAdapter.EventViewHolder> {
    private List<CleaningEvent> events;
    private Context context;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CleaningEventsAdapter(List<CleaningEvent> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cleaning_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CleaningEvent event = events.get(position);
        holder.tvTitle.setText(event.getTitle());
        holder.tvDescription.setText(event.getDescription());
        holder.tvDate.setText("Date: " + event.getDate());

        holder.btnJoin.setOnClickListener(v -> joinEvent(event));
    }

    private void joinEvent(CleaningEvent event) {
        String userId = "USER_ID"; // Replace with actual user ID
        db.collection("cleaning_events").document(event.getId())
                .collection("participants").document(userId)
                .set(new Participant(userId))
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Joined Event!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error joining event", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDate;
        Button btnJoin;

        EventViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvDescription = itemView.findViewById(R.id.tvEventDescription);
            tvDate = itemView.findViewById(R.id.tvEventDate);
            btnJoin = itemView.findViewById(R.id.btnJoinEvent);
        }
    }
}
