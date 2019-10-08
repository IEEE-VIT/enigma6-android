package com.example.enigma.Fragments;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.enigma.Activities.SetUpActivity;
import com.example.enigma.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;


public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private MaterialButton googleLogin;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextView forgotPassword;
    private MaterialButton login;
    private TextView signUp;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String emailText;
    private String passwordText;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        initialize(rootView);

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLoginProcess();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalLogin();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUpActivity.getmSwitchToOtherFragments().goToSignUpFragment();
            }
        });

        return rootView;
    }


    private void initialize(View rootView) {
        googleLogin = rootView.findViewById(R.id.login_google_button);
        email = rootView.findViewById(R.id.email_text_edit_login_fragment);
        password = rootView.findViewById(R.id.password_text_edit_login_fragment);
        forgotPassword = rootView.findViewById(R.id.forgot_password_text_view);
        login = rootView.findViewById(R.id.login_button_login_fragment);
        signUp = rootView.findViewById(R.id.sign_up_text_view_login_fragment);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            // We are not connected to a network
            makeSnackbar("Please connect to your Internet", login);
        }
    }

    private void changePassword() {

        if(email.getText()!= null && email.getText().length()>0 ) {
            auth.sendPasswordResetEmail(email.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                makeSnackbar("Mail sent for password", forgotPassword);
                            }
                            else
                            {
                                makeSnackbar("Wrong email", forgotPassword);
                            }
                        }
                    });
        }
        else
        {
            makeSnackbar("Please enter the email field for this feature", forgotPassword);
        }
    }





    private void googleLoginProcess() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void normalLogin() {
        emailText = email.getText().toString();
        passwordText = password.getText().toString();

        if((email.getText()!=null && emailText.length()>0)
                && (password.getText()!=null && passwordText.length()>0)) {
            auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                    SetUpActivity.getmSwitchToOtherFragments().goToUserProfileFragment();
                                } else {
                                    Snackbar.make(login, "Verify Email", Snackbar.LENGTH_SHORT)
                                            .setAction("Resend", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    user = auth.getCurrentUser();
                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful())
                                                                    {
                                                                        makeSnackbar("Email sent again", login);
                                                                    }
                                                                    else
                                                                    {
                                                                        makeSnackbar("Some Error Occured Try Later", login);
                                                                    }
                                                                }
                                                            });
                                                }
                                            })
                                            .show();
                                }
                            }
                            else
                            {
                                makeSnackbar("Wrong Details", login);
                            }
                        }
                    });
        }
        else {
            makeSnackbar("Please enter all the fields", login);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
            }
            catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            SetUpActivity.getmSwitchToOtherFragments().goToUserProfileFragment();
                        }
                        else {
                            makeSnackbar("Some Error Occured Try After some time", googleLogin);
                        }
                    }
                });
    }

    private void makeSnackbar(String message , View view)
    {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
