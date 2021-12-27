package com.example.countdayoflove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView day_display, date_display;
    ImageButton gotoSetting;
    private String filename = "internalStorage.txt";
    private String filepath = "ThuMucCuaToi";
    File myInternalFile;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //end
        setContentView(R.layout.activity_main);

        setup();

        //TODO CHECK THE EXISTENCE OF FILE
        //IF FILE NOT EXIT, CREATE NEW FILE
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(filepath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory, filename);
        GetDay gd = new GetDay();

        //TODO SHOW THE NUMBER OF LOVED DAYS
        String s = ReadFile();
        //IF S IS EMPTY
        if(s.equals("")){
            day_display.setText("0");
            date_display.setText("0/0/0");
        }
        // IF S NOT EMPTY
        else {
            date_display.setText(s);
            String[] words = s.split("/");
            int day = Integer.parseInt(words[0]);
            int month = Integer.parseInt(words[1]);
            int year = Integer.parseInt(words[2]);
            int result = gd.finalyday(day, month, year) + 1;
            String display = String.valueOf(result);
            day_display.setText("Đã Bên Nhau " + display + " Ngày");

        }
        //-----------------------------------
        //TODO GOTO SETTING ACTIVITY
        gotoSetting.setOnClickListener(view -> {
            Intent mg1 = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(mg1);
        });
    }
    //TODO READ FILE TXT (SAVE DATE)
    private String ReadFile(){
        String str ="";
        try {
            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            str += br.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    //TODO SETUP INITIAL VARIABLE
    public void setup(){
        date_display = (TextView) findViewById(R.id.date_watch) ;
        day_display = (TextView) findViewById(R.id.dayoflove);
        gotoSetting = (ImageButton) findViewById(R.id.gotoSetting);
    }
}