package com.example.enigma.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.enigma.Activities.SetUpActivity;
import com.example.enigma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class SignupFragment extends Fragment {

    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private MaterialButton signUpButton;
    private String emailText;
    private String passwordText;
    private FirebaseAuth auth;
    private TextView login;
    private FirebaseUser user;
    private ProgressBar pBar;

    public SignupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        initialize(rootView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUpActivity.getmSwitchToOtherFragments().goToLoginFragment();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText() != null && password.getText() != null && confirmPassword.getText() != null) {
                    if(password.getText().toString().matches(confirmPassword.getText().toString()))
                    {
                        emailText = email.getText().toString();
                        passwordText = password.getText().toString();
                        pBar.setVisibility(View.VISIBLE);
                        signUpProcess(emailText, passwordText);
                    }
                    else
                    {
                        Snackbar.make(signUpButton, "Enter the same password", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
                else
                {
                    Snackbar.make(signUpButton, "Enter all the fields", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
        return rootView;
    }

    private void initialize(View rootView) {
        email = rootView.findViewById(R.id.email_text_edit_sign_up_fragment);
        password = rootView.findViewById(R.id.password_text_edit_sign_up_fragment);
        confirmPassword = rootView.findViewById(R.id.confirm_password_text_edit_sign_up_fragment);
        signUpButton = rootView.findViewById(R.id.sign_up_button_sign_up_fragment);
        login = rootView.findViewById(R.id.login_text_view_sign_up_fragment);
        auth = FirebaseAuth.getInstance();
        pBar = rootView.findViewById(R.id.sign_up_progress_bar);
    }

    private void signUpProcess(final String email, final String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        user = auth.getCurrentUser();
                        if(task.isSuccessful())
                        {
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Snackbar.make(signUpButton,"Verification mail sent check your mail",Snackbar.LENGTH_SHORT)
                                                        .show();
                                                SetUpActivity.getmSwitchToOtherFragments().pop();

                                            }
                                            else
                                            {
                                                Snackbar.make(signUpButton,"Some Error occured Try later",Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }

                                        }
                                    });
                        }
                        else if(password.length()<6)
                        {
                            Snackbar.make(signUpButton,"Please enter password of atleast 6 characters",Snackbar.LENGTH_SHORT)
                                    .show();
                            pBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Snackbar.make(signUpButton,"Some error occured.",Snackbar.LENGTH_SHORT)
                                    .show();
                            pBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}
