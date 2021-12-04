package com.example.aptaki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aptaki.Prevelent.Prevelent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText InputName, InputNumber, InputPassword;
    private Button changePassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        InputName = (EditText) findViewById(R.id.User_name);
        InputNumber = (EditText) findViewById(R.id.login_phone_number);
        InputPassword = (EditText) findViewById(R.id.login_password);
        loadingBar = new ProgressDialog(this);

        changePassword = (Button) findViewById(R.id.change_password_btn);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CreateAccount();
            }

            public void CreateAccount()
            {
                String name =InputName.getText().toString();
                String phone =InputNumber.getText().toString();
                String password =InputPassword.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter your name....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter your phone number....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Please Enter your password....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.getTrimmedLength(phone) < 9)
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.getTrimmedLength(phone) < 6)
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Password must be greater than 6 characters", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Changing Password");
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
                        if(dataSnapshot.child("Users").child(phone).child(name).exists()) {

                            HashMap<String, Object> userdataMap = new HashMap<>();
                            userdataMap.put("password",password);

                            RootRef.child("Users").child(phone).updateChildren(userdataMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(ForgotPasswordActivity.this, "Password chsnged successfully", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();

                                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            } else {
                                                loadingBar.dismiss();
                                                Toast.makeText(ForgotPasswordActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(ForgotPasswordActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                        if(!(dataSnapshot.child(name).exists()))
                        {
                            Toast.makeText(ForgotPasswordActivity.this, "This User name :"+name+" dosent match", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(ForgotPasswordActivity.this, "Please request developers for password", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(ForgotPasswordActivity.this , MainActivity.class);
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
