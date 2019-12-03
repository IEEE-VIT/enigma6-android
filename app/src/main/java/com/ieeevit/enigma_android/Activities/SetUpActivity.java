package com.ieeevit.enigma_android.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.IntentFilter;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.ieeevit.enigma_android.Fragments.ForgotPasswordBottomSheet;
import com.ieeevit.enigma_android.Fragments.LoginFragment;
import com.ieeevit.enigma_android.Fragments.RulesFragment;
import com.ieeevit.enigma_android.Interfaces.SwitchToOtherFragments;
import com.ieeevit.enigma_android.NetworkStateReceiver;
import com.ieeevit.enigma_android.R;
import com.ieeevit.enigma_android.Fragments.SignupFragment;
import com.ieeevit.enigma_android.Fragments.UserProfileFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.Stack;


public class SetUpActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    static SwitchToOtherFragments mSwitchToOtherFragments;
    private FragmentManager manager;
    private Snackbar snackbar;
    private FrameLayout frameLayout;
    private ForgotPasswordBottomSheet forgotPasswordBottomSheet = new ForgotPasswordBottomSheet();
    private Stack<Fragment> fragmentStack = new Stack<>();
    private NetworkStateReceiver networkStateReceiver;
    private int isShown = 1;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragmentStack.size() == 1) {
            finish();
        } else {
            fragmentStack.pop();
            replace(fragmentStack.peek());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_activity);

        initialize();

        fragmentStack.push(new LoginFragment());
        replace(fragmentStack.peek());


        mSwitchToOtherFragments = new SwitchToOtherFragments() {
            @Override
            public void goToSignUpFragment() {
                fragmentStack.push(new SignupFragment());
                replace(fragmentStack.peek());
            }

            @Override
            public void goToUserProfileFragment() {
                fragmentStack.pop();
                fragmentStack.push(new UserProfileFragment());
                replace(fragmentStack.peek());
            }

            @Override
            public void goToRulesFragment() {
                fragmentStack.pop();
                fragmentStack.push(new RulesFragment());
                replace(fragmentStack.peek());
            }

            @Override
            public void pop() {
                fragmentStack.pop();
                replace(fragmentStack.peek());
            }

            @Override
            public void openForgotPasswordBottomSheet() {
                forgotPasswordBottomSheet.show(getSupportFragmentManager(), forgotPasswordBottomSheet.getTag());
            }

            @Override
            public void snackBarInternetShow() {
                    snackbar = Snackbar.make(frameLayout, "Internet Connection Lost!", Snackbar.LENGTH_INDEFINITE);
                    snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorErrorSnackbar));
                    snackbar.show();
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    isShown++;
            }

            @Override
            public void snackBarInternetDismiss() {
                if (isShown > 1) {
                    if (snackbar != null) {
                        snackbar.dismiss();
                    }
                    Snackbar newSnackbar = Snackbar.make(frameLayout, "You are now connected!", Snackbar.LENGTH_SHORT);
                    newSnackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorSuccessSnackbar));
                    newSnackbar.show();
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                } else {
                    isShown++;
                }
            }
        };
    }

    private void initialize() {
        frameLayout = findViewById(R.id.Container);
        manager = getSupportFragmentManager();
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }


    private void replace(Fragment fragment) {
        manager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left, R.anim.enter_left_to_right,
                        R.anim.exit_left_to_right)
                .replace(R.id.Container, fragment)
                .commit();

    }

    public static SwitchToOtherFragments getmSwitchToOtherFragments() {
        return mSwitchToOtherFragments;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        SetUpActivity.getmSwitchToOtherFragments().snackBarInternetDismiss();
    }

    @Override
    public void networkUnavailable() {
        SetUpActivity.getmSwitchToOtherFragments().snackBarInternetShow();
    }

}
