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
import com.bumptech.glide.Glide;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.CheckAnswer;
import com.example.enigma.Interfaces.FetchCurrentQuestion;
import com.example.enigma.Interfaces.FetchHint;
import com.example.enigma.Models.Answer;
import com.example.enigma.Models.FetchingCurrentQuestion;
import com.example.enigma.Models.FetchingHint;
import com.example.enigma.Models.IsAnswerRight;
import com.example.enigma.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameFragment extends Fragment {

    private ImageView hamburger;
    private ImageView questionImage;
    private ImageView questionHint;
    private FirebaseAuth auth;
    private TextView question;
    private String hint;
    private TextView questionNumber;
    private MaterialButton submit;
    private TextView answer;
    private String imgURL;
    private ProgressBar progressBar;

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
                if(hint==null)
                    getHint();
                else
                    makeSnackbar(questionImage, hint);
            }
        });

        fetchQuestion();
        return rootView;
    }


    private void initializeViews(View rootView) {
        hamburger =  rootView.findViewById(R.id.game_hamburger);
        auth = FirebaseAuth.getInstance();
        question = rootView.findViewById(R.id.question_text_view_game_fragment);
        questionNumber = rootView.findViewById(R.id.question_number_text_view_game_fragment);
        submit = rootView.findViewById(R.id.submit_material_button_game_fragment);
        answer = rootView.findViewById(R.id.answer_edit_text_game_fragment);
        questionImage = rootView.findViewById(R.id.question_image);
        questionHint = rootView.findViewById(R.id.hint_image_game_fragment);
        progressBar = rootView.findViewById(R.id.game_fragment_progress_bar);
    }

    private void getHint() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final FetchCurrentQuestion question = retrofit.create(FetchCurrentQuestion.class);
        Call<FetchingCurrentQuestion> call = question.getCurrentQuestion(auth.getCurrentUser().getUid());
        call.enqueue(new Callback<FetchingCurrentQuestion>() {
            @Override
            public void onResponse(Call<FetchingCurrentQuestion> call, Response<FetchingCurrentQuestion> response) {
              if(response.body()!= null) {
                  if (response.body().getPayload().getHint() == null) {
                      fetchHint();
                  } else {
                      hint = response.body().getPayload().getHint();
                      progressBar.setVisibility(View.GONE);
                      makeSnackbar(questionHint, hint);

                  }
              }
              else
              {
                  progressBar.setVisibility(View.GONE);
                  makeSnackbar(questionHint, "Some Error Occured");
              }
            }

            @Override
            public void onFailure(Call<FetchingCurrentQuestion> call, Throwable t) {

            }
        });
    }

    private void fetchHint() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final FetchHint fetch = retrofit.create(FetchHint.class);
        Call<FetchingHint> call = fetch.fetchHint(auth.getCurrentUser().getUid());
        call.enqueue(new Callback<FetchingHint>() {
            @Override
            public void onResponse(Call<FetchingHint> call, Response<FetchingHint> response) {
                hint = response.body().getPayload().getHint();
                progressBar.setVisibility(View.GONE);
                makeSnackbar(questionHint, hint);

            }

            @Override
            public void onFailure(Call<FetchingHint> call, Throwable t) {

            }
        });
    }

    private void checkAnswer() {
        progressBar.setVisibility(View.VISIBLE);
        if(answer.getText()!=null && answer.getText().length()>0) {
            Answer Ans = new Answer(answer.getText().toString());
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            final CheckAnswer ans = retrofit.create(CheckAnswer.class);
            Call<IsAnswerRight> answerCall = ans.checkAnswer(auth.getCurrentUser().getUid(), Ans);
            answerCall.enqueue(new Callback<IsAnswerRight>() {
                @Override
                public void onResponse(Call<IsAnswerRight> call, Response<IsAnswerRight> response) {
                    if (response.code() == 200) {
                        if (response.body().isAnswerCorrect()) {
                            progressBar.setVisibility(View.GONE);
                            makeSnackbar(submit, "Your Answer is Correct!");
                            fetchQuestion();
                            hint=null;
                            answer.setText("");
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        makeSnackbar(submit, "OOPS Wrong Answer!");
                    }
                }

                @Override
                public void onFailure(Call<IsAnswerRight> call, Throwable t) {

                }
            });
        }
        else
        {
            progressBar.setVisibility(View.GONE);
            makeSnackbar(submit, "Enter your answer");
        }
    }

    private void fetchQuestion() {
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        final FetchCurrentQuestion currentQuestion = retrofit.create(FetchCurrentQuestion.class);
        Call<FetchingCurrentQuestion> call = currentQuestion.getCurrentQuestion(auth.getCurrentUser().getUid());
        call.enqueue(new Callback<FetchingCurrentQuestion>() {
            @Override
            public void onResponse(Call<FetchingCurrentQuestion> call, Response<FetchingCurrentQuestion> response)
            {
                if(response.body()!=null)
                {
                    question.setText(response.body().getPayload().getQuestion());
                    questionNumber.setText(String.valueOf(response.body().getPayload().getLevel()));
                    imgURL = response.body().getPayload().getImgURL();
                    Glide.with(getView())
                            .load(imgURL)
                            .into(questionImage);
                    progressBar.setVisibility(View.GONE);
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    makeSnackbar(submit, "Some Error Occured");
                }
            }

            @Override
            public void onFailure(Call<FetchingCurrentQuestion> call, Throwable t) {

            }
        });
    }

    private void makeSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
