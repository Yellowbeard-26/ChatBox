package com.example.chatbox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegestrationActivity extends AppCompatActivity {
    private static final String TAG = "RegestrationActivity";

    EditText username;
    EditText password;
    EditText Email;
    Button Register;

    FirebaseAuth auth;
    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regestration);

        username=(EditText) findViewById(R.id.editTextTextPersonName);
        password=(EditText) findViewById(R.id.editTextTextPassword);
        Email=(EditText) findViewById(R.id.editTextTextEmailAddress);
        Register=(Button) findViewById(R.id.button);

        auth=FirebaseAuth.getInstance();

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_n=username.getText().toString();
                String Pass_t=password.getText().toString();
                String Email_i=Email.getText().toString();

                if(TextUtils.isEmpty(user_n)||TextUtils.isEmpty(Pass_t)||TextUtils.isEmpty(Email_i))
                {
                    Toast.makeText(RegestrationActivity.this,"PLEASE FILL ALL THE FIELDS ",Toast.LENGTH_SHORT).show();
                }
                else if(Pass_t.length()<=6)
                {
                    Toast.makeText(RegestrationActivity.this,"PASSWORD LENGTH SHOULD BE MORE THAN 6 CHARACHTERS ",Toast.LENGTH_SHORT).show();
                }
                else
                {Log.e(TAG, "onComplete: inside auth" );
                    RegisterNow(user_n , Email_i , Pass_t);
                }
            }
        });


    }
    private void RegisterNow(final String Username , String Emailid , String Password)
    {

        Log.e(TAG, "onComplete: inside registernow" );
        auth.createUserWithEmailAndPassword( Emailid , Password ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    Log.e(TAG, "onComplete: userid = "+userid );

                    myref = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Id", userid);
                    hashMap.put("username", Username);
                    hashMap.put("imageUrl", "default");
                    hashMap.put("Status","Offline");

                    myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(RegestrationActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                        }
                    });

                }
                    else
                    {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());


                        Toast.makeText(RegestrationActivity.this,"Invalid Email or PassWord",Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
}