package com.anshu.safeoverse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;

public class EmergencyAlerts extends AppCompatActivity {
    TextView nameTextView;
    ImageView profileImage;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
RecyclerView recyclerView;
String[] newsDescriptions = {"Following its presence in the Kadma Biodiversity Park, " +
        "a leopard has been reported in Sonari, sparking fear among locals." +
        " Residents alerted the Forest Department and administration after discovering evidence of the" +
        " leopard’s visit to the northern layout of Sonari, where it had apparently killed and partially " +
        "consumed a pig overnight.  Concerned citizens reported the discovery of a pig’s half-eaten carcass," +
        " with one half severed. The Forest Department promptly dispatched a team to " +
        "investigate the incident and collect samples from the remaining portion of the pig for analysis.","A trader had a narrow escape while his 37-year-old wife was shot dead when unidentified " +
        "assailants opened fire on the couple at Kanderbera Chowk on NH-33 under the Chandil police station " +
        "limits in Seraikela-Kharsawan district on Friday.","Following a three-week international break, Jamshedpur FC will look to leave behind their 0-3 loss to Mumbai City FC" +
        "as Indian Super League (ISL) resumes its business end with a double header on Saturday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alerts);
        nameTextView = findViewById(R.id.nameMain2);
        profileImage = findViewById(R.id.profile_image2);
        sharedPreferences = getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        nameTextView.setText(sharedPreferences.getString("nameStr","User"));
        sharedPreferences = getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loadImageFromStorage(sharedPreferences.getString("ProfilePath","Null"));
        recyclerView = findViewById(R.id.alertRecyclerView);
        ArrayList<newsFeed> arrayList = new ArrayList<newsFeed>();
        arrayList.add(new newsFeed("Leopard in Jamshedpur !",
                newsDescriptions[0],
                "https://firebasestorage.googleapis.com/v0/b/shieldme-eb123.appspot.com/o/leopard.jpeg?alt=media&token=173cf698-293b-4a31-b600-2a40a2e9490d"));
        arrayList.add(new newsFeed("Jamshedpur trader shot at, wife killed",newsDescriptions[1],
                "https://firebasestorage.googleapis.com/v0/b/shieldme-eb123.appspot.com/o/killed.jpeg?alt=media&token=c7033576-74fa-44ab-9ce7-b4e0c584690a"));
        arrayList.add(new newsFeed("Jamshedpur FC Aims for Redemption as ISL Resumes Business End After International Break",
                newsDescriptions[2],"https://firebasestorage.googleapis.com/v0/b/shieldme-eb123.appspot.com/o/jsrbusiness.jpg?alt=media&token=19ea7709-2266-44e7-8e68-cd3c4466fbe9"));
        EmergencyAlertAdapter adapter = new EmergencyAlertAdapter(this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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