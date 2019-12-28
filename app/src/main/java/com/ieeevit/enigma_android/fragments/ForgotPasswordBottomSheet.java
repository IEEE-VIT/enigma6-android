package com.ieeevit.enigma_android.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.ieeevit.enigma_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton getEmail;
    private EditText email;
    private FirebaseAuth auth;
    private LottieAnimationView animationView;

    public ForgotPasswordBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_sheet_forgot_password, container, false);
        initialize(rootView);

        getEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText()!=null && email.getText().length()>0)
                {
                    email.setEnabled(false);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    animationView.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                        startActivity(intent);
                                        animationView.setVisibility(View.INVISIBLE);
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        email.setEnabled(true);
                                    }
                                    else {
                                        animationView.setVisibility(View.INVISIBLE);
                                        email.setEnabled(true);
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        Toast.makeText(getContext(), "Some Error occured", Toast.LENGTH_SHORT).show();
                                    }
                                    }
                                });
                }
                else
                {
                    animationView.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getContext(), "Enter email id", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    private void initialize(View rootView) {
        getEmail = rootView.findViewById(R.id.get_email);
        email = rootView.findViewById(R.id.email_text_edit_bottom_sheet);
        auth = FirebaseAuth.getInstance();
        animationView = rootView.findViewById(R.id.get_mail_lottie_animation);
    }
}
