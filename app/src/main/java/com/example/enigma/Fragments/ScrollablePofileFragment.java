package com.example.enigma.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.FetchUserProfile;
import com.example.enigma.Models.FetchingUserProfile;
import com.example.enigma.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScrollablePofileFragment extends Fragment {
    private ImageView hamburger;
    private TextView username;
    private TextView email;
    private TextView hintsUsed;
    private TextView questionSolved;
    private TextView score;
    private TextView rank;
    private FirebaseAuth auth;
    private int usedHintCount;
    private ProgressBar progressBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public ScrollablePofileFragment() {
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
        progressBar = rootView.findViewById(R.id.scrollable_profile_progress_bar);
        collapsingToolbarLayout = rootView.findViewById(R.id.profile_collapsing_toolbar);
    }

    private void loadProfileDeatils() {
        progressBar.setVisibility(hintsUsed.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FetchUserProfile userProfile = retrofit.create(FetchUserProfile.class);
        Call<FetchingUserProfile> call = userProfile.fetchProfile(auth.getCurrentUser().getUid());
        call.enqueue(new Callback<FetchingUserProfile>() {
            @Override
            public void onResponse(Call<FetchingUserProfile> call, Response<FetchingUserProfile> response) {
                if(response.body().getStatusCode()==200 && response.body().getPayload()!=null) {
                    username.setText(response.body().getPayload().getUser().getName());
                    email.setText(response.body().getPayload().getUser().getEmail());
                    if(response.body().getPayload().getUser().getUsedHint()!=null) {
                        for(boolean f :response.body().getPayload().getUser().getUsedHint())
                        {
                            if(f)
                                usedHintCount+=1;
                            hintsUsed.setText(String.valueOf(usedHintCount));
                        }
                    }
                    else
                    {
                                hintsUsed.setText("0");
                    }
                    questionSolved.setText(String.valueOf((response.body().getPayload().getUser().getLevel())-1));
                    score.setText(String.valueOf(response.body().getPayload().getUser().getPoints()));
                    rank.setText(String.valueOf(response.body().getPayload().getUser().getRank()));
                }
                else
                {
                    makaSnackbar(hamburger, "Some error Occured");
                }
                progressBar.setVisibility(hintsUsed.GONE);
            }

            @Override
            public void onFailure(Call<FetchingUserProfile> call, Throwable t) {
                progressBar.setVisibility(hintsUsed.GONE);
            }
        });
    }

    private void makaSnackbar(View view, String msg) {
        Snackbar.make(view, msg, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }


}
