package com.trikay.fallinginlove.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.trikay.fallinginlove.R;
import com.trikay.fallinginlove.logic.GetDay;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

public class MainActivity extends AppCompatActivity {
    //DataBase
    String DATABASE_NAME = "SQLDemNgayYeu.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    //Controls
    TextView txtDayOfLove,
             txtDayBeginLove,
             txtNameAdam,
             txtNameEva;
    CircleImageView imgAvatarAdam,
                    imgAvatarEva;

    //Variables
    String dbTxtNameAdam = "";
    String dbTxtNameEva = "";
    Bitmap bitmapImageAvatarAdam = null;
    Bitmap bitmapImageAvatarEva = null;

    //DatePicker
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        //end
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        processCopy();
        getRawDataFromDB();
        setUpDataUser();
    }

    public void addControls() {
        txtDayOfLove = findViewById(R.id.txtDayOfLove);
        txtDayBeginLove = findViewById(R.id.txtDayBeginLove);
        txtNameAdam = findViewById(R.id.txtNameAdam);
        txtNameEva = findViewById(R.id.txtNameEva);
        imgAvatarAdam = findViewById(R.id.imgAvatarAdam);
        imgAvatarEva = findViewById(R.id.imgAvatarEva);
    }

    private void setUpDataUser() {
        txtNameAdam.setText(dbTxtNameAdam);
        txtNameEva.setText(dbTxtNameEva);
        if (bitmapImageAvatarAdam != null) {
            imgAvatarAdam.setImageBitmap(bitmapImageAvatarAdam);
        }
        if (bitmapImageAvatarEva != null) {
            imgAvatarEva.setImageBitmap(bitmapImageAvatarEva);
        }
    }

    private void getRawDataFromDB() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("DemNguoiYeuUser", null, null, null, null, null, null);
        cursor.moveToNext();
        dbTxtNameAdam = cursor.getString(0);
        dbTxtNameEva = cursor.getString(1);
        String pathImageAvatarAdam = cursor.getString(4);
        if (!pathImageAvatarAdam.equals("None")) {
            Uri uriPathImageAvatarAdam = Uri.parse(pathImageAvatarAdam);
            bitmapImageAvatarAdam = uriToBitmap(uriPathImageAvatarAdam);
        }
        String pathImageAvatarEva = cursor.getString(5);
        if (!pathImageAvatarEva.equals("None")) {
            Uri uriPathImageAvatarEva = Uri.parse(pathImageAvatarEva);
            bitmapImageAvatarEva = uriToBitmap(uriPathImageAvatarEva);
        }
        String dateRaw = cursor.getString(6);
        try {
            if (dateRaw.equals("0/0/0")) {
                Toast.makeText(MainActivity.this, "Bạn vui lòng vào chỉnh sữa để đặt ngày đôi bạn đến với nhau nhé", Toast.LENGTH_SHORT).show();
            } else {
                GetDay getDay = new GetDay();
                txtDayBeginLove.setText(dateRaw);
                String[] dateProcessed = dateRaw.split("/");
                int day = Integer.parseInt(dateProcessed[0]);
                int month = Integer.parseInt(dateProcessed[1]);
                int year = Integer.parseInt(dateProcessed[2]);
                int result = getDay.finalyday(day, month, year) + 1;
                txtDayOfLove.setText(Html.fromHtml("Đã Yêu" + "<br>" + result + " <br>" + "Ngày"));
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString() + "getRawDataFromDB()", Toast.LENGTH_SHORT).show();
        }
    }

    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void copyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    //TODO permissionRequestChooseAvatar(View view)
    public void permissionRequestChooseAvatar(View view) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                chooseAvatar(view);
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
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    // convert from bitmap to byte array
    public byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public void chooseAvatar(View view) {
        TedBottomPicker.OnImageSelectedListener listener = new TedBottomPicker.OnImageSelectedListener() {
            @Override
            public void onImageSelected(Uri uri) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    String pathBitmap = getImageUri(MainActivity.this, bitmap);
                    ContentValues contentValues = new ContentValues();
                    if (view.getId() == R.id.imgAvatarAdam) {
                        bitmapImageAvatarAdam = bitmap;
                        contentValues.put("AvatarAdam", pathBitmap);
                        int ret = database.update("DemNguoiYeuUser", contentValues, "AgeAdam=?", new String[]{"18"});
                    } else if (view.getId() == R.id.imgAvatarEva) {
                        bitmapImageAvatarEva = bitmap;
                        contentValues.put("AvatarEva", pathBitmap);
                        int ret = database.update("DemNguoiYeuUser", contentValues, "AgeAdam=?", new String[]{"18"});
                    }
                    ThreadAvatarChooseUpdateUI threadAvatarChooseUpdateUI = new ThreadAvatarChooseUpdateUI();
                    threadAvatarChooseUpdateUI.execute();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        try {
            TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(MainActivity.this)
                    .setOnImageSelectedListener(listener)
                    .create();
            tedBottomPicker.show(getSupportFragmentManager());
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public String getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    }

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void btnOpenMenuSettingMainActivity(View view) {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, view, Gravity.CENTER_VERTICAL);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_setting_mainactivity, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        popup.show(); //showing popup menu
    }
    String[] s = {""};
    public void showDatePicker() {
        DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int YEAR, int MONTH, int DAY_OF_MONTH) {
                s[0] = DAY_OF_MONTH +"/" + (MONTH+1) +"/" + YEAR;
                database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                ContentValues contentValues = new ContentValues();
                contentValues.put("Date", s[0]);
                int ret = database.update("DemNguoiYeuUser", contentValues, "AgeAdam=?", new String[]{"18"});
                if(ret> 0){
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    ThreadDatePickerUpdateUI threadDatePickerUpdateUI = new ThreadDatePickerUpdateUI();
                    threadDatePickerUpdateUI.execute();
                }else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                callBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH) );
        datePickerDialog.show();
    }
    //TODO BUTTONS CLICK
    public void menuSettingMainActivityClick(MenuItem item) {
        if(item.getItemId() == R.id.menuSettingMainActivity_Item_One){
            showDatePicker();
        }else if(item.getItemId() == R.id.menuSettingMainActivity_Item_Two){
            Toast.makeText(MainActivity.this,
                    "You Choose Two",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void txtNameClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Tên Của Bạn Hoặc Người Ấy");
        alertDialog.setMessage("Mời Bạn Nhập Tên Bên Dưới Nhé");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.edit_button);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String nameTemp = input.getText().toString();
                        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                        ContentValues contentValues = new ContentValues();
                        if(view.getId() == R.id.txtNameAdam){
                            contentValues.put("NameAdam", nameTemp);
                        }else if(view.getId() == R.id.txtNameEva) {
                            contentValues.put("NameEva", nameTemp);
                        }
                        int ret = database.update("DemNguoiYeuUser", contentValues, "AgeAdam=?", new String[]{"18"});
                        if(ret>0){
                            ThreadNameTypeUpdateUI threadNameTypeUpdateUI = new ThreadNameTypeUpdateUI();
                            threadNameTypeUpdateUI.execute();
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public void txtDayOfLoveClick(View view) {
        showDatePicker();
    }


    //TODO THREADS
    @SuppressLint("StaticFieldLeak")
    class ThreadNameTypeUpdateUI extends AsyncTask<Void, String, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            txtNameAdam.setText(values[0]);
            txtNameEva.setText(values[1]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.query("DemNguoiYeuUser", null, null, null, null, null, null);
            cursor.moveToNext();
            publishProgress(cursor.getString(0),cursor.getString(1));
            return null;
        }
    }
    @SuppressLint("StaticFieldLeak")
    class ThreadAvatarChooseUpdateUI extends  AsyncTask<Void, Bitmap, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }

        @Override
        protected void onProgressUpdate(Bitmap... bitmap) {
            super.onProgressUpdate(bitmap);
            if(bitmap[0] != null){
                imgAvatarAdam.setImageBitmap(bitmap[0]);
            }
            if(bitmap[1] != null){
                imgAvatarEva.setImageBitmap(bitmap[1]);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.query("DemNguoiYeuUser", null, null, null, null, null, null);
            cursor.moveToNext();

            Bitmap bitmapAdam = null;
            if(!cursor.getString(4).equals("None")){
               bitmapAdam = uriToBitmap(Uri.parse(cursor.getString(4)));
            }
            Bitmap bitmapEva = null;
            if(!cursor.getString(5).equals("None")){
                bitmapEva = uriToBitmap(Uri.parse(cursor.getString(5)));
            }
            publishProgress(bitmapAdam, bitmapEva);
            return null;
        }
    }
    @SuppressLint("StaticFieldLeak")
    class ThreadDatePickerUpdateUI extends AsyncTask<Void, String, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            txtDayOfLove.setText(Html.fromHtml("Đã Yêu"+"<br>"+ values[0] + " <br>"+"Ngày"));
            txtDayBeginLove.setText(values[1]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.query("DemNguoiYeuUser", null, null, null, null, null, null);
            cursor.moveToNext();
            String dateRaw = cursor.getString(6);
            String result = "";
                if (dateRaw.equals("0/0/0")) {
                    Toast.makeText(MainActivity.this, "Bạn vui lòng vào chỉnh sữa để đặt ngày đôi bạn đến với nhau nhé", Toast.LENGTH_SHORT).show();
                } else {
                    GetDay getDay = new GetDay();
                    String[] dateProcessed = dateRaw.split("/");
                    int day = Integer.parseInt(dateProcessed[0]);
                    int month = Integer.parseInt(dateProcessed[1]);
                    int year = Integer.parseInt(dateProcessed[2]);
                    result = String.valueOf(getDay.finalyday(day, month, year) + 1);
                }
            publishProgress(result,dateRaw);
            return null;
        }
    }
}