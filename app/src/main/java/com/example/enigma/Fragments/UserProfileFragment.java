package com.example.enigma.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.airbnb.lottie.LottieAnimationView;
import com.example.enigma.Activities.SetUpActivity;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.ChangeProfile;
import com.example.enigma.Interfaces.FetchUserProfile;
import com.example.enigma.Interfaces.RegisteringUser;
import com.example.enigma.Models.ChangeUserName;
import com.example.enigma.Models.FetchingUserProfile;
import com.example.enigma.Models.RegistrationResponse;
import com.example.enigma.Models.UserDetails;
import com.example.enigma.Models.Name;
import com.example.enigma.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileFragment extends Fragment {
    private TextView emailTextView;
    private String username;
    private TextView firstNameTextView;
    private TextView lastNameTextView;
    private TextView usernameTextView;
    private MaterialButton done;
    private FirebaseAuth auth;
    private LottieAnimationView animationView;
    private Call<FetchingUserProfile> call2;
    private Call<RegistrationResponse> call;
    private Call<ChangeUserName> call1;
    private ImageView tint;

    public UserProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initializeViews(rootView);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifyfields())
                {
                    username = usernameTextView.getText().toString();
                    UserDetails details = new UserDetails(username, auth.getCurrentUser().getEmail());
                    Name name = new Name(username);
                    tint.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.VISIBLE);
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    registerUser(details, name);
                }
                else
                {
                    makeSnackbar(done, "Please enter all the fields");
                }
            }
        });
        return rootView;
    }

    private void initializeViews(View rootView) {
        usernameTextView = rootView.findViewById(R.id.username_text_edit_set_profile_fragment);
        done = rootView.findViewById(R.id.done_button_set_profile_fragment);
        auth = FirebaseAuth.getInstance();
        firstNameTextView = rootView.findViewById(R.id.first_name_text_edit_set_profile_fragment);
        lastNameTextView = rootView.findViewById(R.id.last_name_text_edit_set_profile_fragment);
        emailTextView = rootView.findViewById(R.id.email_text_edit_set_profile_fragment);
        emailTextView.setText(auth.getCurrentUser().getEmail());
        emailTextView.setEnabled(false);
        animationView = rootView.findViewById(R.id.lottie_animation_set_up_profile);
        tint = rootView.findViewById(R.id.set_up_profile_tint);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call!=null)
            call.cancel();
        if(call1!=null)
            call1.cancel();
        if(call2!=null)
            call2.cancel();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        tint.setVisibility(View.VISIBLE);
        animationView.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FetchUserProfile userProfile = retrofit.create(FetchUserProfile.class);
        call2 = userProfile.fetchProfile(auth.getCurrentUser().getUid());
        call2.enqueue(new Callback<FetchingUserProfile>() {
            @Override
            public void onResponse(Call<FetchingUserProfile> call, Response<FetchingUserProfile> response) {
                if(response.body()!=null)
                {
                    if(response.body().getPayload().getUser().getName()!=null)
                        {
                            Intent intent = new Intent(getActivity(), WorkingActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                tint.setVisibility(View.INVISIBLE);
                animationView.setVisibility(View.INVISIBLE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }

                @Override
                public void onFailure(Call<FetchingUserProfile> call, Throwable t) {
                    if(call2.isCanceled())
                    {
                        tint.setVisibility(View.INVISIBLE);
                        animationView.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        tint.setVisibility(View.INVISIBLE);
                        animationView.setVisibility(View.INVISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }
            });
        }

    private boolean verifyfields() {
        if(((firstNameTextView.getText()!=null && firstNameTextView.getText().length()>0)
            && (lastNameTextView.getText()!=null && lastNameTextView.getText().length()>0)
            && (usernameTextView.getText()!=null && usernameTextView.getText().length()>0)))

        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void registerUser(UserDetails details, final Name name) {
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        final Retrofit retrofit = builder.build();
        final RegisteringUser user = retrofit.create(RegisteringUser.class);
        call = user.registerPlayer(auth.getCurrentUser().getUid(), details);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, final Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if(registrationResponse.getStatusCode()==200 && (registrationResponse.isRegSuccess() ||
                        registrationResponse.getPayload().getMsg().equals("User already registered, Signing In!")))
                {

                    final ChangeProfile profile = retrofit.create(ChangeProfile.class);
                    call1 = profile.changeUserName(auth.getCurrentUser().getUid(), username);
                    call1.enqueue(new Callback<ChangeUserName>() {
                        @Override
                        public void onResponse(Call<ChangeUserName> call, Response<ChangeUserName> response1) {
                            if(response1.body()!=null) {
                                if (response1.body().getStatusCode() == 200) {
                                    SetUpActivity.getmSwitchToOtherFragments().goToRulesFragment();
                                    tint.setVisibility(View.INVISIBLE);
                                    animationView.setVisibility(View.INVISIBLE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            }
                            else
                            {
                                makeSnackbar(done, "Username Already Taken!");
                                tint.setVisibility(View.INVISIBLE);
                                animationView.setVisibility(View.INVISIBLE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangeUserName> call, Throwable t) {
                                if(call1.isCanceled())
                                {
                                    tint.setVisibility(View.INVISIBLE);
                                    animationView.setVisibility(View.INVISIBLE);
                                }
                                else
                                {
                                    tint.setVisibility(View.INVISIBLE);
                                    animationView.setVisibility(View.INVISIBLE);
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                        }
                    });

                }
                else
                {
                    makeSnackbar(done, response.body().getPayload().getMsg());
                    tint.setVisibility(View.INVISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                if(call.isCanceled())
                {
                    tint.setVisibility(View.INVISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    makeSnackbar(done, "Some Error Occured, Please Try later");
                    tint.setVisibility(View.INVISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                }
            }
        });
    }

    private void makeSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}
