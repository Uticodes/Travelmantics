package com.example.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Utibe Etim
 * 3rd/Aug/2019
 */
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealsViewHolder> {
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private ImageView imageDeal;

    public DealAdapter(){
       // FirebaseUtils.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtils.mFirebaseDataBase;
        mDatabaseReference = FirebaseUtils.mDataBaseReference;
        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");
        this.deals = FirebaseUtils.mDeals;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d( "Deal: ","" + td.getTitle());
                //Log.d("Deal:","Child not added");
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public DealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
        return new DealsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsViewHolder holder, int position) {
        /*TravelDeal deal = deals.get(position);
        holder.bind(deal);*/
        TravelDeal deal = deals.get(position);
        holder.tvTitle.setText(deal.getTitle());
        holder.tvDescription.setText(deal.getDescription());
        holder.tvPrice.setText(deal.getPrice());
        FirebaseUtils.connectStorage();
        holder.showImage(deal.getImageUrl());
    }

    @Override
    public int getItemCount() { return deals.size(); }

    public class DealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView tvTitle;
            TextView tvDescription;
            TextView tvPrice;
            ImageView imageDeal;
            Context context;
        public DealsViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvTitle =  itemView.findViewById(R.id.tvTitle);
            tvDescription =  itemView.findViewById(R.id.tvDescription);
            tvPrice =  itemView.findViewById(R.id.tvPrice);
            imageDeal = itemView.findViewById(R.id.imageDeal);
            itemView.setOnClickListener(this);
        }

        public void bind (TravelDeal deal){
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedDeal);
            view.getContext().startActivity(intent);
        }

        public void showImage (String url){
            if (url != null && !url.isEmpty()){
                Picasso.with(imageDeal.getContext())
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(imageDeal);
            }
        }
    }

}
