package com.ieeevit.enigma_android.interfaces;

import com.ieeevit.enigma_android.models.FetchingLeaderboard;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchLeaderBoard {
    @POST("/api/auth/leaderBoard")
    Call<FetchingLeaderboard> fetchLeaderBoard(@Header("Authorization") String uid);
}
