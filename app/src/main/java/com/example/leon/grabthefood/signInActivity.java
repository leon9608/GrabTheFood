package com.example.leon.grabthefood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leon.grabthefood.commonActivity.displayRequestActivity;
import com.example.leon.grabthefood.commonActivity.makeRequestActivity;
import com.example.leon.grabthefood.menusRelated.MenuActivity;
import com.example.leon.grabthefood.menusRelated.cameraActivity;
import com.example.leon.grabthefood.personalActivity.myPickupsActivity;
import com.example.leon.grabthefood.personalActivity.myRequestActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Reference from https://Used feature in github.com/firebase/quickstart-android/blob/master/auth/app/src/main/java/
 * com/google/fireauth/GoogleSignInActivity.javabase/quickstart/
 */

public class signInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private FirebaseDatabase firebaseDatabase;
    private ImageView welcomeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        welcomeImage = (ImageView) findViewById(R.id.welcome_view);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.camera).setOnClickListener(this);
        findViewById(R.id.make_request_button).setOnClickListener(this);
        findViewById(R.id.display_all_button).setOnClickListener(this);
        findViewById(R.id.my_request_button).setOnClickListener(this);
        findViewById(R.id.my_pickup_button).setOnClickListener(this);
        findViewById(R.id.menu_button).setOnClickListener(this);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    final DatabaseReference rootRef = firebaseDatabase.getReference();
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (!dataSnapshot.hasChild(userID)) {
                                rootRef.child(userID).child("NumberOfRequest").setValue(0);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(signInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START signOut]
    private void signOut() {
        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }

    private void getCamera() {
        Intent intent = new Intent(signInActivity.this, cameraActivity.class);
        startActivity(intent);
    }

    private void makeRequest() {
        Intent intent = new Intent(signInActivity.this, makeRequestActivity.class);
        startActivity(intent);

    }

    private void displayAll() {
        Intent intent = new Intent(signInActivity.this, displayRequestActivity.class);
        startActivity(intent);
    }

    private void displayMyRequest() {
        Intent intent = new Intent(signInActivity.this, myRequestActivity.class);
        startActivity(intent);
    }

    private void displayMyPickUp() {
        Intent intent = new Intent(signInActivity.this, myPickupsActivity.class);
        startActivity(intent);
    }

    private void displayMenu() {
        Intent intent = new Intent(signInActivity.this, MenuActivity.class);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google play services error.", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            mStatusTextView.setTextSize(20);
            welcomeImage.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.camera).setVisibility(View.VISIBLE);
            findViewById(R.id.make_request_button).setVisibility(View.VISIBLE);
            findViewById(R.id.display_all_button).setVisibility(View.VISIBLE);
            findViewById(R.id.my_pickup_button).setVisibility(View.VISIBLE);
            findViewById(R.id.my_request_button).setVisibility(View.VISIBLE);
            findViewById(R.id.menu_button).setVisibility(View.VISIBLE);

        } else {
            mStatusTextView.setText(getString(R.string.welcome) + "\n" +
                    getString(R.string.google_title_text));
            mStatusTextView.setTextSize(40);
            welcomeImage.setVisibility(View.GONE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            findViewById(R.id.camera).setVisibility(View.GONE);
            findViewById(R.id.make_request_button).setVisibility(View.GONE);
            findViewById(R.id.display_all_button).setVisibility(View.GONE);
            findViewById(R.id.my_pickup_button).setVisibility(View.GONE);
            findViewById(R.id.my_request_button).setVisibility(View.GONE);
            findViewById(R.id.menu_button).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.camera:
                getCamera();
                break;
            case R.id.make_request_button:
                makeRequest();
                break;
            case R.id.display_all_button:
                displayAll();
                break;
            case R.id.my_request_button:
                displayMyRequest();
                break;
            case R.id.my_pickup_button:
                displayMyPickUp();
                break;
            case R.id.menu_button:
                displayMenu();
                break;
        }
    }


}