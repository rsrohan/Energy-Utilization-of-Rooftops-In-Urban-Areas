package com.example.minorproject2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.example.minorproject2.CameraActivity.MEDIA_TYPE_IMAGE;

public class SendImageActivity extends AppCompatActivity {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 99;
    private static String s;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;

    Button eastImageButton, westImageButton, analyseImageButton, getDetailsButton;
    EditText et_rooftop;
    ImageView eastImage, westImage;

    FirebaseUser user;
    private double latitude = 0, longitude = 0;
    private String TAG = "tag";
    private Button soilDetailButton;

    private int REQUEST_LOCATION = 101;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseReference latReference, longReference, areaReference, postalReference;
    ArrayList<String> images;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);
        user = FirebaseAuth.getInstance().getCurrentUser();
        latReference = FirebaseDatabase.getInstance().getReference("uploadedLat");
        longReference = FirebaseDatabase.getInstance().getReference("uploadedLon");
        areaReference = FirebaseDatabase.getInstance().getReference("uploadedArea");
        postalReference = FirebaseDatabase.getInstance().getReference("uploadedPostalCode");


        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        setViewById();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploadedFromPython");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while we are uploading image...");
        progressDialog.setCancelable(false);
        if (getIntent().getBooleanExtra("imagesClicked", false)) {
            //progressDialog.show();
            eastImageButton.setVisibility(View.GONE);
            images = new ArrayList<>();
            images.add(getIntent().getStringExtra("image1"));
            images.add(getIntent().getStringExtra("image2"));
            images.add(getIntent().getStringExtra("image3"));
            images.add(getIntent().getStringExtra("image4"));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    makeCollage();
                }
            });

        } else {
            if (checkAndRequestPermissions()) {
                if (hasGPSDevice(getApplicationContext())) {
                    enableLoc();
                    getLocation();
                }
            }


            eastImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class).putExtra("requirement", "east"));
                    finish();
                }
            });

        }


    }

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "minor");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
            s = "IMG_" + timeStamp + ".png";
        } else {
            return null;
        }

        return mediaFile;
    }

    private void makeCollage() {
        Bitmap[] parts = new Bitmap[4];

        for (int i = 0; i < images.size(); i++) {
            File imgFile = new File(images.get(i));
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                parts[i] = myBitmap;

            }
        }
        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //Uri file = Uri.fromFile(new File(result+".png"));
        final File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(byteArray);
            fos.close();
            //Toast.makeText(CameraActivity.this, "" + pictureFile, Toast.LENGTH_SHORT).show();
            final StorageReference riversRef = mStorageRef.child(s);
            Glide.with(getApplicationContext()).load(Uri.fromFile(new File(pictureFile.getAbsolutePath()))).into(eastImage);

            progressDialog.dismiss();

            analyseImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (et_rooftop.getText().toString().length() < 1) {
                        Toast.makeText(SendImageActivity.this, "Enter rooftop area", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();

                        areaReference.setValue(et_rooftop.getText().toString());
                        et_rooftop.setFocusable(false);
                        riversRef.putFile((Uri.fromFile(new File(pictureFile.getAbsolutePath()))))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        progressDialog.dismiss();

                                        progressDialog.setMessage("Please wait...");
                                        FirebaseDatabase.getInstance().getReference("uploadedFromPython").child(s.substring(0, s.indexOf("."))).setValue(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                        progressDialog.dismiss();

                                        Log.d(TAG, "onFailure: " + exception.getCause());
                                        Toast.makeText(SendImageActivity.this, "" + exception, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                }
            });


            getDetailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query lastQuery = databaseReference.child("result").orderByKey().limitToLast(1);
                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot data) {
                            progressDialog.dismiss();
                            String result = String.valueOf(Objects.requireNonNull(data.getValue()).toString());
                            result = result.substring(result.indexOf("=") + 1);
                            result = result.substring(0, result.indexOf("}"));
                            Toast.makeText(SendImageActivity.this, "" + result, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                }
            });


        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }

    }

    private Bitmap getYourInputImage() {

        return BitmapFactory.decodeResource(getResources(), R.drawable.rooftop);

    }


    private void setViewById() {
        eastImage = findViewById(R.id.imageEast);
        westImage = findViewById(R.id.imageWest);
        eastImageButton = findViewById(R.id.imageEastButton);
        westImageButton = findViewById(R.id.imageWestButton);
        analyseImageButton = findViewById(R.id.submitImageButton);
        getDetailsButton = findViewById(R.id.soilDetails);
        et_rooftop = findViewById(R.id.et_rooftop);
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

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(SendImageActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(SendImageActivity.this, REQUEST_LOCATION);

                                //finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }


    private void getLocation() {
        final LocationManager manager = (LocationManager) SendImageActivity.this.getSystemService(Context.LOCATION_SERVICE);

        if (manager != null) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(SendImageActivity.this)) {
                //Toast.makeText(SendImageActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                enableLoc();
            } else {

                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                                    List<Address> addresses = null;
                                    try {
                                        addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        if (addresses.size() > 0) {
                                            Log.d(TAG, "onSuccess: " + addresses.get(0).getPostalCode());
                                            latReference.setValue(location.getLatitude() + "");
                                            longReference.setValue(location.getLongitude() + "");
                                            postalReference.setValue(addresses.get(0).getPostalCode());
                                        }


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    Toast.makeText(SendImageActivity.this, "Something went wrong. ERROR: Location NULL", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }
    }

}
