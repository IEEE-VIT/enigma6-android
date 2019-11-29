package com.example.enigma.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.bumptech.glide.Glide;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.CheckAnswer;
import com.example.enigma.Interfaces.FetchCurrentQuestion;
import com.example.enigma.Interfaces.FetchUserProfile;
import com.example.enigma.Models.Answer;
import com.example.enigma.Models.FetchingCurrentQuestion;
import com.example.enigma.Models.FetchingUserProfile;
import com.example.enigma.Models.IsAnswerRight;
import com.example.enigma.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameFragment extends Fragment {

    private ImageView hamburger;
    private ImageView questionImage;
    private FirebaseAuth auth;
    private TextView question;
    private MaterialButton submit;
    private TextView answer;
    private String imgURL;
    private ArrayList<String> taunt1 = new ArrayList<>();
    private ArrayList<String> taunt2 = new ArrayList<>();
    private ArrayList<String> taunt3 = new ArrayList<>();
    private ArrayList<String> taunt4 = new ArrayList<>();
    private Random random = new Random();
    private Call<IsAnswerRight> answerCall;
    private Call<FetchingCurrentQuestion> call3;
    private ShimmerFrameLayout mShimmerViewContainer;
    private LottieAnimationView animationView;
    private ImageView tint;
    private FloatingActionButton questionHint;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView score;
    private TextView rank;

    public GameFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        initializeViews(rootView);
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenDrawerFragments().OpenDrawer();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer();
            }
        });
        questionHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkingActivity.getOpenBottomSheets().OpenHintBottomSheet();
            }
        });
        fetchQuestion();
        return rootView;
    }


    private void initializeViews(View rootView) {
        hamburger =  rootView.findViewById(R.id.game_hamburger);
        score = rootView.findViewById(R.id.game_score);
        rank = rootView.findViewById(R.id.game_rank);
        auth = FirebaseAuth.getInstance();
        question = rootView.findViewById(R.id.question_text_view_game_fragment);
        submit = rootView.findViewById(R.id.submit_material_button_game_fragment);
        answer = rootView.findViewById(R.id.answer_edit_text_game_fragment);
        questionImage = rootView.findViewById(R.id.question_image);
        questionHint = rootView.findViewById(R.id.question_hint_fab);
        mShimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        animationView = rootView.findViewById(R.id.lottie_animation_game);
        animationView.setSpeed(1);
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        tint = rootView.findViewById(R.id.game_tint);
        taunt1.add("Almost There! Think Harder");
        taunt1.add("You're nearly there!");
        taunt1.add("Quiet close! Come on!");
        taunt2.add("Close, but not close enough.");
        taunt2.add("You are on the right path.");
        taunt2.add("Think. Think harder!");
        taunt3.add("You might be thinking along these lines.");
        taunt3.add("Nice approach. But, try harder.");
        taunt3.add("Think you're smart?");
        taunt4.add("Nope. Try using a hint?");
        taunt4.add("Ah you are better than this. Use a hint.");
        taunt4.add("Faar from home. Use your hint!");
    }

    @Override
    public void onResume() {
        super.onResume();
        WorkingActivity.getOpenBottomSheets().setChecked(1);
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            makeSnackbar(question, "Please connect to your Internet");
        }
    }

    private void checkAnswer() {
        tint.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(answer.getText()!=null && answer.getText().length()>0) {
            Answer Ans = new Answer(answer.getText().toString());
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            final CheckAnswer ans = retrofit.create(CheckAnswer.class);
            answerCall = ans.checkAnswer(auth.getCurrentUser().getUid(), Ans);
            answerCall.enqueue(new Callback<IsAnswerRight>() {
                @Override
                public void onResponse(Call<IsAnswerRight> call, Response<IsAnswerRight> response) {
                    if (response.code() == 200) {
                        if (response.body().isAnswerCorrect()) {
                            animationView.setVisibility(View.GONE);
                            tint.setVisibility(View.INVISIBLE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            makeSnackbar(submit, "Your Answer is Correct!");
                            fetchQuestion();
                            answer.setText("");
                        }
                        switch (response.body().getPayload().getHowClose())
                        {
                            case "4" : animationView.setVisibility(View.GONE);
                                       tint.setVisibility(View.INVISIBLE);
                                       getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                       makeSnackbar(submit, taunt4.get(random.nextInt(taunt4.size())));
                                       break;

                            case "3" : animationView.setVisibility(View.GONE);
                                tint.setVisibility(View.INVISIBLE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                makeSnackbar(submit, taunt3.get(random.nextInt(taunt3.size())));
                                break;

                            case "2" : animationView.setVisibility(View.GONE);
                                tint.setVisibility(View.INVISIBLE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                makeSnackbar(submit, taunt2.get(random.nextInt(taunt2.size())));
                                break;

                            case "1" : animationView.setVisibility(View.GONE);
                                tint.setVisibility(View.INVISIBLE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                makeSnackbar(submit, taunt1.get(random.nextInt(taunt1.size())));
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<IsAnswerRight> call, Throwable t) {
                    if(answerCall.isCanceled())
                    {
                        animationView.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        animationView.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }

                }
            });
        }
        else
        {
            animationView.setVisibility(View.GONE);
            tint.setVisibility(View.INVISIBLE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            makeSnackbar(submit, "Enter your answer");
        }
    }

    private void fetchQuestion() {
        tint.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        questionImage.setVisibility(View.INVISIBLE);

        Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        final Retrofit retrofit = builder.build();
        final FetchCurrentQuestion currentQuestion = retrofit.create(FetchCurrentQuestion.class);
        call3 = currentQuestion.getCurrentQuestion(auth.getCurrentUser().getUid());
        call3.enqueue(new Callback<FetchingCurrentQuestion>() {
            @Override
            public void onResponse(Call<FetchingCurrentQuestion> call, Response<FetchingCurrentQuestion> response)
            {
                if(response.body()!=null)
                {
                    question.setText(response.body().getPayload().getQuestion());
                    imgURL = response.body().getPayload().getImgURL();
                    Glide.with(getView())
                            .load(imgURL)
                            .into(questionImage);


                    FetchUserProfile userProfile = retrofit.create(FetchUserProfile.class);
                    Call<FetchingUserProfile> call1 = userProfile.fetchProfile(auth.getCurrentUser().getUid());
                    call1.enqueue(new Callback<FetchingUserProfile>() {
                        @Override
                        public void onResponse(Call<FetchingUserProfile> call, Response<FetchingUserProfile> response) {
                            if(response.body().getStatusCode()==200 && response.body().getPayload()!=null) {
                                score.setText(String.valueOf((int)response.body().getPayload().getUser().getPoints()));
                                rank.setText(String.valueOf(response.body().getPayload().getUser().getRank()));
                            }
                            else
                            {
                                makeSnackbar(hamburger, "Some error Occured");
                            }
                        }

                        @Override
                        public void onFailure(Call<FetchingUserProfile> call, Throwable t) {

                        }
                    });

                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    questionImage.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    tint.setVisibility(View.INVISIBLE);
                    editor = sharedPreferences.edit();
                    editor.putString("Hint", null);
                    editor.apply();
                }
                else
                {
                    questionImage.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    tint.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    makeSnackbar(submit, "Some Error Occured");
                }
            }

            @Override
            public void onFailure(Call<FetchingCurrentQuestion> call, Throwable t) {
                if(call3.isCanceled())
                {
                    questionImage.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    tint.setVisibility(View.INVISIBLE);
                }
                else
                {
                    questionImage.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    tint.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call3!=null)
            call3.cancel();
        if(answerCall!=null)
            answerCall.cancel();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void makeSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
