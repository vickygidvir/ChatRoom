package com.example.chatroom;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText textEmail,textPass,textName;

    ProgressBar progressBar;
    FirebaseAuth auth;
    DatabaseReference reference;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        textEmail=findViewById(R.id.edRegisterEmail);
        textPass=findViewById(R.id.edRegisterPass);
        textName=findViewById(R.id.edRegisterName);
        progressBar=findViewById(R.id.progressBarRegister);

            auth=FirebaseAuth.getInstance();
            reference= FirebaseDatabase.getInstance().getReference().child("Users");
        }

    public void registerUser(View view)
    {

        progressBar.setVisibility(View.GONE);

        final String email=textEmail.getText().toString();
        final String password=textPass.getText().toString();
        final String name=textName.getText().toString();

        if (!email.equals("") && !password.equals("") &&  password.length() > 6)
        {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete( Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser firebaseUser=auth.getCurrentUser();
                        User u=new User();
                        u.setName(name);
                        u.setEmail(email);

                        reference.child(firebaseUser.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(), "User registered sucessfully", Toast.LENGTH_SHORT).show();
                                     progressBar.setVisibility(View.GONE);
                                     finish();
                                    Intent i=new Intent(RegisterActivity.this,GroupChatActivity.class);
                                    startActivity(i);
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "User can't be created", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                }
            });
        }

        else
        {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }

    }

    public void gotoLogin(View v)
    {
        Intent i=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(i);
    }
}
