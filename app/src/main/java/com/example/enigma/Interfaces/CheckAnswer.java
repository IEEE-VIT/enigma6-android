package com.example.enigma.Interfaces;

import com.example.enigma.Models.Answer;
import com.example.enigma.Models.IsAnswerRight;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CheckAnswer {
    @POST("/api/auth/checkAnswer")
    Call<IsAnswerRight> checkAnswer(@Header("Authorization") String uid,
                                    @Body Answer answer);
}
