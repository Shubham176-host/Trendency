package com.example.aptaki.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aptaki.R;

public class PaymentSuccessfullActivity extends AppCompatActivity {

    private TextView TransactionId , OrderID;
    private String tranxid , orderid;
    private Button Continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successfull);

        Continue = (Button) findViewById(R.id.continue_home);
        TransactionId = (TextView) findViewById(R.id.transaction_id);
        OrderID = (TextView) findViewById(R.id.Order_id) ;

        orderid = getIntent().getStringExtra("Order Id");
        OrderID.setText("Order id: " + orderid);

        tranxid = getIntent().getStringExtra("Trnx Id");
        TransactionId.setText("Transaction id: " +tranxid);

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PaymentSuccessfullActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}
