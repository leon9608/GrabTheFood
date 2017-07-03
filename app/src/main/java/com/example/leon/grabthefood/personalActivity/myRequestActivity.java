package com.example.leon.grabthefood.personalActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.leon.grabthefood.R;
import com.example.leon.grabthefood.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class myRequestActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Request> allRequests = new ArrayList<>();
    private ArrayList<String> requestItemsID = new ArrayList<>();
    private mrAdapter myRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        setTitle("My Requests");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView3);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String currentID = mAuth.getCurrentUser().getUid();
        DatabaseReference userReference = firebaseDatabase.getReference(currentID);
        userReference.child("MyRequest").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                allRequests.add(dataSnapshot.getValue(Request.class));
                requestItemsID.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int index = requestItemsID.indexOf(dataSnapshot.getKey());
                allRequests.set(index, dataSnapshot.getValue(Request.class));
                myRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRequestAdapter = new mrAdapter(allRequests, requestItemsID);
        mRecyclerView.setAdapter(myRequestAdapter);
    }
}
