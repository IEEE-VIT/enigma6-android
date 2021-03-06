package com.ieeevit.enigma_android.interfaces;
import com.ieeevit.enigma_android.models.ChangeUserName;

import retrofit2.Call;
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