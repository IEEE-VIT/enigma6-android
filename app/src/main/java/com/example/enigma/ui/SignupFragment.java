package com.example.enigma.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.enigma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupFragment extends Fragment {

    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private MaterialButton signUp;
    private String emailText;
    private String passwordText;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public SignupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        email = rootView.findViewById(R.id.email_text_edit);
        password = rootView.findViewById(R.id.password_text_edit);
        confirmPassword = rootView.findViewById(R.id.confirm_password_text_edit);
        signUp = rootView.findViewById(R.id.login_button);
        auth = FirebaseAuth.getInstance();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText() != null && password.getText() != null && confirmPassword.getText() != null
                        && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    emailText = email.getText().toString();
                    passwordText = password.getText().toString();
                    signUpProcess(emailText, passwordText);
                }

                else if(! password.getText().toString().matches(confirmPassword.getText().toString()))
                {
                    Snackbar.make(signUp,"Enter password fields correctly",BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }

                // check with sparsh bhaiya for empty fields
                else if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()
                        || confirmPassword.getText().toString().isEmpty())
                {
                    Snackbar.make(signUp,"Empty fields",BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }

            }
        });
        return rootView;
    }

    private void signUpProcess(String email, String password) {

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
                                                Snackbar.make(signUp,"Verification mail sent check your mail",BaseTransientBottomBar.LENGTH_SHORT)
                                                        .show();

                                            }
                                            else
                                            {
                                                Snackbar.make(signUp,"Some Error occured Try later",BaseTransientBottomBar.LENGTH_SHORT)
                                                        .show();
                                            }

                                        }
                                    });
                        }
                        else
                        {
                            Snackbar.make(signUp,"Please enter password of atleast 6 characters",BaseTransientBottomBar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
