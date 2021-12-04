package com.example.aptaki.Admin;

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

import com.example.aptaki.LoginActivity;
import com.example.aptaki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminSignupActivity extends AppCompatActivity
{
    private Button CreateAccountButton;
    private EditText InputName, InputPhonenumber, InputPassword, InputEmailid, InputPanNumber, InputAddress, InputPincode;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        CreateAccountButton =(Button) findViewById(R.id.admin_signup_btn);
        InputName =(EditText) findViewById(R.id.admin_name);
        InputPhonenumber =(EditText) findViewById(R.id.signup_phone_number);
        InputPassword =(EditText) findViewById(R.id.signup_password);
        InputAddress=(EditText) findViewById(R.id.admin_shop_address);
        InputEmailid=(EditText) findViewById(R.id.admin_emailid);
        InputPanNumber=(EditText) findViewById(R.id.admin_pancard_number);
        InputPincode=(EditText) findViewById(R.id.admin_adress_pincode);
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
                String address =InputAddress.getText().toString();
                String pincode =InputPincode.getText().toString();
                String pannum =InputPanNumber.getText().toString();
                String email =InputEmailid.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your name....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(phone))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your phone number....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your password....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(address))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your address....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(pincode))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your Pincode....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(pannum))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your PAN number....", Toast.LENGTH_SHORT).show();
                }
                else
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(AdminSignupActivity.this, "Please Enter your email....", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Creating Account");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidatePhoneNumber(name, phone, password, email, pannum, pincode, address);
                }
            }

            private void ValidatePhoneNumber(final String name, final String phone, final String password, final String email, final String pannum, final String pincode, final String address )
            {
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(!(dataSnapshot.child("Admin").child(phone).exists()))
                        {
                                if(!(dataSnapshot.child("Admin").child(pannum).exists()))
                                {
                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("phone", phone);
                                    userdataMap.put("password", password);
                                    userdataMap.put("name", name);
                                    userdataMap.put("status", "not verified");
                                    userdataMap.put("address", address);
                                    userdataMap.put("pincode", pincode);
                                    userdataMap.put("email", email);
                                    userdataMap.put("pannum", pannum);

                                    RootRef.child("Admin").child(phone).updateChildren(userdataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(AdminSignupActivity.this, "Congragulation your account is created successfully", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();

                                                        Intent intent = new Intent(AdminSignupActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                    } else {
                                                        loadingBar.dismiss();
                                                        Toast.makeText(AdminSignupActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(AdminSignupActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(AdminSignupActivity.this, "This "+pannum+" already exists", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    Toast.makeText(AdminSignupActivity.this, "Please contact the customer service", Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(AdminSignupActivity.this , LoginActivity.class);
                                    startActivity(intent);
                                }
                        }
                        else
                        {
                            Toast.makeText(AdminSignupActivity.this, "This"+phone+" already exists", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Toast.makeText(AdminSignupActivity.this, "Please try again using a new number", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(AdminSignupActivity.this , LoginActivity.class);
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
