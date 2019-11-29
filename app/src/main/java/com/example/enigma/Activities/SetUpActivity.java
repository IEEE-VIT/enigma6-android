package com.example.enigma.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.enigma.Fragments.ForgotPasswordBottomSheet;
import com.example.enigma.Fragments.LoginFragment;
import com.example.enigma.Fragments.RulesFragment;
import com.example.enigma.Interfaces.SwitchToOtherFragments;
import com.example.enigma.R;
import com.example.enigma.Fragments.SignupFragment;
import com.example.enigma.Fragments.UserProfileFragment;
import com.google.android.material.snackbar.Snackbar;

public class SetUpActivity extends AppCompatActivity {

    static SwitchToOtherFragments mSwitchToOtherFragments;
    private FragmentManager manager;
    private Snackbar snackbar;
    private FrameLayout frameLayout;
    private ForgotPasswordBottomSheet forgotPasswordBottomSheet = new ForgotPasswordBottomSheet();

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

        add(new LoginFragment());

        mSwitchToOtherFragments = new SwitchToOtherFragments() {
            @Override
            public void goToSignUpFragment() {
                add(new SignupFragment());
            }

            @Override
            public void goToUserProfileFragment() {
                replace(new UserProfileFragment());
            }

            @Override
            public void goToRulesFragment() {
                replace(new RulesFragment());
            }

            @Override
            public void pop() {
                manager.popBackStack();
            }

            @Override
            public void openForgotPasswordBottomSheet() {
                forgotPasswordBottomSheet.show(getSupportFragmentManager(), forgotPasswordBottomSheet.getTag());
            }

            @Override
            public void snackBarInternetShow() {
                snackbar = Snackbar.make(frameLayout, "Connect To Internet", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }

            @Override
            public void snackBarInternetDismiss() {
                if(snackbar!=null)
                {
                    snackbar.dismiss();
                }
            }
        };
    }

    private void initialize() {
        frameLayout = findViewById(R.id.Container);
        manager = getSupportFragmentManager();
    }

    private void replace(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.Container, fragment)
                .commit();
    }

    private void add(Fragment fragment) {
        manager.beginTransaction()
                .add(R.id.Container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static SwitchToOtherFragments getmSwitchToOtherFragments() {
        return mSwitchToOtherFragments;
    }


}
