package com.example.minorproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import java.util.Objects;

import static com.example.minorproject2.CameraActivity.MEDIA_TYPE_IMAGE;

public class InputRooftopAreaActivity extends AppCompatActivity {

    private static String s;
    private Button analyseImageButton;
    private EditText et_rooftop;
    int imagesCount = 0;
    FirebaseUser user;

    DatabaseReference areaReference;
    ArrayList<String> images;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef;
    private String TAG = "tag";
    ImageView img1, img2, img3, img4, iv_step;
    HorizontalScrollView scrollView;
    TextView tv_step, tv_retake;
    Bitmap imagesToUpload[] = new Bitmap[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_rooftop_area);
        analyseImageButton = findViewById(R.id.submitImageButton);
        et_rooftop = findViewById(R.id.et_rooftop);
        img1 = findViewById(R.id.image1);
        img2 = findViewById(R.id.image2);
        img3 = findViewById(R.id.image3);
        img4 = findViewById(R.id.image4);
        tv_step = findViewById(R.id.tv_step);
        iv_step = findViewById(R.id.iv_step);
        tv_retake = findViewById(R.id.tv_retake);
        scrollView = findViewById(R.id.scrollView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        images = new ArrayList<>();
        mStorageRef = FirebaseStorage.getInstance().getReference(Objects.requireNonNull(user.getPhoneNumber())).child("uploadedFromPython");
        areaReference = FirebaseDatabase.getInstance().getReference(user.getPhoneNumber()).child("uploadedArea");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading Image...");
        progressDialog.setCancelable(false);
        imagesCount = getIntent().getIntExtra("imagesClicked", 4);
        if (imagesCount == 3) {
            images.add(getIntent().getStringExtra("image1"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(0)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[0] = resource;
                    img1.setImageBitmap(resource);
                    return true;
                }
            }).submit();
            images.add(getIntent().getStringExtra("image2"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(1)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[1] = resource;
                    img2.setImageBitmap(resource);
                    return true;
                }
            }).submit();
            images.add(getIntent().getStringExtra("image3"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(2)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[2] = resource;
                    img3.setImageBitmap(resource);
                    return true;
                }
            }).submit();
            findViewById(R.id.cvimage4).setVisibility(View.GONE);
        } else {
            images.add(getIntent().getStringExtra("image1"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(0)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[0] = resource;
                    img1.setImageBitmap(resource);
                    return true;
                }
            }).submit();
            images.add(getIntent().getStringExtra("image2"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(1)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[1] = resource;
                    img2.setImageBitmap(resource);
                    return true;
                }
            }).submit();
            images.add(getIntent().getStringExtra("image3"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(2)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[2] = resource;
                    img3.setImageBitmap(resource);
                    return true;
                }
            }).submit();
            images.add(getIntent().getStringExtra("image4"));
            Glide.with(getApplicationContext()).asBitmap().load(images.get(3)).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imagesToUpload[3] = resource;
                    img4.setImageBitmap(resource);
                    return true;
                }
            }).submit();

        }

        analyseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_rooftop.getText().toString().length() < 1) {
                    Toast.makeText(InputRooftopAreaActivity.this, "Enter rooftop area", Toast.LENGTH_SHORT).show();
                    et_rooftop.requestFocus();
                } else {
                    progressDialog.show();
                    areaReference.setValue(et_rooftop.getText().toString());
                    et_rooftop.setFocusable(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadImage();
                        }
                    });
                }
            }
        });

        tv_retake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SendImageActivity.class));
                finish();
            }
        });


    }



    private void uploadImage() {
        FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user.getPhoneNumber())).child("uploadedCount").setValue(images.size());

        for (int i = 0; i < images.size(); i++) {

            final int x = images.size()-i-1;

            String timeStamp = String.valueOf(System.currentTimeMillis());
            timeStamp = "IMG_"+timeStamp;

            final String extension = images.get(i);
//            Uri selectedImage = Uri.parse(images.get(i));
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            Objects.requireNonNull(cursor).moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
            final File imgFile = new File(images.get(i));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imagesToUpload[i].compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            imagesToUpload[i].recycle();
            if (imgFile.exists()) {
                final StorageReference riversRef = mStorageRef.child(timeStamp + "" + extension.substring(extension.indexOf(".")));
                try {
                    final String finalTimeStamp = timeStamp;
                    riversRef.putBytes(byteArray)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user.getPhoneNumber())).child("uploadedFromPython").child(finalTimeStamp).setValue(finalTimeStamp + "" + extension.substring(extension.indexOf(".")));
                                    if (x == imagesCount - 1) {
                                        progressDialog.dismiss();
                                        et_rooftop.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.GONE);
                                        tv_step.setText("Done !");
                                        iv_step.setImageDrawable(getResources().getDrawable(R.drawable.rooftop_image2));
                                        analyseImageButton.setText("Browse plants");
                                        tv_retake.setVisibility(View.GONE);
                                        analyseImageButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                progressDialog.setMessage("Please wait...");
                                                progressDialog.show();
                                                try{
                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(user.getPhoneNumber());
                                                    Query lastQuery = databaseReference.child("result").orderByKey().limitToLast(1);
                                                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot data) {
                                                            progressDialog.dismiss();
                                                            String result = String.valueOf(Objects.requireNonNull(data.getValue()).toString());
                                                            result = result.substring(result.indexOf("=") + 1);
                                                            result = result.substring(0, result.indexOf("}"));
                                                            Log.d(TAG, "onDataChange: " + result);
                                                            if (result.equals("NULL")) {
                                                                Toast.makeText(InputRooftopAreaActivity.this, "Sorry no plants found in our database.", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                startActivity(new Intent(getApplicationContext(), ResultActivity.class).putExtra("result", result));
                                                                finish();
                                                            }
                                                            //Toast.makeText(InputRooftopAreaActivity.this, "" + result, Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }catch (Exception e){
                                                    Toast.makeText(InputRooftopAreaActivity.this, "Ruko Zara ! Sabr Karo...", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                                    } else {
                                        progressDialog.setMessage("Uploading Image...");

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                    if (x == imagesCount) {
                                        progressDialog.dismiss();
                                    }
                                    Log.d(TAG, "onFailure: " + exception.getCause());
                                    Toast.makeText(InputRooftopAreaActivity.this, "" + exception, Toast.LENGTH_SHORT).show();
                                }
                            });


                } catch (Exception e) {
                    Log.d(TAG, "Error accessing file: " + e.getMessage());
                }

            }
        }


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





    }

}
