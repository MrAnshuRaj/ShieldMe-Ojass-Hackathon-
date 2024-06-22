package com.anshu.safeoverse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
CardView sosCard, safetyCheckInCard, floatingCard, emergencyAlert, mentalHealth, CyberThreats, Volunteering;
ImageView profileImage;
Button sosButton;
SharedPreferences sharedPreferences;
SharedPreferences.Editor editor;
boolean messagePermissionGranted=false;
TextView nameTxtView;

private static final int PERMISSION_REQUEST_SEND_SMS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sosCard = findViewById(R.id.sos);
        emergencyAlert =findViewById(R.id.emergencyAlert);
        emergencyAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EmergencyAlerts.class));
            }
        });
        mentalHealth = findViewById(R.id.mentalHealth);
        mentalHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg();
            }
        });
        CyberThreats = findViewById(R.id.CyberThreats);
        CyberThreats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg();
            }
        });
        Volunteering = findViewById(R.id.Volunteering);
        Volunteering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMsg();
            }
        });
        safetyCheckInCard = findViewById(R.id.safetyCheckIn);
        sharedPreferences = getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nameTxtView =findViewById(R.id.nameMain);
        profileImage = findViewById(R.id.profile_image);
        loadImageFromStorage(sharedPreferences.getString("ProfilePath","Null")); //image loaded
        nameTxtView.setText(sharedPreferences.getString("nameStr","User"));
        safetyCheckInCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SafetyCheckIn.class));
            }
        });
        sosCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SosActivity.class));
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });
        floatingCard = findViewById(R.id.floatingCard);
        floatingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=jLYECy2-t-0"));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);
            }
        });
        sosButton=findViewById(R.id.sosButton);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"SOS action initiated",Toast.LENGTH_LONG).show();
                if(messagePermissionGranted)
                {
                    String number = sharedPreferences.getString("EmergencyNumber","100");
                    sendSMS(number);
                }
            }
        });
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            messagePermissionGranted = false;
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS);
        } else {
            // Permission is granted, send the SMS
            messagePermissionGranted=true;
           // sendSMS("1234567890", "This is the message text."); // Replace "1234567890" with the recipient's number
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send the SMS
                messagePermissionGranted=true;
                //sendSMS("1234567890", "This is the message text."); // Replace "1234567890" with the recipient's number
            } else {
                // Permission denied, display a toast message
                messagePermissionGranted=false;
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMS(String number) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, "SOS received from Anshu Raj", null, null);
        Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
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
    public void showMsg()
    {
        Toast.makeText(this,"Feature coming soon...",Toast.LENGTH_SHORT).show();
    }
}