package com.anshu.safeoverse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SafetyCheckIn extends AppCompatActivity {
EditText eventName,destinationAddress , startTime, endTime, alertTime,alertMessage, date;
String eventNameStr,  destinationAddressStr, startTimeStr, endTimeStr, alertTimeStr, alertMessageStr, dateStr;
int startTimeHrs,startTimeMins, startTimeSecs, endTimeHrs,endTimeMins,endTimeSecs;
Button submitButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView profileImage;
    TextView nameTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_check_in);
        eventName = findViewById(R.id.eventName);
        destinationAddress = findViewById(R.id.destinationAddress);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        alertTime = findViewById(R.id.alertTime);
        alertMessage = findViewById(R.id.alertMessage);
        nameTxtView= findViewById(R.id.nameCheckIn);
        profileImage = findViewById(R.id.profile_image_checkin);
        date = findViewById(R.id.date);
        sharedPreferences = getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventNameStr=eventName.getText().toString();
                destinationAddressStr=destinationAddress.getText().toString();
                startTimeStr= startTime.getText().toString();
                endTimeStr=endTime.getText().toString();
                alertTimeStr=alertTime.getText().toString();
                alertMessageStr=alertMessage.getText().toString();
                dateStr = date.getText().toString();
            }
        });
        nameTxtView.setText(sharedPreferences.getString("nameStr","User"));
        sharedPreferences = getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor = sharedPreferences.edit();
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