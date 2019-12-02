package com.ieeevit.enigma_android.Interfaces;

import com.ieeevit.enigma_android.Models.FetchingUserProfile;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchUserProfile {
    @POST("/api/auth/profile")
    Call<FetchingUserProfile> fetchProfile(@Header("Authorization")String uid);
}
