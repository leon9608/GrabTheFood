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
class mpAdapter extends RecyclerView.Adapter<mpAdapter.ViewHolder> {
    private ArrayList<Request> requests;
    private ArrayList<String> requestItemsID;

    mpAdapter(ArrayList<Request> requests, ArrayList<String> requestItemsID) {
        this.requests = requests;
        this.requestItemsID = requestItemsID;
    }

    @Override
    public mpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        final View mprequestItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.mypickups_item, parent, false);
        return new ViewHolder(mprequestItem);
    }


    public void onBindViewHolder(final mpAdapter.ViewHolder holder, final int position) {
        final Request request = requests.get(position);

        holder.foodName.setText(request.displayFood());
        holder.restaurantNameAddrsText.setText(request.displayResInfo());
        holder.priceText.setText(request.displayPrice());
        holder.deliveryAddrsText.setText(request.displayRequesterInfo());
        holder.remarkText.setText(request.displayRemark());
        if (request.getStatus().equalsIgnoreCase("Delivered")) {
            holder.drop.setVisibility(View.INVISIBLE);
        }
        holder.drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference openPlatformReferecnce = firebaseDatabase.
                        getReference("Openplatform");
                String requestItemID = requestItemsID.get(position);
                String currentID = mAuth.getCurrentUser().getUid();
                String requesterID = requestItemID.substring(0, requestItemID.length() - 1);
                openPlatformReferecnce.child(requestItemID).child("status").setValue("Available");
                firebaseDatabase.getReference(currentID).child("MyPickUp").
                        child(requestItemID).removeValue();
                firebaseDatabase.getReference(requesterID).child("MyRequest").
                        child(requestItemID).child("status").setValue("Available");
                firebaseDatabase.getReference(requesterID).child("MyRequest").
                        child(requestItemID).child("pickUpInfo").setValue("");
                firebaseDatabase.getReference(requesterID).child("MyRequest").
                        child(requestItemID).child("pickUpID").setValue("");
                holder.remarkText.setText(R.string.drop_success);
                holder.drop.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName;
        private TextView restaurantNameAddrsText;
        private TextView priceText;
        private TextView deliveryAddrsText;
        private TextView remarkText;
        private Button drop;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.mpfood);
            priceText = (TextView) itemView.findViewById(R.id.mpprice);
            restaurantNameAddrsText = (TextView) itemView.findViewById(R.id.mpname_address);
            deliveryAddrsText = (TextView) itemView.findViewById(R.id.mpdelivery);
            remarkText = (TextView) itemView.findViewById(R.id.mpadditions);
            drop = (Button) itemView.findViewById(R.id.mpdrop_button);

        }
    }
}
