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
import android.widget.TextView;
import android.widget.Toast;

import com.example.aptaki.Admin.AdminCategoryActivity;
import com.example.aptaki.Admin.AdminSignupActivity;
import com.example.aptaki.Model.Users;
import com.example.aptaki.Prevelent.Prevelent;
import com.example.aptaki.User.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import com.rey.material.widget.CheckBox;

public class LoginActivity extends AppCompatActivity
{
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink,SignupAdminLink, forgotPassword;

    public String ParentDbName= "Users";
    private CheckBox chckboxremeberme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton =(Button) findViewById(R.id.second_login_btn);
        InputNumber =(EditText) findViewById(R.id.login_phone_number);
        InputPassword =(EditText) findViewById(R.id.login_password);
        AdminLink=(TextView) findViewById(R.id.admin_panel_link);
        SignupAdminLink=(TextView)findViewById(R.id.admin_siguppanel_link);
        NotAdminLink=(TextView) findViewById(R.id.not_admin_panel_link);
        forgotPassword= (TextView) findViewById(R.id.forgot_password);
        loadingBar = new ProgressDialog(this);

        chckboxremeberme = (CheckBox) findViewById(R.id.CheckBox_RememberME);
        chckboxremeberme.setChecked(true);
        Paper.init(LoginActivity.this);

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login Admin");
                forgotPassword.setVisibility(View.INVISIBLE);
                chckboxremeberme.setVisibility(View.INVISIBLE);
                chckboxremeberme.setChecked(false);
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                SignupAdminLink.setVisibility(View.VISIBLE);


                ParentDbName="Admin";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login");
                forgotPassword.setVisibility(View.VISIBLE);
                chckboxremeberme.setVisibility(View.VISIBLE);
                chckboxremeberme.setChecked(true);
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                SignupAdminLink.setVisibility(View.INVISIBLE);

                ParentDbName="Users";
            }
        });

        SignupAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, AdminSignupActivity.class);
                startActivity(intent);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }

            private void LoginUser()
            {
                String phone =InputNumber.getText().toString();
                String password= InputPassword.getText().toString();

                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(LoginActivity.this, "Please Enter your phone number....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginActivity.this, "Please Enter your password....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.getTrimmedLength(phone) < 9)
                {
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    AllowAccess(phone, password);
                }
            }

            private void AllowAccess(final String phone, final String password)
            {

                if(chckboxremeberme.isChecked())
                {
                    Paper.book().write(Prevelent.UserPhoneKey, phone);
                    Paper.book().write(Prevelent.UserPasswordKey, password);
                }
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if(dataSnapshot.child(ParentDbName).child(phone).exists())
                        {
                            Users userData= dataSnapshot.child(ParentDbName).child(phone).getValue(Users.class);

                            if(userData.getPhone().equals(phone)) {

                                if (userData.getPassword().equals(password)) {
                                    if (ParentDbName.equals("Admin")) {

                                        if (!(dataSnapshot.child(ParentDbName).child(phone).child("status").exists())) {
                                            Toast.makeText(LoginActivity.this, "Admin Logged In successfully", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                            startActivity(intent);
                                        } else
                                            {
                                            Toast.makeText(LoginActivity.this, "Your account is not verified", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                            }
                                    } else if (ParentDbName.equals("Users")) {
                                        Toast.makeText(LoginActivity.this, "Logged In successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        Prevelent.currentonlineUser = userData; //s
                                        startActivity(intent);

                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Account with "+phone+" does not exist", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent1);
    }
}

