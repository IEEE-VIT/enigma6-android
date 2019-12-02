package com.ieeevit.enigma_android.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.BuildConfig;
import com.ieeevit.enigma_android.Interfaces.FetchUserProfile;
import com.ieeevit.enigma_android.Models.FetchingUserProfile;
import com.ieeevit.enigma_android.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollableProfileFragment extends Fragment {
    private ImageView hamburger;
    private TextView username;
    private TextView email;
    private TextView hintsUsed;
    private TextView questionSolved;
    private TextView score;
    private TextView rank;
    private FirebaseAuth auth;
    private int usedHintCount;
    private LottieAnimationView animationView;
    private Call<FetchingUserProfile> call;
    private ImageView tint;
    private ImageView editUsernameIcon;

    public ScrollableProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scrollable_profile, container, false);
        initializeViews(rootView);
        loadProfileDeatils();
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenDrawerFragments().OpenDrawer();
            }
        });

        editUsernameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenBottomSheets().OpenUserNameBottomSheet();
            }
        });
        return rootView;
    }

    private void initializeViews(View rootView) {
        hamburger = (ImageView)rootView.findViewById(R.id.profile_hamburger);
        username = (TextView)rootView.findViewById(R.id.profile_text_view_username);
        email = (TextView)rootView.findViewById(R.id.profile_text_view_email);
        hintsUsed = (TextView)rootView.findViewById(R.id.profile_text_view_hints_used);
        questionSolved = (TextView)rootView.findViewById(R.id.profile_text_view_questions_solved);
        score = (TextView)rootView.findViewById(R.id.profile_text_view_score);
        rank = (TextView)rootView.findViewById(R.id.profile_text_view_rank);
        auth = FirebaseAuth.getInstance();
        animationView = rootView.findViewById(R.id.lottie_animation_profile);
        tint = rootView.findViewById(R.id.profile_tint);
        editUsernameIcon = rootView.findViewById(R.id.edit_username_icon);
    }

    @Override
    public void onResume() {
        super.onResume();
        WorkingActivity.getOpenBottomSheets().setChecked(3);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call!=null)
        {
            call.cancel();
        }
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void loadProfileDeatils() {
            tint.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            FetchUserProfile userProfile = retrofit.create(FetchUserProfile.class);
            call = userProfile.fetchProfile(auth.getCurrentUser().getUid());
            call.enqueue(new Callback<FetchingUserProfile>() {
                @Override
                public void onResponse(Call<FetchingUserProfile> call, Response<FetchingUserProfile> response) {
                    if (response.body().getStatusCode() == 200 && response.body().getPayload() != null) {
                        username.setText(response.body().getPayload().getUser().getName());

                        email.setText(response.body().getPayload().getUser().getEmail());
                        if (response.body().getPayload().getUser().getUsedHint() != null) {
                            for (boolean f : response.body().getPayload().getUser().getUsedHint()) {
                                if (f)
                                    usedHintCount += 1;
                            }
                            hintsUsed.setText(String.valueOf(usedHintCount));
                            usedHintCount = 0;
                        } else {
                            hintsUsed.setText("0");
                        }
                        questionSolved.setText(String.valueOf((response.body().getPayload().getUser().getLevel()) - 1));
                        score.setText(String.valueOf((int) response.body().getPayload().getUser().getPoints()));
                        rank.setText(String.valueOf(response.body().getPayload().getUser().getRank()));
                    } else {
                        makaSnackbar(hamburger, "Some error Occured");
                    }
                    animationView.setVisibility(View.GONE);
                    tint.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onFailure(Call<FetchingUserProfile> call, Throwable t) {
                    if (call.isCanceled()) {
                        animationView.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                    } else {
                        animationView.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }

                }
            });
        }

    private void makaSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .show();
    }


}
