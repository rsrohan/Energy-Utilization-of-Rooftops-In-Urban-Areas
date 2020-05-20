package com.example.minorproject2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {


    EditText phone;
    Button sendOTP;
    String regEx = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.phoneNumber);
        sendOTP = findViewById(R.id.sendOTP);
        activity = this;

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Pattern.compile(regEx).matcher(phone.getText().toString()).matches())
                {
                    final OTPDialog otpDialog = new OTPDialog(LoginActivity.this, phone.getText().toString(), activity);
                    otpDialog.setCancelable(false);
                    otpDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (otpDialog.isNewUser())
                            {

                                ProfileDialog profileDialog = new ProfileDialog(activity, null);
                                profileDialog.setCancelable(false);
                                profileDialog.show();
                                profileDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                        finish();
                                    }
                                });
                            }else{
                                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                finish();
                            }
                        }
                    });
                    otpDialog.show();

                }else{
                    Toast.makeText(LoginActivity.this, "ARE YOU SURE THIS NUMBER IS VALID ?", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
