package com.example.countdayoflove.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.countdayoflove.R;

import java.util.Objects;

public class OpeningScene extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //end
        setContentView(R.layout.activity_opening_scene);
        //TODO  RUN TIME OPENING SCREEN
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                Intent i = new Intent(OpeningScene.this, MainActivity.class);
                startActivity(i);
                finish(); } }, 3000);
    }
    }
