package com.example.countdayoflove;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {
    ImageButton goto_main2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //end
        setContentView(R.layout.activity_setting);
        setup();
        goto_main2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg1 = new Intent(SettingActivity.this, MainActivity2.class);
                startActivity(mg1);
            }
        });
    }
    public void setup(){
        goto_main2 = (ImageButton) findViewById(R.id.gotoMain2);
    }
}