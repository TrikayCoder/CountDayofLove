package com.example.countdayoflove.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.countdayoflove.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {
    private final String fileName = "internalStorage.txt";
    private final String filePath = "ThuMucCuaToi";
    File myInternalFile;

    EditText typeDate;
    ImageButton save, gotoSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //end
        setContentView(R.layout.activity_main2);
        //TODO CHECK THE EXISTENCE OF FILE
        //IF FILE NOT EXIT, CREATE NEW FILE
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(filePath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory, fileName);
        setup();

        //TODO GOTO SETTING ACTIVITY
        gotoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mg2 = new Intent(MainActivity2.this, SettingActivity.class);
                startActivity(mg2);
            }
        });

        //TODO BUTTON SAVE DATE
        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (formatCheck()){
                    writeFile();
                    Toast.makeText(MainActivity2.this,"Saved",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity2.this,"Error!",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //TODO METHOD WRITE DATA OVERRIDING TO FILE
    private void writeFile(){
        try {
            //Mở file
            FileOutputStream fos = new FileOutputStream(myInternalFile);
            //Ghi dữ liệu vào file
            fos.write(typeDate.getText().toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkString(String s){
        int checkTemp = 0;
        for(int i=0; i<s.length();i++){
            if(s.charAt(i) >= 'a' && s.charAt(i) <= 'z' ){
                checkTemp++;
            }
            if(s.charAt(i) >= 'A' && s.charAt(i) <= 'Z' ){
                checkTemp++;
            }
        }
        if(checkTemp == 0){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkNumber(String s){
        int checkTemp = 0;
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) >= '0' && s.charAt(i) <= '9'){
                checkTemp += 1;
            }
        }
        if (checkTemp != 0){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkSpec(){
        int checkTemp = 0;
        String s = typeDate.getText().toString();
        for(int i=0;i<s.length();i++){
            if(s.charAt(i) == '/'){
                checkTemp++;
            }
        }
        if(checkTemp != 2){
            return false;
        } else{
            return true;
        }
    }
    //TODO CHECK DATE TYPE STRING TRUE OR FALSE
    private boolean formatCheck(){
        int checkFinaly = 0;
        String s = typeDate.getText().toString();
        if (s.equals("")){
            return false;
        }
        else if(!checkSpec()){
            return false;
        }
        else {
            String[] temp = s.split("/");
            if (checkString(temp[0])) {
                int day = Integer.parseInt(temp[0]);
                if (day >  0){
                    if (checkString(temp[1])) {
                        int month = Integer.parseInt(temp[1]);
                        if (month >  0){
                            if (checkString(temp[2])) {
                                int year = Integer.parseInt(temp[2]);
                                if (year >  0){
                                    checkFinaly += 1;
                                }
                            }
                        }
                    }
                }
            }

        }
        if(checkFinaly != 0){
            return true;
        }else{
            return false;
        }
    }
    //TODO SETUP INITIAL VARIABLE
    public void setup(){
        typeDate = findViewById(R.id.type_date);
        save = findViewById(R.id.save_date);
        gotoSetting = findViewById(R.id.gotoSettingfromActivity2);
    }
}
