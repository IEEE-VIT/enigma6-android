package com.ieeevit.enigma_android.interfaces;

import com.ieeevit.enigma_android.models.FetchingCurrentQuestion;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchCurrentQuestion {

    @POST("/api/auth/getCurrent")
    Call<FetchingCurrentQuestion> getCurrentQuestion(@Header("Authorization")String uid);
}
