package com.example.chatroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


     EditText uname,pass;
     ProgressBar progressBar;
     FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();

        //if user is already login then open groupchat activity automaticaly
        if(auth.getCurrentUser()!=null)
        {
            Intent i=new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(i);
        }
        else
        {
            setContentView(R.layout.activity_main);
            uname=findViewById(R.id.edLoginUname);

            pass=findViewById(R.id.edLoginPass);
            progressBar=findViewById(R.id.progressBar);

        }

    }

    public void loginUser(View v)
    {
          progressBar.setVisibility(View.VISIBLE);

          String name=uname.getText().toString();
          String password=pass.getText().toString();

          if(!name.equals("") && !password.equals(""))
          {
              auth.signInWithEmailAndPassword(name,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {


                      if(task.isSuccessful())
                      {
                          progressBar.setVisibility(View.GONE);
                          Toast.makeText(getApplicationContext(), "Logged in Sucessfully.", Toast.LENGTH_SHORT).show();

                          Intent i=new Intent(MainActivity.this,GroupChatActivity.class);
                          startActivity(i);
                      }
                      else
                      {
                          Toast.makeText(getApplicationContext(), "Wrong Credentials!! Try Again.", Toast.LENGTH_SHORT).show();
                          progressBar.setVisibility(View.GONE);
                      }
                  }
              });

          }

    }

    public  void gotoRegister(View v)
    {
        Intent i=new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    public void  forgetPassword(View v)
    {
       AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);

       LinearLayout container=new LinearLayout(MainActivity.this);
       container.setOrientation(LinearLayout.VERTICAL);
       LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
       ip.setMargins(50,0,0,100);

        final EditText input=new EditText(MainActivity.this);
         input.setLayoutParams(ip);
         input.setGravity(Gravity.TOP|Gravity.START);
         input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
         input.setLines(1);
         input.setMaxLines(1);

         container.addView(input,ip);

         alert.setMessage("Enter your Registered Username/Email");
         alert.setTitle("Forgot Password");
         alert.setView(container);

         alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(final DialogInterface dialog, int which) {

                 String entered_email=input.getText().toString();

                 auth.sendPasswordResetEmail(entered_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful())
                         {
                             dialog.dismiss();
                             Toast.makeText(getApplicationContext(), "Email sent to registered mail", Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
         });
    }
}
