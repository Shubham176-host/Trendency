package com.example.aptaki.User;

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
import com.example.aptaki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ContactActivity extends AppCompatActivity {

    private Button submitbtn;
    private EditText submit;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        submit = (EditText) findViewById(R.id.feedback);
        submitbtn = (Button) findViewById(R.id.submit_feedback);
        loadingBar = new ProgressDialog(this);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String feedback =submit.getText().toString();
                if(TextUtils.isEmpty(feedback))
                {
                    Toast.makeText(ContactActivity.this, "response cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Submitting response");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    feedbacksubmit(feedback);
                }
            }

            private void feedbacksubmit(final String feedback)
            {
                final DatabaseReference RootRef;
                RootRef= FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, Object> userdataMap =new HashMap<>();
                        userdataMap.put("Feedback: ",feedback);


                        RootRef.child("Feedback").child(Prevelent.currentonlineUser.getPhone()).updateChildren(userdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                           loadingBar.dismiss();
                                            Toast.makeText(ContactActivity.this, "your Response sent successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            loadingBar.dismiss();
                                            Toast.makeText(ContactActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(ContactActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
