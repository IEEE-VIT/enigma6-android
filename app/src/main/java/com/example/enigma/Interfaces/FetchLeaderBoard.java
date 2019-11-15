package com.example.enigma.Interfaces;

import com.example.enigma.Models.FetchingLeaderboard;
import com.example.enigma.Models.Leaderboard;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface FetchLeaderBoard {
    @POST("/api/auth/leaderBoard")
    Call<FetchingLeaderboard> fetchLeaderBoard(@Header("Authorization") String uid);
}
