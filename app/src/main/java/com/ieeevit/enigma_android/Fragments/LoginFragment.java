package com.ieeevit.enigma_android.Fragments;

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
import com.ieeevit.enigma_android.Activities.SetUpActivity;
import com.ieeevit.enigma_android.Activities.WorkingActivity;
import com.ieeevit.enigma_android.BuildConfig;
import com.ieeevit.enigma_android.Interfaces.FetchUserProfile;
import com.ieeevit.enigma_android.Models.FetchingUserProfile;
import com.ieeevit.enigma_android.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private MaterialButton googleLogin;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextView forgotPassword;
    private MaterialButton login;
    private TextView signUp;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String emailText;
    private String passwordText;
    private Call<FetchingUserProfile> call;
    private LottieAnimationView animationView;
    private ImageView tint;

    public LoginFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initialize(rootView);
        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleLoginProcess();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normalLogin();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetUpActivity.getmSwitchToOtherFragments().goToSignUpFragment();
            }
        });
        return rootView;
    }


    private void initialize(View rootView) {
        googleLogin = rootView.findViewById(R.id.login_google_button);
        email = rootView.findViewById(R.id.email_text_edit_login_fragment);
        password = rootView.findViewById(R.id.password_text_edit_login_fragment);
        forgotPassword = rootView.findViewById(R.id.forgot_password_text_view);
        login = rootView.findViewById(R.id.login_button_login_fragment);
        signUp = rootView.findViewById(R.id.sign_up_text_view_login_fragment);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(Objects.requireNonNull(getActivity()), gso);
        auth = FirebaseAuth.getInstance();
        animationView = rootView.findViewById(R.id.lottie_animation);
        tint = rootView.findViewById(R.id.login_tint);
    }

    @Override
    public void onStart() {
        super.onStart();
            user = auth.getCurrentUser();
            if (user != null && user.isEmailVerified()) {
                tint.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.VISIBLE);
                animationView.setSpeed(1);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                FetchUserProfile userProfile = retrofit.create(FetchUserProfile.class);
                call = userProfile.fetchProfile(auth.getCurrentUser().getUid());
                call.enqueue(new Callback<FetchingUserProfile>() {
                    @Override
                    public void onResponse(Call<FetchingUserProfile> call, Response<FetchingUserProfile> response) {
                        if (response.body() != null) {
                            if (response.body().getPayload().getUser().getName() != null) {
                                if (getActivity() != null) {
                                    Intent intent = new Intent(getActivity(), WorkingActivity.class);
                                    animationView.setVisibility(View.GONE);
                                    tint.setVisibility(View.INVISIBLE);
                                    if (getActivity() != null) {
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            }
                        }
                    }

                        @Override
                        public void onFailure (Call < FetchingUserProfile > call, Throwable t){
                            if (call.isCanceled()) {
                                animationView.setVisibility(View.GONE);
                                tint.setVisibility(View.INVISIBLE);
                            } else {
                                animationView.setVisibility(View.GONE);
                                tint.setVisibility(View.INVISIBLE);
                                if (getActivity() != null)
                                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        }
                    });
                }
            }

        private void changePassword () {
            SetUpActivity.getmSwitchToOtherFragments().openForgotPasswordBottomSheet();
        }

        private void googleLoginProcess () {
                animationView.setVisibility(View.VISIBLE);
                tint.setVisibility(View.VISIBLE);
                if (getActivity() != null) {
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
        }

        private void normalLogin () {
                tint.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.VISIBLE);
                if (getActivity() != null)
                    getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                emailText = email.getText().toString();
                passwordText = password.getText().toString();

                if ((email.getText() != null && emailText.length() > 0)
                        && (password.getText() != null && passwordText.length() > 0)) {
                    auth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (Objects.requireNonNull(auth.getCurrentUser()).isEmailVerified()) {
                                            SetUpActivity.getmSwitchToOtherFragments().goToUserProfileFragment();
                                        } else {
                                            Snackbar.make(login, "Verify Email", Snackbar.LENGTH_SHORT)
                                                    .setAction("Resend", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            user = auth.getCurrentUser();
                                                            user.sendEmailVerification()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                makeSnackbar("Email sent again", login);

                                                                            } else {
                                                                                makeSnackbar("Some Error Occured Try Later", login);
                                                                            }
                                                                        }
                                                                    });
                                                        }
                                                    })
                                                    .show();
                                            tint.setVisibility(View.INVISIBLE);
                                            animationView.setVisibility(View.INVISIBLE);
                                            if (getActivity() != null)
                                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    } else {
                                        makeSnackbar("Wrong Details", login);
                                        tint.setVisibility(View.INVISIBLE);
                                        animationView.setVisibility(View.INVISIBLE);
                                        if (getActivity() != null)
                                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    }
                                }
                            });
                } else {
                    makeSnackbar("Please enter all the fields", login);
                    tint.setVisibility(View.INVISIBLE);
                    animationView.setVisibility(View.INVISIBLE);
                    if (getActivity() != null)
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == RC_SIGN_IN) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        tint.setVisibility(View.INVISIBLE);
                        animationView.setVisibility(View.INVISIBLE);
                        if (getActivity() != null)
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        e.printStackTrace();
                    }
                }
        }

        private void firebaseAuthWithGoogle (GoogleSignInAccount acct){
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                auth.signInWithCredential(credential)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    SetUpActivity.getmSwitchToOtherFragments().goToUserProfileFragment();
                                    tint.setVisibility(View.INVISIBLE);
                                    animationView.setVisibility(View.INVISIBLE);
                                    if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                } else {
                                    makeSnackbar("Some Error Occured Try After some time", googleLogin);
                                    tint.setVisibility(View.INVISIBLE);
                                    animationView.setVisibility(View.INVISIBLE);
                                    if (getActivity() != null)
                                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                }
                            }
                        });
        }

        private void makeSnackbar (String message, View view){
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
