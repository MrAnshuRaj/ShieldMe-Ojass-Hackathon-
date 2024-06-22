package com.anshu.safeoverse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ProfileActivity extends AppCompatActivity {
ImageView profileImage;
TextView nameTxtView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage = findViewById(R.id.profile_image);
        nameTxtView = findViewById(R.id.textView8);
        sharedPreferences = getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nameTxtView.setText(sharedPreferences.getString("nameStr","User"));
        loadImageFromStorage(sharedPreferences.getString("ProfilePath","Null"));
    }
    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            profileImage.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace(System.out);
            Log.w("File","file load error");
        }

    }
}