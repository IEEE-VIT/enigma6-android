package com.example.enigma.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.enigma.R;

public class LaunchingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        checkLoggedInState();

        ImageView arrowButton = findViewById(R.id.arrow_button);
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
