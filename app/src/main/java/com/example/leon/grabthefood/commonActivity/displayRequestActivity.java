package com.example.leon.grabthefood.commonActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.leon.grabthefood.R;
import com.example.leon.grabthefood.Request;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class displayRequestActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Request> allRequests = new ArrayList<>();
    private ArrayList<String> requestID = new ArrayList<>();
    private requestAdapter myRequestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_request);
        setTitle("All Requests");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myRequestAdapter = new requestAdapter(allRequests, requestID, this);
        mRecyclerView.setAdapter(myRequestAdapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference openPlatformReferecnce = firebaseDatabase.getReference("Openplatform");
        openPlatformReferecnce.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                requestID.add(dataSnapshot.getKey());
                allRequests.add(dataSnapshot.getValue(Request.class));
                myRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int index = requestID.indexOf(dataSnapshot.getKey());
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
    }

}
