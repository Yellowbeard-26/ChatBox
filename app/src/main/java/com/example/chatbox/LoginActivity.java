package com.example.chatbox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {

    EditText UserName,PassWord;
    Button Login,Not_reg;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserName=(EditText) findViewById(R.id.login_user);
        PassWord=(EditText) findViewById(R.id.Login_pass);
        Login=(Button) findViewById(R.id.button2);
        Not_reg=(Button) findViewById(R.id.button3);

        Not_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegestrationActivity.class);
                startActivity(i);
            }
        });



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_text=UserName.getText().toString();
                String Pass_Text=PassWord.getText().toString();

                if(TextUtils.isEmpty(user_text)||TextUtils.isEmpty(Pass_Text))
                {
                    Toast.makeText(LoginActivity.this,"PLEASE FILL ALL THE FIELDS",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    auth.signInWithEmailAndPassword(user_text,Pass_Text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"LOGIN FAILED!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        auth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null)
        {
            Intent i=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}