package com.example.enigma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.enigma.ui.SignupFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SignupFragment signup = new SignupFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.Container,signup)
                .commit();



    }
}
