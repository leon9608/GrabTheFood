package com.example.leon.grabthefood.commonActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leon.grabthefood.R;
import com.example.leon.grabthefood.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class makeRequestActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference userReference;
    private DatabaseReference openPlatformReferecnce;
    int numberOfRequest;
    String userID;
    private EditText foodName;
    private EditText restaurantNameText;
    private EditText restaurantAddrsText;
    private EditText priceText;
    private EditText deliveryAddrsText;
    private EditText contactText;
    private EditText remarkText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_request);
        setTitle("Make a Request Now");
        foodName = (EditText) findViewById(R.id.makefood_name);
        restaurantNameText = (EditText) findViewById(R.id.makerestaurant_name);
        restaurantAddrsText = (EditText) findViewById(R.id.makerestaurant_address);
        priceText = (EditText) findViewById(R.id.makeprice);
        deliveryAddrsText = (EditText) findViewById(R.id.makedelivery_address);
        contactText = (EditText) findViewById(R.id.makecontact_number);
        remarkText = (EditText) findViewById(R.id.makeremark);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        openPlatformReferecnce = firebaseDatabase.getReference("Openplatform");
        userID = mAuth.getCurrentUser().getUid();
        userReference = firebaseDatabase.getReference(userID);
        userReference.child("NumberOfRequest").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        numberOfRequest = dataSnapshot.getValue(Integer.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void submit(View view) {
        String contact = contactText.getText().toString();
        String deliveryAddress = deliveryAddrsText.getText().toString();
        String food = foodName.getText().toString();
        String price = priceText.getText().toString();
        String remark = remarkText.getText().toString();
        String restaurantAddress = restaurantAddrsText.getText().toString();
        String restaurantName = restaurantNameText.getText().toString();
        if (legalInput(contact, deliveryAddress, food, price, restaurantAddress, restaurantName)) {
            Request request = new Request(contact, deliveryAddress, food, price, remark,
                    restaurantAddress, restaurantName, "Available");
            userReference.child("MyRequest").child(userID + numberOfRequest).setValue(request);
            openPlatformReferecnce.child(userID + numberOfRequest).setValue(request);
            userReference.child("NumberOfRequest").setValue((numberOfRequest + 1) % 10);
            Toast.makeText(this, R.string.request_success, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public boolean legalInput(String contact, String deliveryAddress, String food, String price,
                               String restaurantAddress, String restaurantName) {

        if (TextUtils.isEmpty(food)) {
            foodName.setError("Fill in the name of the food!");
            return false;
        }
        if (TextUtils.isEmpty(restaurantName)) {
            restaurantNameText.setError("Fill in the restaurant name!");
            return false;
        }
        if (TextUtils.isEmpty(restaurantAddress)) {
            restaurantAddrsText.setError("Fill in the restaurant address!");
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            priceText.setError("Fill in the amount you willing to pay!");
            return false;
        }
        if (TextUtils.isEmpty(deliveryAddress)) {
            deliveryAddrsText.setError("Fill in your address!");
            return false;
        }
        if (TextUtils.isEmpty(contact) || contact.length() != 10) {
            contactText.setError("Fill in your 10 digit contact number!");
            return false;
        }
        return true;
    }
}
