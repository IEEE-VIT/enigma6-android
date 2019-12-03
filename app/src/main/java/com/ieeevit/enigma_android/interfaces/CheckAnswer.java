package com.ieeevit.enigma_android.interfaces;

import com.ieeevit.enigma_android.models.Answer;
import com.ieeevit.enigma_android.models.IsAnswerRight;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CheckAnswer {
    @POST("/api/auth/checkAnswer")
    Call<IsAnswerRight> checkAnswer(@Header("Authorization") String uid,
                                    @Body Answer answer);
}
