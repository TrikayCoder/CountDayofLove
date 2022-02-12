package com.example.countdayoflove.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.countdayoflove.R;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    ImageButton gotoMain2, gotoMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //end
        setContentView(R.layout.activity_setting);
        setup();
        gotoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg1 = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(mg1);
            }
        });
        gotoMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg1 = new Intent(SettingActivity.this, MainActivity2.class);
                startActivity(mg1);
            }
        });
    }
    public void setup(){
        gotoMain = (ImageButton) findViewById(R.id.gotoActivity);
        gotoMain2 = (ImageButton) findViewById(R.id.gotoMain2);
    }
}