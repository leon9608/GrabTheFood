package com.example.leon.grabthefood.commonActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.leon.grabthefood.R;
import com.example.leon.grabthefood.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by leon on 4/28/17.
 */

class requestAdapter extends RecyclerView.Adapter<requestAdapter.ViewHolder> {

    private ArrayList<Request> requests;
    private ArrayList<String> requestItemsID;
    private Context context;

    public requestAdapter(ArrayList<Request> requests, ArrayList<String> requestsID,
                          Context context) {
        this.requests = requests;
        this.requestItemsID = requestsID;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        final View allrequestItem = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.allrequest_item, parent, false);
        return new ViewHolder(allrequestItem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Request request = requests.get(position);

        holder.foodName.setText(request.displayFood());
        holder.restaurantNameAddrsText.setText(request.displayResInfo());
        holder.priceText.setText(request.displayPrice());
        holder.deliveryAddrsText.setText(request.displayRequesterInfo());
        holder.statusText.setText(request.displayStatus());
        holder.remarkText.setText(request.displayRemark());

        if (request.getStatus().equalsIgnoreCase("Available")) {
            holder.pickup.setVisibility(View.VISIBLE);
        } else {
            holder.pickup.setVisibility(View.INVISIBLE);
        }

        holder.pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage(R.string.pickUp_dialog_info);

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(input);

                alert.setPositiveButton("Yes, I want to PICK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String contactInfo = input.getText().toString();
                        if (contactInfo.isEmpty() || contactInfo.length() != 10) {
                            Toast.makeText(context, R.string.pickUp_failure,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            updateData(contactInfo, position, holder, request);
                            holder.pickup.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();

            }
        });
    }

    private void updateData(String contactInfo, int position, ViewHolder holder, Request request) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference openPlatformReferecnce = firebaseDatabase.getReference("Openplatform");

        String currentID = mAuth.getCurrentUser().getUid();
        String requestItemID = requestItemsID.get(position);
        String requesterID = requestItemID.substring(0, requestItemID.length() - 1);

        openPlatformReferecnce.child(requestItemID).child("status").setValue("Picked");
        holder.statusText.setText(R.string.picked_status);
        firebaseDatabase.getReference(currentID).child("MyPickUp").
                child(requestItemID).setValue(request);
        firebaseDatabase.getReference(currentID).child("MyPickUp").
                child(requestItemID).child("status").setValue("Picked");
        firebaseDatabase.getReference(requesterID).child("MyRequest").
                child(requestItemID).child("status").setValue("Picked");
        firebaseDatabase.getReference(requesterID).child("MyRequest").
                child(requestItemID).child("pickUpID").setValue(currentID);
        firebaseDatabase.getReference(requesterID).child("MyRequest").
                child(requestItemID).child("pickUpInfo").setValue(contactInfo);
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
        private TextView statusText;
        private TextView remarkText;
        private Button pickup;

        ViewHolder(View itemView) {
            super(itemView);
            foodName = (TextView) itemView.findViewById(R.id.allfood);
            priceText = (TextView) itemView.findViewById(R.id.allprice);
            restaurantNameAddrsText = (TextView) itemView.findViewById(R.id.allname_address);
            deliveryAddrsText = (TextView) itemView.findViewById(R.id.alldelivery);
            statusText = (TextView) itemView.findViewById(R.id.allstatus);
            remarkText = (TextView) itemView.findViewById(R.id.alladditions);
            pickup = (Button) itemView.findViewById(R.id.allpickup_button);
        }
    }
}
