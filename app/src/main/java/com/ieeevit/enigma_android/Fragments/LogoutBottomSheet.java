package com.ieeevit.enigma_android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ieeevit.enigma_android.Activities.LaunchingActivity;
import com.ieeevit.enigma_android.Activities.SetUpActivity;
import com.ieeevit.enigma_android.Activities.SplashActivity;
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton yesButton;
    private MaterialButton noButton;

    public LogoutBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_sheet_logout, container, false);
        initialize(rootView);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetUpActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                getActivity().finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenBottomSheets().CloseLogoutBottomSheet();
                WorkingActivity.getOpenDrawerFragments().OpenDrawer();
            }
        });
        return rootView;
    }

    private void initialize(View rootView) {
        yesButton = rootView.findViewById(R.id.yes_logout);
        noButton = rootView.findViewById(R.id.no_logout);
    }
}
