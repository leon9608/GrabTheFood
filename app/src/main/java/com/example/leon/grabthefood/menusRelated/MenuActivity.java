package com.example.leon.grabthefood.menusRelated;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.leon.grabthefood.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference menuRef;
    private ArrayList<String> menusPath = new ArrayList<>();
    private menuAdapter myMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);
        setTitle("Restaurant Menus");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMenue);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myMenuAdapter = new menuAdapter(menusPath, this);
        mRecyclerView.setAdapter(myMenuAdapter);
        menuRef = FirebaseDatabase.getInstance().getReference("menuePic");
        menuRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                menusPath.add(dataSnapshot.getValue(String.class));
                myMenuAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
