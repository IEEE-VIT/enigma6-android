package com.example.enigma.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.enigma.MainActivity;
import com.example.enigma.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import static com.facebook.FacebookSdk.getApplicationContext;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 1;
    private MaterialButton facebookLogin;
    private MaterialButton googleLogin;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextView forgotPassword;
    private MaterialButton login;
    private TextView signUp;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private FirebaseAuth auth;
    private String emailText;
    private String passwordText;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        initialize(rootView);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLoginProcess();
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
                MainActivity.getmSwitchToOtherFragments().goToSignUpFragment();
            }
        });

        return rootView;
    }


    private void initialize(View rootView) {
        facebookLogin = rootView.findViewById(R.id.login_facebook_button);
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
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginButton = rootView.findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();
        facebookLoginProcess();
    }

    private void facebookLoginProcess() {
    }

    private void googleLoginProcess() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void normalLogin() {
        emailText = email.getText().toString();
        passwordText = password.getText().toString();

        if((email.getText()!=null && emailText.length()>0)
                || (password.getText()!=null && passwordText.length()>0))
        {
            auth.signInWithEmailAndPassword(emailText, passwordText)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                if(Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified())
                                {
                                    MainActivity.getmSwitchToOtherFragments().goToUserProfileFragment();
                                }
                                else
                                {
                                    Snackbar.make(login, "Verify Email", Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                    });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
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
                        }
                        else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}
