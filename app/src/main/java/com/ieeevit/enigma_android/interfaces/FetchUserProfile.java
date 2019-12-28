package com.ieeevit.enigma_android.interfaces;

import com.ieeevit.enigma_android.models.FetchingUserProfile;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchUserProfile {
    @POST("/api/auth/profile")
    Call<FetchingUserProfile> fetchProfile(@Header("Authorization")String uid);
}
