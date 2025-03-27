package com.anshu.sustaincity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anshu.sustaincity.FoodDonation;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class FoodDistributionAdapter extends RecyclerView.Adapter<FoodDistributionAdapter.ViewHolder> {
    private Context context;
    private List<FoodDonation> foodList;
    private FirebaseFirestore db;

    public FoodDistributionAdapter(Context context, List<FoodDonation> foodList) {
        this.context = context;
        this.foodList = foodList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.food_distribution_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDonation food = foodList.get(position);

        holder.foodName.setText(food.getFoodName());
        holder.foodQuantity.setText("Quantity: " + food.getQuantity());
        holder.foodExpiry.setText("Expires on: " + food.getExpiryDate());
        holder.foodLocation.setText("Pickup Location: " + food.getLatitude() + ", " + food.getLongitude());

        Glide.with(context).load(food.getImageUrl()).into(holder.foodImage);

        holder.btnNavigate.setOnClickListener(v -> {
            Intent intent = new Intent(context, MapActivity.class);
            intent.putExtra("latitude", food.getLatitude());
            intent.putExtra("longitude", food.getLongitude());
            context.startActivity(intent);
        });

        holder.btnPickup.setOnClickListener(v -> markAsPickedUp(food.getFoodId()));
    }

    private void markAsPickedUp(String foodId) {
        db.collection("food_donations").document(foodId)
                .update("status", "Picked Up")
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(context, "Food Picked Up!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error updating status!", Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodQuantity, foodLocation, foodExpiry;
        Button btnNavigate, btnPickup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.ivFoodImage);
            foodName = itemView.findViewById(R.id.tvFoodName);
            foodQuantity = itemView.findViewById(R.id.tvFoodQuantity);
            foodLocation = itemView.findViewById(R.id.tvFoodLocation);
            foodExpiry = itemView.findViewById(R.id.tvFoodExpiry);
            btnNavigate = itemView.findViewById(R.id.btnNavigate);
            btnPickup = itemView.findViewById(R.id.btnPickup);
        }
    }
}
