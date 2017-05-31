package com.czterysery.hop.drone.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.czterysery.hop.drone.Models.MyDrone;
import com.czterysery.hop.drone.R;
import com.czterysery.hop.drone.ReadDroneActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tmax0 on 15.05.2017.
 */

public class MyDroneAdapter extends RecyclerView.Adapter<MyDroneAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<MyDrone> drones = new ArrayList<>();

    public MyDroneAdapter(Activity activity, ArrayList<MyDrone> drones) {
        this.activity = activity;
        this.drones = drones;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom card layout
        View contactView = activity.getLayoutInflater()
                .inflate(R.layout.card_mydrone, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.headerView.setText(drones.get(position).getName());
        holder.descriptionView.setText(drones.get(position).getDescription());
        // TODO: 15.05.2017 Improve picasso options
        Picasso.with(activity).load(drones.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return drones.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_mydrone_description)
        TextView descriptionView;
        @BindView(R.id.card_mydrone_header)
        TextView headerView;
        @BindView(R.id.card_mydrone_imageview)
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_mydrone_cardview)
        void onCardClick(){
            int position = getAdapterPosition();
            Intent readDroneActivity = new Intent(activity, ReadDroneActivity.class);
            readDroneActivity.putExtra("droneName", drones.get(position).getName());
            activity.startActivity(readDroneActivity);
        }

    }
}
