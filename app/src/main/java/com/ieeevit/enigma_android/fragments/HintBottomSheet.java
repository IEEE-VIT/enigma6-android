package com.ieeevit.enigma_android.fragments;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.ieeevit.enigma_android.activities.WorkingActivity;
import com.ieeevit.enigma_android.BuildConfig;
import com.ieeevit.enigma_android.interfaces.FetchCurrentQuestion;
import com.ieeevit.enigma_android.interfaces.FetchHint;
import com.ieeevit.enigma_android.models.FetchingCurrentQuestion;
import com.ieeevit.enigma_android.models.FetchingHint;
import com.ieeevit.enigma_android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HintBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton yesButton;
    private MaterialButton noButton;
    private MaterialButton okButton;
    private TextView wantHint;
    private TextView hint;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private LottieAnimationView animationView;

    public HintBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.bottom_sheet_hint, container, false);
        initialize(rootView);

        ckeckState();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noButton.setVisibility(View.GONE);
                yesButton.setVisibility(View.GONE);
                wantHint.setVisibility(View.GONE);
                animationView.setVisibility(View.VISIBLE);
                getHint();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenBottomSheets().CloseHintBottomSheet();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenBottomSheets().CloseHintBottomSheet();
            }
        });

        return rootView;
    }

    private void initialize(View rootView) {
        yesButton = rootView.findViewById(R.id.Yes);
        noButton = rootView.findViewById(R.id.NO);
        wantHint = rootView.findViewById(R.id.want_hint);
        hint = rootView.findViewById(R.id.hint);
        okButton = rootView.findViewById(R.id.ok);
        animationView = rootView.findViewById(R.id.hint_lottie_animation);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        LayoutTransition transition = new LayoutTransition();
        transition.setAnimateParentHierarchy(false);
        ConstraintLayout layout = rootView.findViewById(R.id.hint_container);
        layout.setLayoutTransition(transition);
    }

    private void getHint() {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            final FetchCurrentQuestion question = retrofit.create(FetchCurrentQuestion.class);
            Call<FetchingCurrentQuestion> call1 = question.getCurrentQuestion(auth.getCurrentUser().getUid());
            call1.enqueue(new Callback<FetchingCurrentQuestion>() {
                @Override
                public void onResponse(Call<FetchingCurrentQuestion> call, Response<FetchingCurrentQuestion> response) {
                    if(response.body()!= null) {
                        if (response.body().getPayload().getHint() == null) {
                            fetchHint();
                        } else {
                            hint.setText(response.body().getPayload().getHint());
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            animationView.setVisibility(View.GONE);
                            okButton.setVisibility(View.VISIBLE);
                            hint.setVisibility(View.VISIBLE);
                            editor = sharedPreferences.edit();
                            editor.putString("Hint", response.body().getPayload().getHint());
                            editor.apply();
                        }
                    }
                    else
                    {
                        animationView.setVisibility(View.GONE);
                        noButton.setVisibility(View.VISIBLE);
                        yesButton.setVisibility(View.VISIBLE);
                        wantHint.setVisibility(View.VISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getContext(), "Some error occured", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<FetchingCurrentQuestion> call, Throwable t) {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });
        }

    private void fetchHint() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final FetchHint fetch = retrofit.create(FetchHint.class);
        Call<FetchingHint> call2 = fetch.fetchHint(auth.getCurrentUser().getUid());
        call2.enqueue(new Callback<FetchingHint>() {
            @Override
            public void onResponse(Call<FetchingHint> call, Response<FetchingHint> response) {
                hint.setText(response.body().getPayload().getHint());
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                animationView.setVisibility(View.GONE);
                okButton.setVisibility(View.VISIBLE);
                hint.setVisibility(View.VISIBLE);
                editor = sharedPreferences.edit();
                editor.putString("Hint", response.body().getPayload().getHint());
                editor.apply();
            }

            @Override
            public void onFailure(Call<FetchingHint> call, Throwable t) {
                    animationView.setVisibility(View.GONE);
                    noButton.setVisibility(View.VISIBLE);
                    yesButton.setVisibility(View.VISIBLE);
                    wantHint.setVisibility(View.VISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            });

    }

    private void ckeckState() {

        if(sharedPreferences.getString("Hint", null) == null)
        {
            noButton.setVisibility(View.VISIBLE);
            yesButton.setVisibility(View.VISIBLE);
            wantHint.setVisibility(View.VISIBLE);
            hint.setVisibility(View.GONE);
            okButton.setVisibility(View.GONE);
        }
        else
        {
            noButton.setVisibility(View.GONE);
            yesButton.setVisibility(View.GONE);
            wantHint.setVisibility(View.GONE);
            hint.setText(sharedPreferences.getString("Hint", null));
            okButton.setVisibility(View.VISIBLE);
            hint.setVisibility(View.VISIBLE);
        }
    }

}
