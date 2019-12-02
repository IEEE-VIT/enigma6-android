package com.ieeevit.enigma_android.Interfaces;

import com.ieeevit.enigma_android.Models.FetchingCurrentQuestion;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchCurrentQuestion {

    @POST("/api/auth/getCurrent")
    Call<FetchingCurrentQuestion> getCurrentQuestion(@Header("Authorization")String uid);
}
