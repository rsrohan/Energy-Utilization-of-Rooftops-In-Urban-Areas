package com.example.minorproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SendImageActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 99;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    Button eastImageButton, westImageButton, analyseImageButton;

    ImageView eastImage, westImage;

    FirebaseUser user;
    private double latitude=0, longitude=0;
    private String TAG="tag";
    private Button soilDetailButton;

    String soilDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null)
        {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        setViewById();

        if (checkAndRequestPermissions()) {
            turnGPSOn();


            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
            mGoogleApiClient.connect();
        }

        try{
            Uri uri = Uri.parse(getIntent().getStringExtra("path"));
            Glide.with(getApplicationContext()).asBitmap().load(uri).addListener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    Log.d(TAG, "onLoadFailed: "+e);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    eastImage.setImageBitmap(resource);
                    Log.d(TAG, "onResourceReady: "+resource);
                    return false;
                }
            }).submit();
            //eastImage.setImageURI(uri);
            Log.d(TAG, "onNewIntent: "+uri);
            eastImage.setVisibility(View.VISIBLE);
        }catch (Exception e){
            Log.d(TAG, "onNewIntent: "+e);
        }
            eastImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("requirement", "east"));
                finish();
            }
        });
        westImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("requirement", "west"));
            }
        });

        analyseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SendImageActivity.this, "ANALYZE", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);




    }

    private void setViewById() {
        eastImage = findViewById(R.id.imageEast);
        westImage = findViewById(R.id.imageWest);
        eastImageButton = findViewById(R.id.imageEastButton);
        westImageButton = findViewById(R.id.imageWestButton);
        analyseImageButton = findViewById(R.id.submitImageButton);
        soilDetailButton = findViewById(R.id.soilDetails);
    }
    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    private void turnGPSOn() {
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);

        }
    }
    private void runLocationTracker() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {

                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                }
            }
        }, 5000);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        runLocationTracker();

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(this, ""+connectionResult, Toast.LENGTH_SHORT).show();
    }

}
