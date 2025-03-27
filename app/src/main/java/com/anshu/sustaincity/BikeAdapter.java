package com.anshu.sustaincity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.BikeViewHolder> {
    private Context context;
    private List<Bike> bikeList;

    public BikeAdapter(Context context, List<Bike> bikeList) {
        this.context = context;
        this.bikeList = bikeList;
    }

    @NonNull
    @Override
    public BikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bike, parent, false);
        return new BikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeViewHolder holder, int position) {
        Bike bike = bikeList.get(position);
        holder.tvBikeName.setText(bike.getName());
        holder.tvBikeType.setText("Type: " + bike.getType());
        holder.tvBikePrice.setText("₹ " + bike.getPrice() + "/hour");

        // Load image using Glide
        Glide.with(context).load(bike.getImageUrl()).into(holder.ivBikeImage);
    }

    @Override
    public int getItemCount() {
        return bikeList.size();
    }

    public static class BikeViewHolder extends RecyclerView.ViewHolder {
        TextView tvBikeName, tvBikeType, tvBikePrice;
        ImageView ivBikeImage;

        public BikeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBikeName = itemView.findViewById(R.id.tvBikeName);
            tvBikeType = itemView.findViewById(R.id.tvBikeType);
            tvBikePrice = itemView.findViewById(R.id.tvBikePrice);
            ivBikeImage = itemView.findViewById(R.id.ivBikeImage);
        }
    }
}
