package com.anshu.safeoverse;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    EditText nameLogin, numberLogin, cityLogin, countryLogin;
    private static final int SELECT_PICTURE=200;
    Button buttonLogin;
    String nameStr, numberStr, cityStr, countryStr;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    HashMap<String, Object> user = new HashMap<>();
    ProgressBar progressBar;
    ImageView profilePic;
    ImageButton selectImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        nameLogin = findViewById(R.id.nameLogin);
        numberLogin = findViewById(R.id.numberLogin);
        cityLogin = findViewById(R.id.cityLogin);
        countryLogin = findViewById(R.id.countryLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        sharedPreferences = getSharedPreferences("shieldMe", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        profilePic = findViewById(R.id.imageView2);
        progressBar =findViewById(R.id.progressBar);
        selectImage =findViewById(R.id.imageButton);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDP();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                nameStr = nameLogin.getText().toString();
                numberStr = numberLogin.getText().toString();
                cityStr = cityLogin.getText().toString();
                countryStr = countryLogin.getText().toString();
                editor.putString("nameStr", nameStr);
                editor.putString("numberStr", numberStr);
                editor.putString("cityStr", cityStr);
                editor.putString("countryStr", countryStr);
                editor.apply();
                user.put("nameStr", nameStr);
                user.put("numberStr", numberStr);
                user.put("cityStr", cityStr);
                user.put("countryStr", countryStr);
                getCoordinates(cityStr,countryStr);
            }
        });

    }
    public void getCoordinates(String city, String country) {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.api-ninjas.com/v1/geocoding?city=" + city + "&country=" + country; // Replace this with your API URL
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Parse JSON response
                            JSONObject jsonObject = response.getJSONObject(0);
                            String name = ((JSONObject) jsonObject).getString("name");
                            double latitude = jsonObject.getDouble("latitude");
                            double longitude = jsonObject.getDouble("longitude");
                            String country = jsonObject.getString("country");
                            user.put("latitude",String.valueOf(latitude));
                            user.put("longitude",String.valueOf(longitude));
                            //when we get latitude longitude then update database
                            db.collection(numberStr).document("userData").set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(LoginScreen.this, MainActivity.class));
                                    Toast.makeText(LoginScreen.this,
                                            "Logged in successfully!",
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                    editor.putBoolean("LoggedIn",true);
                                    editor.apply();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginScreen.this,
                                            "Unable to add user. Try again later!",
                                            Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error fetching JSON data: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("X-Api-Key", "OPakK7lmBBhCx+Lakh1IGQ==14OypK9nRf0bFDPG");
                return headers;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sharedPreferences.getBoolean("LoggedIn",false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
    public void updateDP()
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profilePic.setImageURI(selectedImageUri);
                    try {
                        //storing image in bitmap and saving its path to shared preferences
                        Bitmap selectedImageBitmap= MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(),
                                selectedImageUri);
                        String path= saveToInternalStorage(selectedImageBitmap); //local method

                        //saving path in shared preferences
                        editor.putString("ProfilePath",path);
                        editor.apply();

                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                    }
                }
            }
        }
    }
    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourApp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
        return directory.getAbsolutePath();
    }
}
