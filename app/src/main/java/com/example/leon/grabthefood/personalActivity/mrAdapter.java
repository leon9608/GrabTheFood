package com.example.leon.grabthefood.personalActivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.leon.grabthefood.R;
import com.example.leon.grabthefood.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by leon on 4/29/17.
 */

class mrAdapter extends RecyclerView.Adapter<mrAdapter.ViewHolder> {
    private ArrayList<Request> requests;
    private ArrayList<String> requestItemsID;

    public mrAdapter(ArrayList<Request> requests, ArrayList<String> requestItemsID) {
        this.requests = requests;
        this.requestItemsID = requestItemsID;
    }

    @Override
    public mrAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        final View mrrequestItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.myrequest_item, parent, false);
        return new mrAdapter.ViewHolder(mrrequestItem);
    }


    public void onBindViewHolder(final mrAdapter.ViewHolder holder, final int position) {
        final Request request = requests.get(position);

        holder.foodName.setText(request.displayFood());
        holder.restaurantNameAddrsText.setText(request.displayResInfo());
        holder.priceText.setText(request.displayPrice());
        holder.deliveryAddrsText.setText(request.displayRequesterInfo());
        holder.remarkText.setText(request.displayRemark());
        holder.statusText.setText(request.displayStatus());
        if (request.getStatus().equalsIgnoreCase("Picked")) {
            holder.cancel.setText("Received");
            holder.providerInfo.setText(request.displayPickUpInfo());
        } else {
            holder.providerInfo.setVisibility(View.INVISIBLE);
            holder.cancel.setText("Cancel");
        }
        if (request.getStatus().equalsIgnoreCase("Cancelled")
                || request.getStatus().equalsIgnoreCase("Delivered")) {
            holder.cancel.setVisibility(View.INVISIBLE);
        }
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference openPlatformReferecnce = firebaseDatabase.
                        getReference("Openplatform");
                String requestItemID = requestItemsID.get(position);
                String currentID = mAuth.getCurrentUser().getUid();
                DatabaseReference currentUserRef = firebaseDatabase.
                        getReference(currentID + "/MyRequest");
                if (request.getStatus().equalsIgnoreCase("Available")) {
                    openPlatformReferecnce.child(requestItemID).child("status").
                            setValue("Cancelled");
                    currentUserRef.child(requestItemID).child("status").setValue("Cancelled");
                    holder.statusText.setText(R.string.cancelled_status);
                    holder.cancel.setVisibility(View.INVISIBLE);
                } else {
                    openPlatformReferecnce.child(requestItemID).child("status").
                            setValue("Delivered");
                    currentUserRef.child(requestItemID).child("status").setValue("Delivered");
                    firebaseDatabase.getReference(request.getPickUpID() + "/MyPickUp/" +
                            requestItemID).child("status").setValue("Delivered");
                    holder.cancel.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName;
        private TextView restaurantNameAddrsText;
        private TextView priceText;
        private TextView deliveryAddrsText;
        private TextView remarkText;
        private TextView statusText;
        private TextView providerInfo;
        private Button cancel;


        public ViewHolder(View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.mrfood);
            priceText = (TextView) itemView.findViewById(R.id.mrprice);
            restaurantNameAddrsText = (TextView) itemView.findViewById(R.id.mrname_address);
            deliveryAddrsText = (TextView) itemView.findViewById(R.id.mrdelivery);
            remarkText = (TextView) itemView.findViewById(R.id.mradditions);
            statusText = (TextView) itemView.findViewById(R.id.mrstatus);
            providerInfo = (TextView) itemView.findViewById(R.id.mrprovider_info);
            cancel = (Button) itemView.findViewById(R.id.mrdelete_button);

        }
    }
}