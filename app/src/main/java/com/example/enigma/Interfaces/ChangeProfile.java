package com.example.enigma.Interfaces;
import com.example.enigma.Models.ChangeUserName;
import com.example.enigma.Models.Name;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ChangeProfile {

    @FormUrlEncoded
    @POST("/api/auth/changeProfile")
    Call<ChangeUserName> changeUserName(
            @Header("Authorization") String uid,
            @Field("name") String name
    );

}