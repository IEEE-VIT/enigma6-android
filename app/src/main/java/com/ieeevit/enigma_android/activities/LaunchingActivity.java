package com.ieeevit.enigma_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ieeevit.enigma_android.utils.EnigmaStatus;
import com.ieeevit.enigma_android.R;

public class LaunchingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private ImageView ring;
    private ImageView arrowButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference enigmaStartedReference;
    private TextView registration;
    private TextView date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        arrowButton = findViewById(R.id.arrow_button);
        ring = findViewById(R.id.ring_image);
        registration = findViewById(R.id.regsitration);
        date = findViewById(R.id.date_time);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

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
                     date.setVisibility(View.GONE);
                     registration.setVisibility(View.GONE);
                 }
                 else
                 {

                     date.setText(enigmaStatus.getSecondaryMessage());
                     registration.setText(enigmaStatus.getPrimaryMessage());
                     ring.setVisibility(View.GONE);
                     arrowButton.setVisibility(View.GONE);
                     registration.setVisibility(View.VISIBLE);
                     date.setVisibility(View.VISIBLE);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchingActivity.this, WorkingActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("LoggedIn", 1);
                editor.apply();;
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreferences.getInt("LoggedIn", 0) == 1)
        {
            Intent intent = new Intent(LaunchingActivity.this, WorkingActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
