package com.example.aptaki.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aptaki.Prevelent.Prevelent;
import com.example.aptaki.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentGatewayActivity extends AppCompatActivity {

    final int UPI_PAYMENT = 0;
    private String totalamount = "";
    String Productid, orderid;

    private Button payButton;
    private TextView totalAmount;
    private CheckBox payonline , payoncod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        payonline = (CheckBox) findViewById(R.id.payonlinecheck);
        payoncod= (CheckBox) findViewById(R.id.payoncod);

        Productid = getIntent().getStringExtra("pid");

        orderid = getIntent().getStringExtra("Order id");

        payButton = (Button) findViewById(R.id.final_pay);
        totalAmount = (TextView) findViewById(R.id.Total_amaount);
        totalamount = getIntent().getStringExtra("Total Price");

        totalAmount.setText("Total Amount = ₹" + totalamount);

        payonline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                payoncod.setChecked(false);
            }
        });

        payoncod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                payonline.setChecked(false);
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (payoncod.isChecked()) {


                    Toast.makeText(PaymentGatewayActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaymentGatewayActivity.this , HomeActivity.class);
                    startActivity(intent);
                } else

                    if(payonline.isChecked())
                    {
                    Uri uri =
                            new Uri.Builder()
                                    .scheme("upi")
                                    .authority("pay")
                                    .appendQueryParameter("pa", "8050685189-1@okbizaxis")
                                    .appendQueryParameter("pn", "Trendency")
                                    //.appendQueryParameter("mc", "1234")
                                    //.appendQueryParameter("tr", "123456789")
                                    //.appendQueryParameter("tn", "test transaction note")
                                    .appendQueryParameter("am", totalamount)
                                    .appendQueryParameter("cu", "INR")
                                    //.appendQueryParameter("url", "https://test.merchant.website")
                                    .build();
                    Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
                    upiPayIntent.setData(uri);
                    Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

                    if (null != chooser.resolveActivity(getPackageManager())) {
                        startActivityForResult(chooser, UPI_PAYMENT);
                    } else {
                        Toast.makeText(PaymentGatewayActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();

                    }
                }
                    else
                    {
                        Toast.makeText(PaymentGatewayActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("main ", "response " + resultCode);
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(PaymentGatewayActivity.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentGatewayActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: " + approvalRefNo);
                Intent intent = new Intent(PaymentGatewayActivity.this, PaymentSuccessfullActivity.class);
                intent.putExtra("Trnx Id", approvalRefNo);
                intent.putExtra("Order Id", orderid);
                ConfirmOrder(orderid);
                startActivity(intent);

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentGatewayActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
            } else {
                Toast.makeText(PaymentGatewayActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(PaymentGatewayActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    private void ConfirmOrder(final String orderid) {


        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(orderid);

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("payment", "paid");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    final  DatabaseReference UserorderRef = FirebaseDatabase.getInstance().getReference()
                            .child("Users")
                            .child(Prevelent.currentonlineUser.getPhone())
                            .child("Order")
                            .child(orderid);

                    HashMap<String, Object> UserordersMap = new HashMap<>();
                    UserordersMap.put("payment", "paid");

                    UserorderRef.updateChildren(UserordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(PaymentGatewayActivity.this, "Payment Successfull", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Do you want to cancel the order?")
                .setPositiveButton("YES", null)
                .setNegativeButton("NO", null)
                .show();
        Button Possitivebtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Possitivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                        .child("Orders")
                        .child(orderid);


                ordersRef.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            final  DatabaseReference UserorderRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(Prevelent.currentonlineUser.getPhone())
                                    .child("Order")
                                    .child(orderid);


                            UserorderRef.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PaymentGatewayActivity.this, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PaymentGatewayActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
            }
        });

    }
}


