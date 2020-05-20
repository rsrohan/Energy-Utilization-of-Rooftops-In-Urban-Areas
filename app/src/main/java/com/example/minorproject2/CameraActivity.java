package com.example.minorproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.utils.SpotlightListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SensorEventListener {
    private Camera mCamera;
    private CameraPreview mPreview;
    private String TAG = "Camera";
    ImageButton capture, gallery, switchCam;
    ProgressBar progressBar;

    ImageView compass;
    SensorManager sensorManager;
    LocationManager locationManager;
    TextView imageCount;
    float currentDegree = 0f;
    int count = 1;
    public static ArrayList<String> images = null;
    private FrameLayout preview;
    int imagesToBeTaken;
    TextView top, bottom;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        images = new ArrayList<>();
        capture = findViewById(R.id.capture);
        gallery = findViewById(R.id.gallery);
        switchCam = findViewById(R.id.switchCamera);
        progressBar = findViewById(R.id.progressbar);
        compass = findViewById(R.id.compass);
        imageCount = findViewById(R.id.imageCount);
        top = findViewById(R.id.top);
        bottom = findViewById(R.id.bottom);


        imagesToBeTaken = getIntent().getIntExtra("requirement", 4);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkAndRequestPermissions()) {

            if (checkCameraHardware(getApplicationContext())) {

                mCamera = getCameraInstance();

                // Create our Preview view and set it as the content of our activity.
                mPreview = new CameraPreview(this, mCamera);
                preview = findViewById(R.id.camera);
                preview.addView(mPreview);

                final Activity activity = this;
                new SpotlightView.Builder(this)
                        .introAnimationDuration(400)
                        .enableRevealAnimation(true)
                        .performClick(true)
                        .fadeinTextDuration(400)
                        .headingTvColor(Color.parseColor("#eb273f"))
                        .headingTvSize(32)
                        .headingTvText("Images Count")
                        .subHeadingTvColor(Color.parseColor("#ffffff"))
                        .subHeadingTvSize(16)
                        .subHeadingTvText("Here you will know how many images must be taken to ensure the proper analysis.")
                        .maskColor(Color.parseColor("#dc000000"))
                        .target(imageCount)
                        .lineAnimDuration(400)
                        .lineAndArcColor(Color.parseColor("#eb273f"))
                        .dismissOnTouch(true)
                        .dismissOnBackPress(true)
                        .enableDismissAfterShown(false)
                        .usageId("imageCoun tutorial") //UNIQUE ID
                        .setListener(new SpotlightListener() {
                            @Override
                            public void onUserClicked(String s) {
                                new SpotlightView.Builder(activity)
                                        .introAnimationDuration(400)
                                        .enableRevealAnimation(true)
                                        .performClick(true)
                                        .fadeinTextDuration(400)
                                        .headingTvColor(Color.parseColor("#eb273f"))
                                        .headingTvSize(32)
                                        .headingTvText("Sky Side")
                                        .subHeadingTvColor(Color.parseColor("#ffffff"))
                                        .subHeadingTvSize(16)
                                        .subHeadingTvText("Capture images in a way that this side must be in sky region.")
                                        .maskColor(Color.parseColor("#dc000000"))
                                        .target(top)
                                        .lineAnimDuration(400)
                                        .lineAndArcColor(Color.parseColor("#eb273f"))
                                        .dismissOnTouch(true)
                                        .dismissOnBackPress(true)
                                        .enableDismissAfterShown(false)
                                        .usageId("top tutorial") //UNIQUE ID
                                        .setListener(new SpotlightListener() {
                                            @Override
                                            public void onUserClicked(String s) {
                                                new SpotlightView.Builder(activity)
                                                        .introAnimationDuration(400)
                                                        .enableRevealAnimation(true)
                                                        .performClick(true)
                                                        .fadeinTextDuration(400)
                                                        .headingTvColor(Color.parseColor("#eb273f"))
                                                        .headingTvSize(32)
                                                        .headingTvText("Bottom Side")
                                                        .subHeadingTvColor(Color.parseColor("#ffffff"))
                                                        .subHeadingTvSize(16)
                                                        .subHeadingTvText("This will be the bottom part of image.")
                                                        .maskColor(Color.parseColor("#dc000000"))
                                                        .target(bottom)
                                                        .lineAnimDuration(400)
                                                        .lineAndArcColor(Color.parseColor("#eb273f"))
                                                        .dismissOnTouch(true)
                                                        .dismissOnBackPress(true)
                                                        .enableDismissAfterShown(false)
                                                        .usageId("bottom tutorial") //UNIQUE ID
                                                        .setListener(new SpotlightListener() {
                                                            @Override
                                                            public void onUserClicked(String s) {
                                                                new SpotlightView.Builder(activity)
                                                                        .introAnimationDuration(400)
                                                                        .enableRevealAnimation(true)
                                                                        .performClick(true)
                                                                        .fadeinTextDuration(400)
                                                                        .headingTvColor(Color.parseColor("#eb273f"))
                                                                        .headingTvSize(32)
                                                                        .headingTvText("Capture")
                                                                        .subHeadingTvColor(Color.parseColor("#ffffff"))
                                                                        .subHeadingTvSize(16)
                                                                        .subHeadingTvText("Tap here to click picture.")
                                                                        .maskColor(Color.parseColor("#dc000000"))
                                                                        .target(capture)
                                                                        .lineAnimDuration(400)
                                                                        .lineAndArcColor(Color.parseColor("#eb273f"))
                                                                        .dismissOnTouch(true)
                                                                        .dismissOnBackPress(true)
                                                                        .enableDismissAfterShown(false)
                                                                        .usageId("click tutorial") //UNIQUE ID
                                                                        .show();
                                                            }
                                                        })
                                                        .show();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .show();
                capture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.setVisibility(View.VISIBLE);
                        mCamera.takePicture(null, null, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                                if (pictureFile == null) {
                                    Log.d(TAG, "Error creating media file, check storage permissions");
                                    return;
                                }

                                try {
                                    FileOutputStream fos = new FileOutputStream(pictureFile);
                                    fos.write(data);
                                    fos.close();
                                    //Toast.makeText(CameraActivity.this, "" + pictureFile, Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    images.add(pictureFile.toString());
                                    count++;
                                    if (images.size() == imagesToBeTaken) {
                                        if (imagesToBeTaken==3)
                                        { startActivity(new Intent(getApplicationContext(), InputRooftopAreaActivity.class)
                                                .putExtra("imagesClicked", 3)
                                                .putExtra("image1", images.get(0))
                                                .putExtra("image2", images.get(1))
                                                .putExtra("image3", images.get(2)));
                                        }else{
                                            startActivity(new Intent(getApplicationContext(), InputRooftopAreaActivity.class)
                                                    .putExtra("imagesClicked", 4)
                                                    .putExtra("image1", images.get(0))
                                                    .putExtra("image2", images.get(1))
                                                    .putExtra("image3", images.get(2))
                                                    .putExtra("image4", images.get(3)));
                                        }

                                        finish();
                                    } else {
                                        imageCount.setText("Image " + count);
                                        mCamera = getCameraInstance();
                                        mPreview = new CameraPreview(getApplicationContext(), mCamera);
                                        //preview = findViewById(R.id.camera);
                                        preview.addView(mPreview);
                                    }

                                } catch (FileNotFoundException e) {
                                    Log.d(TAG, "File not found: " + e.getMessage());
                                } catch (IOException e) {
                                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

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


    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;
        private String TAG = "CAMERA PREVIEW";

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;


            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

            if (mHolder.getSurface() == null) {
                return;
            }

            try {
                mCamera.stopPreview();
            } catch (Exception e) {
            }


            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.setDisplayOrientation(90);
                Camera.Parameters p = mCamera.getParameters();
                p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                mCamera.setParameters(p);
                mCamera.startPreview();

            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);


        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        compass.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
        sensorManager.unregisterListener((SensorEventListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        } catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onResume: " + e);
        }
        try {
            //mCamera = getCameraInstance();

        } catch (Exception ignored) {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }


    private double getDirection(double lat1, double lng1, double lat2, double lng2) {

        double PI = Math.PI;
        double dTeta = Math.log(Math.tan((lat2 / 2) + (PI / 4)) / Math.tan((lat1 / 2) + (PI / 4)));
        double dLon = Math.abs(lng1 - lng2);
        double teta = Math.atan2(dLon, dTeta);
        double direction = Math.round(Math.toDegrees(teta));
        return direction; //direction in degree

    }
}
