package com.ieeevit.enigma_android.Interfaces;

import com.ieeevit.enigma_android.Models.FetchingLeaderboard;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchLeaderBoard {
    @POST("/api/auth/leaderBoard")
    Call<FetchingLeaderboard> fetchLeaderBoard(@Header("Authorization") String uid);
}
