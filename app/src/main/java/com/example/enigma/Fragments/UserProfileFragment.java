package com.example.enigma.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.enigma.Activities.SetUpActivity;
import com.example.enigma.Activities.WorkingActivity;
import com.example.enigma.BuildConfig;
import com.example.enigma.Interfaces.FetchUserProfile;
import com.example.enigma.Interfaces.RegisteringUser;
import com.example.enigma.Models.FetchingUserProfile;
import com.example.enigma.Models.RegistrationResponse;
import com.example.enigma.Models.UserDetails;
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
    private ProgressBar pBar;

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
                    pBar.setVisibility(View.VISIBLE);
                    registerUser(details);
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
        pBar = rootView.findViewById(R.id.profile_progress_bar);
    }

    @Override
    public void onStart() {
        super.onStart();
        pBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        FetchUserProfile userProfile = retrofit.create(FetchUserProfile.class);
        Call<FetchingUserProfile> call = userProfile.fetchProfile(auth.getCurrentUser().getUid());
        call.enqueue(new Callback<FetchingUserProfile>() {
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
                    pBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<FetchingUserProfile> call, Throwable t) {
                    pBar.setVisibility(View.INVISIBLE);
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

    private void registerUser(UserDetails details) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        RegisteringUser user = retrofit.create(RegisteringUser.class);
        Call<RegistrationResponse> call = user.registerPlayer(auth.getCurrentUser().getUid(), details);
        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if(registrationResponse.getStatusCode()==200 && (registrationResponse.isRegSuccess() ||
                        registrationResponse.getPayload().getMsg().equals("User already registered, Signing In!")))
                {
                    SetUpActivity.getmSwitchToOtherFragments().goToRulesFragment();
                    pBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    makeSnackbar(done, response.body().getPayload().getMsg());
                    pBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                makeSnackbar(done, "Some Error Occured, Please Try later");
                pBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void makeSnackbar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
                .show();
    }
}
