package com.example.enigma.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.enigma.Fragments.ScrollableRulesFragment;
import com.example.enigma.R;

public class WorkingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.working_activity);

        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.scollable_container, new ScrollableRulesFragment())
                .addToBackStack(null)
                .commit();
    }
}
