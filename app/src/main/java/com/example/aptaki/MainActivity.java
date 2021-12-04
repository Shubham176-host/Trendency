package com.example.aptaki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.aptaki.Model.Users;
import com.example.aptaki.Prevelent.Prevelent;
import com.example.aptaki.User.HomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button signinbtn, signupbtn;
    private ProgressDialog loadingBar;
    private RelativeLayout logodisplay, x022221;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logodisplay = (RelativeLayout) findViewById(R.id.logo_display);
        x022221 = (RelativeLayout) findViewById(R.id.xx021);
        signinbtn = (Button) findViewById(R.id.main_login_btn);
        signupbtn = (Button) findViewById(R.id.main_signup_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevelent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevelent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "")
        {
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey, UserPasswordKey);

                x022221.setVisibility(View.INVISIBLE);
                logodisplay.setVisibility(View.VISIBLE);
                loadingBar.setTitle("Loging In");
                loadingBar.setMessage("Please wait...");
                loadingBar.setCanceledOnTouchOutside(false);

            }
        }
    }


    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {
                    Users userData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (userData.getPhone().equals(phone)) {
                        if (userData.getPassword().equals(password)) {

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevelent.currentonlineUser = userData;
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Retry Login", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with " + phone + " does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}



