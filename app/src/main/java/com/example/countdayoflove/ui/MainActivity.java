package com.example.countdayoflove.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.countdayoflove.R;
import com.example.countdayoflove.logic.GetDay;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import gun0912.tedbottompicker.TedBottomPicker;

public class MainActivity extends AppCompatActivity {

    TextView dayDisplay, dateDisplay;
    ImageView avartarAdam, avartarEva;
    ImageButton gotoSetting;
    OutputStream outputStream;
    private String fileName = "internalStorage.txt";
    private String filePath = "ThuMucCuaToi";
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
        permissionRequest();
        //TODO CHECK THE EXISTENCE OF FILE
        //IF FILE NOT EXIT, CREATE NEW FILE
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getDir(filePath, Context.MODE_PRIVATE);
        myInternalFile = new File(directory, fileName);
        GetDay gd = new GetDay();

        //TODO SHOW THE NUMBER OF LOVED DAYS
        String dateRaw = ReadFile();
        //IF S IS EMPTY
        if(dateRaw.equals("")){
            dayDisplay.setText("0");
            dateDisplay.setText("0/0/0");
        }
        // IF S NOT EMPTY
        else {
            dateDisplay.setText(dateRaw);
            String[] dateProcessed = dateRaw.split("/");
            int day = Integer.parseInt(dateProcessed[0]);
            int month = Integer.parseInt(dateProcessed[1]);
            int year = Integer.parseInt(dateProcessed[2]);
            int result = gd.finalyday(day, month, year) + 1;
            String display = String.valueOf(result);
            dayDisplay.setText(Html.fromHtml("Đã Yêu"+"<br>"+ display + " <br>"+"Ngày"));

        }
        //-----------------------------------
        //TODO GOTO EDIT ACTIVITY
        gotoSetting.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        });
    }
    //TODO PERMISSON
    public void permissionRequest(){
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    //TODO READ FILE TXT (SAVE DATE)
    private String ReadFile(){
        String str ="";
        try {
            FileInputStream fis = new FileInputStream(myInternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            //str is raw date
            str += br.readLine();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    //TODO SETUP INITIAL VARIABLE
    public void setup(){
        dateDisplay = (TextView) findViewById(R.id.date_watch) ;
        dayDisplay = (TextView) findViewById(R.id.dayoflove);
        gotoSetting = (ImageButton) findViewById(R.id.gotoSetting);
        avartarAdam = (ImageView) findViewById(R.id.avartarAdam);
        avartarEva = (ImageView) findViewById(R.id.avartarEva);
    }

    public void chanceAvartar(View view) {

        TedBottomPicker.OnImageSelectedListener listener = new  TedBottomPicker.OnImageSelectedListener(){
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    if(view.getId() == R.id.avartarAdam){
                        avartarAdam.setImageBitmap(bitmap);
                    }else if(view.getId() == R.id.avartarEva){
                        avartarEva.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(MainActivity.this)
                .setOnImageSelectedListener(listener)
                .create();
        tedBottomPicker.show(getSupportFragmentManager());
    }
}