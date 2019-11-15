package com.example.enigma.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import com.example.enigma.Fragments.LoginFragment;
import com.example.enigma.Fragments.RulesFragment;
import com.example.enigma.Interfaces.SwitchToOtherFragments;
import com.example.enigma.R;
import com.example.enigma.Fragments.SignupFragment;
import com.example.enigma.Fragments.UserProfileFragment;

public class SetUpActivity extends AppCompatActivity {

    static SwitchToOtherFragments mSwitchToOtherFragments;
    private FragmentManager manager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(manager.getBackStackEntryCount()==0)
        {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_activity);


        initialize();

        transaction(new LoginFragment());

        mSwitchToOtherFragments = new SwitchToOtherFragments() {
            @Override
            public void goToLoginFragment() {
                transaction(new LoginFragment());
            }

            @Override
            public void goToSignUpFragment() {
                transaction(new SignupFragment());
            }

            @Override
            public void goToUserProfileFragment() {
                transaction(new UserProfileFragment());
            }

            @Override
            public void goToRulesFragment() {
                transaction(new RulesFragment());
            }

            @Override
            public void pop() {
                manager.popBackStack();
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
