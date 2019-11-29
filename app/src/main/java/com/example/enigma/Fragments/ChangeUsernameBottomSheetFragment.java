package com.example.enigma.Fragments;

import android.content.DialogInterface;
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
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.ChangeProfile;
import com.example.enigma.Models.ChangeUserName;
import com.example.enigma.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangeUsernameBottomSheetFragment extends BottomSheetDialogFragment {

    int flag;

    public ChangeUsernameBottomSheetFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        flag = 0;
        View rootView = inflater.inflate(R.layout.bottom_sheet_edit_name, container, false);
        final EditText username = rootView.findViewById(R.id.username_text_edit_bottom_sheet);
        final LottieAnimationView animationView = rootView.findViewById(R.id.edit_name_lottie_animation);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        MaterialButton changeUsername = rootView.findViewById(R.id.save_username);


        changeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText()!=null && username.getText().length()>0)
                {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    animationView.setVisibility(View.VISIBLE);

                    if(username.getText().length() >=5 && !username.getText().toString().contains(" ")) {
                        final Retrofit.Builder builder = new Retrofit.Builder()
                                .baseUrl(BuildConfig.BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create());
                        username.setEnabled(false);
                        final Retrofit retrofit = builder.build();
                        final ChangeProfile profile = retrofit.create(ChangeProfile.class);
                        Call<ChangeUserName> call1 = profile.changeUserName(auth.getCurrentUser().getUid(), username.getText().toString());
                        call1.enqueue(new Callback<ChangeUserName>() {
                            @Override
                            public void onResponse(Call<ChangeUserName> call, Response<ChangeUserName> response1) {
                                if (response1.body() != null) {
                                    if (response1.body().getStatusCode() == 200) {
                                        Toast.makeText(getContext(), "Username Changed!", Toast.LENGTH_SHORT).show();
                                        username.setEnabled(true);
                                        username.setText("");
                                        flag = 1;
                                        WorkingActivity.getOpenBottomSheets().CloseUserNameBottomSheet();
                                        animationView.setVisibility(View.GONE);
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    }
                                } else {
                                    animationView.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "Username Already Taken!", Toast.LENGTH_SHORT).show();
                                    username.setEnabled(true);
                                    username.setText("");
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }

                                if(flag == 1)
                                {

                                }
                            }

                            @Override
                            public void onFailure(Call<ChangeUserName> call, Throwable t) {
                                username.setEnabled(true);
                                animationView.setVisibility(View.GONE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        });
                    }
                    else
                    {
                        animationView.setVisibility(View.GONE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText( getContext(), "No spaces allowed and should be more than 5 characters", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText( getContext(), "Enter New Username", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        WorkingActivity.getUpdateUsernameInterface().refresh();
    }
}
