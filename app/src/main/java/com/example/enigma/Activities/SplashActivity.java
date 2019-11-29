package com.example.enigma.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.enigma.R;

public class SplashActivity extends AppCompatActivity {

    VideoView video;

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
                    Intent intent = new Intent(SplashActivity.this, LaunchingActivity.class);
                    startActivity(intent);
                    finish();
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
