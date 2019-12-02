package com.ieeevit.enigma_android.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ieeevit.enigma_android.EnigmaStatus;
import com.ieeevit.enigma_android.R;

public class LaunchingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ImageView ring;
    private ImageView arrowButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference enigmaStartedReference;
    private TextView comingSoon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        checkLoggedInState();

        arrowButton = findViewById(R.id.arrow_button);
        ring = findViewById(R.id.ring_image);
        comingSoon = findViewById(R.id.coming_soon);

        firebaseDatabase = FirebaseDatabase.getInstance();
        enigmaStartedReference = firebaseDatabase.getReference().child("state");
        enigmaStartedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EnigmaStatus enigmaStatus = dataSnapshot.getValue(EnigmaStatus.class);
                 if(enigmaStatus.getHasStarted())
                 {
                     ring.setVisibility(View.VISIBLE);
                     arrowButton.setVisibility(View.VISIBLE);
                     comingSoon.setVisibility(View.GONE);
                 }
                 else
                 {
                     ring.setVisibility(View.GONE);
                     arrowButton.setVisibility(View.GONE);
                     comingSoon.setVisibility(View.VISIBLE);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchingActivity.this, SetUpActivity.class);
                startActivity(intent);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("LoggedIn", 1);
                editor.apply();
                finish();
            }
        });

    }

    private void checkLoggedInState() {
        if(sharedPreferences.getInt("LoggedIn", 0) == 1)
        {
            Intent intent = new Intent(LaunchingActivity.this, SetUpActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
