package com.example.enigma.Interfaces;

import com.example.enigma.Models.FetchingHint;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchHint {

    @POST("/api/auth/hintClicked")
    Call<FetchingHint> fetchHint(@Header("Authorization") String uid);
}
