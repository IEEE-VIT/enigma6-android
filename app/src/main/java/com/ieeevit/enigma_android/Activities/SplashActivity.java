package com.ieeevit.enigma_android.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.ieeevit.enigma_android.R;

public class SplashActivity extends AppCompatActivity {

    private VideoView video;
    private SharedPreferences  sharedPreferences;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (getPackageName() != null) {
            video = findViewById(R.id.video);
            video.setKeepScreenOn(true);
            video.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.intro);
            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if(flag == 0) {
                    Intent intent = new Intent(SplashActivity.this, SetUpActivity.class);
                    startActivity(intent);
                    finish(); }
                    else
                    {
                        Intent intent = new Intent(SplashActivity.this, LaunchingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            video.start();
            video.requestFocus();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        video.start();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        flag = sharedPreferences.getInt("Finished", 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        video.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        video.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.stopPlayback();
    }
}
