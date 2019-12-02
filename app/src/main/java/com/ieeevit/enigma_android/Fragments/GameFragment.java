package com.ieeevit.enigma_android.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.BuildConfig;
import com.ieeevit.enigma_android.Interfaces.CheckAnswer;
import com.ieeevit.enigma_android.Interfaces.FetchCurrentQuestion;
import com.ieeevit.enigma_android.Interfaces.FetchUserProfile;
import com.ieeevit.enigma_android.Models.Answer;
import com.ieeevit.enigma_android.Models.FetchingCurrentQuestion;
import com.ieeevit.enigma_android.Models.FetchingUserProfile;
import com.ieeevit.enigma_android.Models.IsAnswerRight;
import com.ieeevit.enigma_android.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private ShimmerFrameLayout shimmerViewContainer;
    private LottieAnimationView animationView;
    private ImageView tint;
    private FloatingActionButton questionHint;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView score;
    private TextView rank;
    private TextView score1;
    private TextView rank1;
    private ImageView bottomDrawable;
    private Context context;

    public GameFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_game, container, false);
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

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > dpToPx(context, 200)) {
                    score.setVisibility(View.GONE);
                    rank.setVisibility(View.GONE);
                    score1.setVisibility(View.GONE);
                    rank1.setVisibility(View.GONE);
                    bottomDrawable.setVisibility(View.GONE);
                }
                else
                {
                    score.setVisibility(View.VISIBLE);
                    rank.setVisibility(View.VISIBLE);
                    score1.setVisibility(View.VISIBLE);
                    rank1.setVisibility(View.VISIBLE);
                    bottomDrawable.setVisibility(View.VISIBLE);
                }
            }
        });

        fetchQuestion();
        return rootView;
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    private void initializeViews(View rootView) {
        context = getContext();
        score1 = rootView.findViewById(R.id.textView26);
        rank1 = rootView.findViewById(R.id.textView24);
        bottomDrawable = rootView.findViewById(R.id.imageView3);
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
        shimmerViewContainer = rootView.findViewById(R.id.shimmer_text_container_5);
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



    private void checkAnswer() {
            tint.setVisibility(View.VISIBLE);
            animationView.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (answer.getText() != null && answer.getText().length() > 0) {
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
                            switch (response.body().getPayload().getHowClose()) {
                                case "4":
                                    animationView.setVisibility(View.GONE);
                                    tint.setVisibility(View.INVISIBLE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    makeSnackbar(submit, taunt4.get(random.nextInt(taunt4.size())));
                                    break;

                                case "3":
                                    animationView.setVisibility(View.GONE);
                                    tint.setVisibility(View.INVISIBLE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    makeSnackbar(submit, taunt3.get(random.nextInt(taunt3.size())));
                                    break;

                                case "2":
                                    animationView.setVisibility(View.GONE);
                                    tint.setVisibility(View.INVISIBLE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    makeSnackbar(submit, taunt2.get(random.nextInt(taunt2.size())));
                                    break;

                                case "1":
                                    animationView.setVisibility(View.GONE);
                                    tint.setVisibility(View.INVISIBLE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    makeSnackbar(submit, taunt1.get(random.nextInt(taunt1.size())));
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IsAnswerRight> call, Throwable t) {
                        if (answerCall.isCanceled()) {
                            animationView.setVisibility(View.GONE);
                            tint.setVisibility(View.INVISIBLE);
                        } else {
                            animationView.setVisibility(View.GONE);
                            tint.setVisibility(View.INVISIBLE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }

                    }
                });
            } else {
                animationView.setVisibility(View.GONE);
                tint.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                makeSnackbar(submit, "Enter your answer");
            }
    }

    private void fetchQuestion() {
            tint.setVisibility(View.VISIBLE);
            mShimmerViewContainer.startShimmerAnimation();
            shimmerViewContainer.startShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.VISIBLE);
            shimmerViewContainer.setVisibility(View.VISIBLE);
            questionImage.setVisibility(View.INVISIBLE);
            question.setVisibility(View.INVISIBLE);

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
                public void onResponse(Call<FetchingCurrentQuestion> call, Response<FetchingCurrentQuestion> response) {
                    if (response.body() != null) {
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
                                    if (response.body().getStatusCode() == 200 && response.body().getPayload() != null) {
                                        score.setText(String.valueOf((int) response.body().getPayload().getUser().getPoints()));
                                        rank.setText(String.valueOf(response.body().getPayload().getUser().getRank()));
                                        if(getActivity()!=null)
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        questionImage.setVisibility(View.VISIBLE);
                                        question.setVisibility(View.VISIBLE);
                                        mShimmerViewContainer.stopShimmerAnimation();
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        shimmerViewContainer.stopShimmerAnimation();
                                        shimmerViewContainer.setVisibility(View.GONE);
                                        tint.setVisibility(View.INVISIBLE);
                                        editor = sharedPreferences.edit();
                                        editor.putString("Hint", null);
                                        editor.apply();

                                    }
                                    else {
                                        if(getActivity()!=null)
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        questionImage.setVisibility(View.VISIBLE);
                                        question.setVisibility(View.VISIBLE);
                                        mShimmerViewContainer.stopShimmerAnimation();
                                        mShimmerViewContainer.setVisibility(View.GONE);
                                        shimmerViewContainer.stopShimmerAnimation();
                                        shimmerViewContainer.setVisibility(View.GONE);
                                        tint.setVisibility(View.INVISIBLE);
                                        editor = sharedPreferences.edit();
                                        editor.putString("Hint", null);
                                        editor.apply();
                                        makeSnackbar(hamburger, "Some error Occured");
                                    }
                                }

                                @Override
                                public void onFailure(Call<FetchingUserProfile> call, Throwable t) {
                                    if(getActivity()!=null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    questionImage.setVisibility(View.VISIBLE);
                                    question.setVisibility(View.VISIBLE);
                                    mShimmerViewContainer.stopShimmerAnimation();
                                    mShimmerViewContainer.setVisibility(View.GONE);
                                    shimmerViewContainer.stopShimmerAnimation();
                                    shimmerViewContainer.setVisibility(View.GONE);
                                    tint.setVisibility(View.INVISIBLE);
                                }
                            });
                    }
                        else {
                        questionImage.setVisibility(View.VISIBLE);
                        question.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        shimmerViewContainer.stopShimmerAnimation();
                        shimmerViewContainer.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                        if(getActivity()!=null)
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        makeSnackbar(submit, "Some Error Occured");
                    }
                }

                @Override
                public void onFailure(Call<FetchingCurrentQuestion> call, Throwable t) {
                    if (call3.isCanceled()) {
                        questionImage.setVisibility(View.VISIBLE);
                        question.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        shimmerViewContainer.stopShimmerAnimation();
                        shimmerViewContainer.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                    } else {
                        questionImage.setVisibility(View.VISIBLE);
                        question.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        shimmerViewContainer.stopShimmerAnimation();
                        shimmerViewContainer.setVisibility(View.GONE);
                        tint.setVisibility(View.INVISIBLE);
                        if(getActivity()!=null)
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }

                }
            });
        }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call3!=null) {
            call3.cancel();
        }
        if(answerCall!=null) {
            answerCall.cancel();
        }
        if(getActivity()!= null)
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void makeSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
