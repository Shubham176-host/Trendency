package com.example.aptaki.User;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.aptaki.Model.*;
import com.example.aptaki.Prevelent.Prevelent;
import com.example.aptaki.R;
import com.example.aptaki.Model.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.paytm.pgsdk.easypay.actions.CustomProgressDialog;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button addTocart, buynow;
    private TextView productName,productDiscription,productPrice;
    private ImageView productImage;
    private ElegantNumberButton numberButton;
    private String productID= "";
    private TextView  freesize;
    private String  pimage= "";int s=0;
    private ProgressDialog Loadingbar;
    private Button S , M , L , XL , a,b,c,d,e,f,g;
    private LinearLayout sizeLayout, numbersizeLayout;
    String price ="";

    private int visivble = 0;
    private String sizze= "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID= getIntent().getStringExtra("pid");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_btn);
        addTocart = (Button) findViewById(R.id.addtocart_btn);
        buynow = (Button) findViewById(R.id.buy_now_direct_btn);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDiscription = (TextView) findViewById(R.id.product_discription_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
        //productId= (TextView) findViewById(R.id.product_id_details);
        numberButton.setNumber("1");
        sizeLayout = (LinearLayout) findViewById(R.id.size_details);
        numbersizeLayout = (LinearLayout) findViewById(R.id.number_size_details);
        Loadingbar= new ProgressDialog(this);
        S = (Button) findViewById(R.id.size_small);
        M = (Button) findViewById(R.id.size_medium);
        L = (Button) findViewById(R.id.size_large);
        XL = (Button) findViewById(R.id.size_xlarge);
        a = (Button) findViewById(R.id.size_6);
        b = (Button) findViewById(R.id.size_7);
        c = (Button) findViewById(R.id.size_8);
        d = (Button) findViewById(R.id.size_9);
        e = (Button) findViewById(R.id.size_10);
        f = (Button) findViewById(R.id.size_11);
        g = (Button) findViewById(R.id.size_12);
        freesize = (TextView) findViewById(R.id.free_size_details);

        getProductDetails(productID);

        S.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizze = "S";
                S.setBackgroundColor(Color.rgb(80,80,80));
                M.setBackgroundColor(Color.rgb(175,175,175));
                L.setBackgroundColor(Color.rgb(175,175,175));
                XL.setBackgroundColor(Color.rgb(175,175,175));
            }
        });
        M.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.setBackgroundColor(Color.rgb(80,80,80));
                S.setBackgroundColor(Color.rgb(175,175,175));
                L.setBackgroundColor(Color.rgb(175,175,175));
                XL.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "M";
            }
        });

        L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.setBackgroundColor(Color.rgb(80,80,80));
                M.setBackgroundColor(Color.rgb(175,175,175));
                S.setBackgroundColor(Color.rgb(175,175,175));
                XL.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "L";
            }
        });
        XL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XL.setBackgroundColor(Color.rgb(80,80,80));
                M.setBackgroundColor(Color.rgb(175,175,175));
                L.setBackgroundColor(Color.rgb(175,175,175));
                S.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "XL";
            }
        });
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setBackgroundColor(Color.rgb(80,80,80));
                b.setBackgroundColor(Color.rgb(175,175,175));
                c.setBackgroundColor(Color.rgb(175,175,175));
                d.setBackgroundColor(Color.rgb(175,175,175));
                e.setBackgroundColor(Color.rgb(175,175,175));
                f.setBackgroundColor(Color.rgb(175,175,175));
                g.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "6 UK";
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setBackgroundColor(Color.rgb(80,80,80));
                a.setBackgroundColor(Color.rgb(175,175,175));
                c.setBackgroundColor(Color.rgb(175,175,175));
                d.setBackgroundColor(Color.rgb(175,175,175));
                e.setBackgroundColor(Color.rgb(175,175,175));
                f.setBackgroundColor(Color.rgb(175,175,175));
                g.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "7 UK";

            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setBackgroundColor(Color.rgb(80,80,80));
                b.setBackgroundColor(Color.rgb(175,175,175));
                a.setBackgroundColor(Color.rgb(175,175,175));
                d.setBackgroundColor(Color.rgb(175,175,175));
                e.setBackgroundColor(Color.rgb(175,175,175));
                f.setBackgroundColor(Color.rgb(175,175,175));
                g.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "8 UK";
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setBackgroundColor(Color.rgb(80,80,80));
                b.setBackgroundColor(Color.rgb(175,175,175));
                c.setBackgroundColor(Color.rgb(175,175,175));
                a.setBackgroundColor(Color.rgb(175,175,175));
                e.setBackgroundColor(Color.rgb(175,175,175));
                f.setBackgroundColor(Color.rgb(175,175,175));
                g.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "9 UK";
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.setBackgroundColor(Color.rgb(80,80,80));
                b.setBackgroundColor(Color.rgb(175,175,175));
                c.setBackgroundColor(Color.rgb(175,175,175));
                d.setBackgroundColor(Color.rgb(175,175,175));
                a.setBackgroundColor(Color.rgb(175,175,175));
                f.setBackgroundColor(Color.rgb(175,175,175));
                g.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "10 UK";
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.setBackgroundColor(Color.rgb(80,80,80));
                b.setBackgroundColor(Color.rgb(175,175,175));
                c.setBackgroundColor(Color.rgb(175,175,175));
                d.setBackgroundColor(Color.rgb(175,175,175));
                e.setBackgroundColor(Color.rgb(175,175,175));
                a.setBackgroundColor(Color.rgb(175,175,175));
                g.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "11 UK";
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.setBackgroundColor(Color.rgb(80,80,80));
                b.setBackgroundColor(Color.rgb(175,175,175));
                c.setBackgroundColor(Color.rgb(175,175,175));
                d.setBackgroundColor(Color.rgb(175,175,175));
                e.setBackgroundColor(Color.rgb(175,175,175));
                f.setBackgroundColor(Color.rgb(175,175,175));
                a.setBackgroundColor(Color.rgb(175,175,175));
                sizze = "12 UK";
            }
        });

        buynow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(numberButton.equals(0))
                {
                    Toast.makeText(ProductDetailsActivity.this, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(visivble == 1){
                        if(sizze.equals("")) {
                            Toast.makeText(ProductDetailsActivity.this, "Select a size", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent = new Intent(ProductDetailsActivity.this, ConfirmFinalOrderActivity.class);

                            intent.putExtra("Total Price", String.valueOf(productPrice.getText().toString()));
                            intent.putExtra("pid", String.valueOf(productID));
                            startActivity(intent);
                        }
                    }
                    else {
                        Intent intent = new Intent(ProductDetailsActivity.this, ConfirmFinalOrderActivity.class);

                        intent.putExtra("Total Price", String.valueOf(productPrice.getText().toString()));
                        intent.putExtra("pid", String.valueOf(productID));
                        startActivity(intent);
                    }
                }
            }
        });
        addTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberButton.getNumber() == "0.0")
                {
                    Toast.makeText(ProductDetailsActivity.this, "Quantity cannot be 0", Toast.LENGTH_SHORT).show();
                }
                else
                if(visivble == 1){
                    if(sizze.equals(""))  {
                        Toast.makeText(ProductDetailsActivity.this, "Select a size", Toast.LENGTH_SHORT).show();
                    }
                    else
                        addingToCartList();
                }
                else
                addingToCartList();
            }
        });

    }

    private void getProductDetails(String productID)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    Products products= dataSnapshot.getValue(Products.class);

                    productName.setText(products.getPname());
                    productPrice.setText("₹" +products.getPrice());
                    productDiscription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);

                    price = products.getPrice();

                    pimage= products.getImage().toString();

                    if(dataSnapshot.child("category").getValue().toString().equals("Hats Caps")){
                        freesize.setVisibility(View.VISIBLE);
                    }
                    if(dataSnapshot.child("category").getValue().toString().equals("Shoes")){
                        numbersizeLayout.setVisibility(View.VISIBLE);
                        visivble =1;

                    }
                    if(dataSnapshot.child("category").getValue().toString().equals("tShirts")){
                        sizeLayout.setVisibility(View.VISIBLE);
                        visivble =1;

                    }
                    if(dataSnapshot.child("category").getValue().toString().equals("Sports tShirts")){
                        sizeLayout.setVisibility(View.VISIBLE);
                        visivble =1;

                    }
                    if(dataSnapshot.child("category").getValue().toString().equals("Female Dresses")){
                        sizeLayout.setVisibility(View.VISIBLE);
                        visivble =1;

                    }
                    if(dataSnapshot.child("category").getValue().toString().equals("Sweathers")){
                        sizeLayout.setVisibility(View.VISIBLE);
                        visivble =1;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addingToCartList()
    {
        Loadingbar.setTitle("Adding item to Cart");
        Loadingbar.setMessage("Please wait...");
        Loadingbar.setCanceledOnTouchOutside(false);
        Loadingbar.show();

        String saveCurrentTime, saveCurrentDate;

        final String productid = productID.toString();

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pimage", pimage);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", price.toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("discount", "");
        cartMap.put("psize", sizze);

        cartListRef.child("User View").child(Prevelent.currentonlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevelent.currentonlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {
                                                Loadingbar.dismiss();
                                                Toast.makeText(ProductDetailsActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                                                Intent intent= new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                intent.putExtra("pid", productID);
                                                startActivity(intent);
                                            }
                                            else
                                            {
                                                Toast.makeText(ProductDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();

                                                Loadingbar.dismiss();
                                            }

                                        }
                                    });
                        }
                    }
                });
    }
}
