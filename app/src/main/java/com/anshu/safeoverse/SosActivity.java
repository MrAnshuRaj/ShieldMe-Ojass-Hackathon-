package com.anshu.safeoverse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SosActivity extends AppCompatActivity {
    String[] emergencyList = {"1. Police: 100",
            "2. Fire: 101",
            "3. Ambulance: 102",
            "4. Women's Helpline: 1091",
            "5. National Emergency Response System (NERS): 112",
            "6. Child Helpline: 1098",
            "7. Disaster Management: 108",
            "8. Anti-Poison (New Delhi): 1066 or 011-1066",
            "9. Anti-Stalking/Cybercrime Helpline: 1090",
            "10. Road Accident Emergency Service: 1073",
            "11. Railway Helpline: 1512",
            "12. Medical Helpline: 104",
            "13. Air Ambulance: 9540161344",
            "14. Helpline for Children in Distress: 1098",
            "15. Senior Citizens Helpline: 1090",
            "16. Blood Bank Emergency Number: 1910",
            "17. Coastal Guard: 1554",
            "18. Women’s Helpline Against Domestic Violence: 181"};
    long[] emNos = {100,101,102,1091,112,1098,108,1066,1090,1073,1512,104, 9540161344L,1098,1090,1910,1554,181};
    ListView emergencyListView;
    EditText emergencyPhoneNo;
    Button saveButton;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView profileImageView;
    TextView nameTxtView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        emergencyListView=findViewById(R.id.EmergencyNamesList);
        emergencyPhoneNo=findViewById(R.id.emergencyInput);
        sharedPreferences=getSharedPreferences("shieldMe",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        profileImageView =findViewById(R.id.profile_image_sos);
        nameTxtView = findViewById(R.id.nameSos);
        nameTxtView.setText(sharedPreferences.getString("nameStr","User"));
        loadImageFromStorage(sharedPreferences.getString("ProfilePath","Null"));
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SosActivity.this,ProfileActivity.class));
            }
        });
        saveButton=findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("EmergencyNumber",emergencyPhoneNo.getText().toString());
                editor.apply();
                Toast.makeText(SosActivity.this,"Emergency number saved successfully",Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.listcard,R.id.textView6,
                emergencyList);
        emergencyListView.setAdapter(arrayAdapter);
        String storedNo=sharedPreferences.getString("EmergencyNumber","Null");
        if(!storedNo.equals("Null")) {
            emergencyPhoneNo.setText(storedNo);
        }
        emergencyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = emergencyList[position];
                long emergencyNo = emNos[position];
                //showing dialog to check whether user should call or not
                new AlertDialog.Builder(SosActivity.this).setTitle("Alert").
                        setMessage("Do you want to call "+emergencyNo).
                        setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + emergencyNo));
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
                return true;
            }
        });

    }
    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

            profileImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace(System.out);
            Log.w("File","file load error");
        }

    }
}