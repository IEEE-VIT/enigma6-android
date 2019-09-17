package com.example.enigma.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.enigma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    public SignupFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);

        initialize(rootView);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText() != null && password.getText() != null && confirmPassword.getText() != null
                        && password.getText().toString().equals(confirmPassword.getText().toString())) {
                    emailText = email.getText().toString();
                    passwordText = password.getText().toString();
                    signUpProcess(emailText, passwordText);
                }

                //TODO:Ask Sparsh Bhaiya about it
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
                                                Snackbar.make(signUpButton,"Verification mail sent check your mail",Snackbar.LENGTH_SHORT)
                                                        .show();


                                            }
                                            else
                                            {
                                                Snackbar.make(signUpButton,"Some Error occured Try later",Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }

                                        }
                                    });
                        }
                        else
                        {
                            Snackbar.make(signUpButton,"Please enter password of atleast 6 characters",Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
}
