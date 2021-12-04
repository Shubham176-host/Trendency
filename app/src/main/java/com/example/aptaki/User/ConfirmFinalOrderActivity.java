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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aptaki.Prevelent.Prevelent;
import com.example.aptaki.R;
import com.example.aptaki.Option.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private Button proceedToPay;
    private EditText shippingname, shippingphone, shippingpincode, shippingaddress1, shippingaddress2, shippingcity;
    private TextView Tprice;
    private ProgressDialog loadingBar;
    private RelativeLayout relativeLayout;
    private String totalAmount = "";
    private String Productids= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        Productids= getIntent().getStringExtra("product ids");

        proceedToPay = (Button) findViewById(R.id.proceed_to_pay_aftershipping);
        shippingname = (EditText) findViewById(R.id.shipping_name);
        shippingphone = (EditText) findViewById(R.id.shipping_contact_number);
        shippingpincode = (EditText) findViewById(R.id.shipping_address_pincode);
        shippingaddress1 = (EditText) findViewById(R.id.shipping_address_line1);
        shippingaddress2 = (EditText) findViewById(R.id.shipping_address_line2);
        shippingcity = (EditText) findViewById(R.id.shipping_address_city);
        relativeLayout= (RelativeLayout) findViewById(R.id.relative_layout);
        loadingBar = new ProgressDialog(this);

        totalAmount = getIntent().getStringExtra("Total Price");

        Tprice = (TextView) findViewById(R.id.Total_Amount);
        Tprice.setText("Total Amount = ₹" + totalAmount);

        shippingAddressAvailable(shippingname, shippingphone, shippingcity, shippingpincode, shippingaddress1, shippingaddress2, shippingaddress2);

        //getShippingDetails();

        proceedToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                confirmFinalOrder();
            }

            private void confirmFinalOrder() {
                String sname = shippingname.getText().toString();
                String sphone = shippingphone.getText().toString();
                String saddressline1 = shippingaddress1.getText().toString();
                String saddressline2 = shippingaddress2.getText().toString();
                String saddresspincode = shippingpincode.getText().toString();
                String scity = shippingcity.getText().toString();

                if (TextUtils.isEmpty(sname)) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please enter shipping name....", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sphone)) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please enter phone number....", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(saddressline1)) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please enter your address line 1....", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(saddressline2)) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please enter your address line 2....", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(scity)) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please enter your city....", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(saddresspincode)) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Please enter your pincode....", Toast.LENGTH_SHORT).show();
                } else if (!(TextUtils.isDigitsOnly(saddresspincode))) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Invalid Pincode", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.getTrimmedLength(sphone)<9) {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Saving Data");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    SaveingShippingAddress(sname, sphone, saddressline1, saddressline2, scity, saddresspincode);
                }
            }

            private void SaveingShippingAddress(final String sname, final String sphone, final String saddress1, final String saddress2, final String scity, final String spincode) {
                final String saveCurrentDate, saveCurrentTime;

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference();

                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        final HashMap<String, Object> ordersdataMap = new HashMap<>();
                        ordersdataMap.put("sname",sname);
                        ordersdataMap.put("sphone",sphone);
                        ordersdataMap.put("saddress1",saddress1);
                        ordersdataMap.put("saddress2",saddress2);
                        ordersdataMap.put("scity",scity);
                        ordersdataMap.put("spincode",spincode);

                        RootRef.child("Users").child(Prevelent.currentonlineUser.getPhone()).child("Shipping Address").updateChildren(ordersdataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Shipping Adress saved successfully", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();

                                            ConfirmOrder();
                                        }
                                        else
                                        {
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                    }
                });
            }
        });
    }

    private void shippingAddressAvailable(final EditText editText, final EditText shippingphone, final EditText shippingcity, final EditText shippingpincode, final EditText shippingaddress1,final EditText shippingaddress2, final EditText shippingname)
    {

        DatabaseReference UsershipsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.currentonlineUser.getPhone()).child("Shipping Address");


        UsershipsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String name = dataSnapshot.child("sname").getValue().toString();
                    String phone = dataSnapshot.child("sphone").getValue().toString();
                    String address1 = dataSnapshot.child("saddress1").getValue().toString();
                    String address2 = dataSnapshot.child("saddress2").getValue().toString();
                    String city = dataSnapshot.child("scity").getValue().toString();
                    String pincode = dataSnapshot.child("spincode").getValue().toString();

                    shippingname.setText(name);
                    shippingcity.setText(city);
                    shippingaddress1.setText(address1);
                    shippingaddress2.setText(address2);
                    shippingphone.setText(phone);
                    shippingpincode.setText(pincode);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ConfirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final String OrderId = saveCurrentDate + saveCurrentTime;

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(OrderId);

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("productid", OrderId);
        ordersMap.put("name", shippingname.getText().toString());
        ordersMap.put("city", shippingcity.getText().toString());
        ordersMap.put("phone", shippingphone.getText().toString());
        ordersMap.put("address", shippingaddress1.getText().toString()+ shippingaddress2.getText().toString());
        ordersMap.put("pincode", shippingcity.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "not shipped");
        ordersMap.put("payment", "not paid");
        ordersMap.put("totalAmount", totalAmount);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference()
                            .child("Cart List")
                            .child("User View")
                            .child(Prevelent.currentonlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("Cart List")
                                                .child("Admin View")
                                                .child(Prevelent.currentonlineUser.getPhone())
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    final DatabaseReference userorder= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.currentonlineUser.getPhone()).child("Order").child(OrderId);

                                                    HashMap<String, Object> userOrdermaps = new HashMap<>();
                                                    userOrdermaps.put("productid", OrderId);
                                                    userOrdermaps.put("name", shippingname.getText().toString());
                                                    userOrdermaps.put("city", shippingcity.getText().toString());
                                                    userOrdermaps.put("phone", shippingphone.getText().toString());
                                                    userOrdermaps.put("address", shippingaddress1.getText().toString()+ shippingaddress2.getText().toString());
                                                    userOrdermaps.put("pincode", shippingcity.getText().toString());
                                                    userOrdermaps.put("date", saveCurrentDate);
                                                    userOrdermaps.put("time", saveCurrentTime);
                                                    userOrdermaps.put("state", "not shipped");
                                                    userOrdermaps.put("payment", "not paid");
                                                    userOrdermaps.put("totalAmount", totalAmount);

                                                    userorder.updateChildren(userOrdermaps).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(ConfirmFinalOrderActivity.this, PaymentGatewayActivity.class);
                                                            intent.putExtra("Total Price", String.valueOf(totalAmount));
                                                            intent.putExtra("Order id", OrderId);
                                                            startActivity(intent);

                                                            Toast.makeText(ConfirmFinalOrderActivity.this, "Proceed with the payment", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        new Intent(ConfirmFinalOrderActivity.this, HomeFragment.class);
        finish();
    }

}