package com.ieeevit.enigma_android.interfaces;

import com.ieeevit.enigma_android.models.RegistrationResponse;
import com.ieeevit.enigma_android.models.UserDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RegisteringUser {
    @POST("api/registerPlayer")
    Call<RegistrationResponse> registerPlayer(
            @Header("Authorization") String uid,
            @Body UserDetails details);
}


