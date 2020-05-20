package com.example.minorproject2;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfileDialog extends Dialog implements DialogInterface.OnClickListener {

    EditText username;

    TextView message;
    ProgressBar progressBar;
    Button save;
    Context context;
    String name;
    DatabaseReference reference;
    FirebaseUser user;

    public ProfileDialog(@NonNull Context context, String name) {
        super(context);
        this.name = name;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.userprofiledialog);
        username = findViewById(R.id.username);
        save = findViewById(R.id.save);
        message = findViewById(R.id.message);
        progressBar = findViewById(R.id.progressbar);

        if (name != null) {
            username.setText(name);
        }
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(user.getPhoneNumber())).child("USERNAME");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().isEmpty()) {
                    try {
                        save.setVisibility(View.GONE);
                        message.setText(R.string.pleasewait);
                        username.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        reference.setValue(username.getText().toString()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "SOMETHING WENT WRONG" + e, Toast.LENGTH_SHORT).show();
                                save.setVisibility(View.VISIBLE);
                                message.setText(R.string.entername);
                                username.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dismiss();
                            }
                        });

                    } catch (Exception e) {
                        Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


}

