package com.example.enigma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.enigma.ui.LoginFragment;
import com.example.enigma.ui.SignupFragment;
import com.example.enigma.ui.UserProfileFragment;

public class MainActivity extends AppCompatActivity {

    static SwitchToOtherFragments mSwitchToOtherFragments;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialize();

        transaction(new LoginFragment());

        mSwitchToOtherFragments = new SwitchToOtherFragments() {
            @Override
            public void goToSignUpFragment() {
                transaction(new SignupFragment());
            }

            @Override
            public void goToUserProfileFragment() {
                transaction(new UserProfileFragment());
            }
        };
    }

    private void initialize() {
        manager = getSupportFragmentManager();
    }

    private void transaction(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.Container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static SwitchToOtherFragments getmSwitchToOtherFragments() {
        return mSwitchToOtherFragments;
    }


}
