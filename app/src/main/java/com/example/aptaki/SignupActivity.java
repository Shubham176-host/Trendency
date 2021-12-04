package com.example.aptaki;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.app.ToolbarManager;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity
{
    private Button CreateAccountButton;
    private EditText InputName, InputPhonenumber, InputPassword, CInputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CreateAccountButton =(Button) findViewById(R.id.main_signup2_btn);
        InputName =(EditText) findViewById(R.id.User_name);
        InputPhonenumber =(EditText) findViewById(R.id.signup_phone_number);
        InputPassword =(EditText) findViewById(R.id.signup_password);
        CInputPassword = findViewById(R.id.confirm_signup_password);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateAccount();
            }

            private void CreateAccount()
            {
                String name =InputName.getText().toString();
                String phone =InputPhonenumber.getText().toString();
                String password =InputPassword.getText().toString();
                String Cpassword =CInputPassword.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignupActivity.this, "Please Enter your name....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(SignupActivity.this, "Please Enter your phone number....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(SignupActivity.this, "Please Enter your password....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.getTrimmedLength(phone) < 9)
                {
                    Toast.makeText(SignupActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else
                if(!(Cpassword.equals(password)))
                {
                    Toast.makeText(SignupActivity.this, "Confirm Password does not match", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.getTrimmedLength(phone) < 6)
                {
                    Toast.makeText(SignupActivity.this, "Password must be greater than 6 characters", Toast.LENGTH_SHORT).show();
                }
                else
                    if(TextUtils.isDigitsOnly(password))
                    {
                        Toast.makeText(SignupActivity.this, "Password must contain alphabets", Toast.LENGTH_SHORT).show();
                    }
                else
                    {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidatePhoneNumber(name, phone, password);
                }
            }

            private void ValidatePhoneNumber(final String name, final String phone, final String password)
            {
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(!(dataSnapshot.child("Users").child(phone).exists()))
                        {
                            HashMap<String, Object> userdataMap =new HashMap<>();
                            userdataMap.put("phone",phone);
                            userdataMap.put("password",password);
                            userdataMap.put("name",name);

                            RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(SignupActivity.this, "Congragulation your account is created successfully", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                                Intent intent =new Intent(SignupActivity.this , LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                loadingBar.dismiss();
                                                Toast.makeText(SignupActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(SignupActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                            if((dataSnapshot.child("Users").child(phone).exists()))
                        {
                            Toast.makeText(SignupActivity.this, "This "+phone+" already exists", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(SignupActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(SignupActivity.this , LoginActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
