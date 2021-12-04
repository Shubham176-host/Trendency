package com.example.aptaki.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aptaki.Model.Cart;
import com.example.aptaki.Model.Products;
import com.example.aptaki.R;
import com.example.aptaki.Option.home.HomeFragment;
import com.example.aptaki.Prevelent.Prevelent;
import com.example.aptaki.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button continueshopping, proceedtopay, updatePrice;
    private TextView textTotalAmt;
    private String productid= "", productid2= "";
    private ImageView productimage;

    private float TotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        productid= getIntent().getStringExtra("pid");

        recyclerView = findViewById(R.id.cart_list);
        productimage = findViewById(R.id.product_image_details);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        continueshopping = findViewById(R.id.continue_shopping);
        updatePrice = findViewById(R.id.update_price_btn);
        proceedtopay = findViewById(R.id.proceed_to_pay);
        textTotalAmt = findViewById(R.id.total_price);

       textTotalAmt.setText("Total Price = ₹" + String.valueOf(TotalPrice));

        updatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textTotalAmt.setText("Total Price = ₹" + String.valueOf(TotalPrice));
            }
        });

        proceedtopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TotalPrice == 0)
                {
                    Toast.makeText(CartActivity.this, "Add items to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                    intent.putExtra("Total Price", String.valueOf(TotalPrice));
                    intent.putExtra("product ids", String.valueOf(productid2));
                    startActivity(intent);
                    finish();
                }
            }
        });

        continueshopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        textTotalAmt.setText("Total Price = ₹" + TotalPrice);

        final DatabaseReference cartLisRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartLisRef.child("User View")
                                .child(Prevelent.currentonlineUser.getPhone())
                                .child("Products"), Cart.class)
                        .build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Q: " + model.getQuantity());
                holder.txtProductPrice.setText("₹" +model.getPrice());
                holder.txtProductName.setText(model.getPname());
                holder.txtProductsize.setText(model.getPsize());

                String productid1 = "";
                productid1= model.getPname();
                productid2 = (productid2+" | " +productid1);

                //getProductDetails(model.getPid());

               // Picasso.get().load().into(holder.txtproductImage);
                Picasso.get().load(model.getPimage()).resize(200, 200).into(holder.txtproductImage);

                float oneTypeProductTPrice = ((Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity()));
                TotalPrice = TotalPrice + oneTypeProductTPrice;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Option");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                } else if (i == 1) {
                                    cartLisRef.child("User View")
                                            .child(Prevelent.currentonlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .getRef()
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        float oneTypeProductTPrice = ((Integer.valueOf(model.getPrice())) * Integer.valueOf(model.getQuantity()));
                                                        TotalPrice = TotalPrice - oneTypeProductTPrice;
                                                        Toast.makeText(CartActivity.this, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onBackPressed() {
        new Intent(CartActivity.this, HomeFragment.class);
        finish();
    }
    private void getProductDetails(final String productid)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(Prevelent.currentonlineUser.getPhone()).child("Products");

        productsRef.child(productid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                    Products products= dataSnapshot.getValue(Products.class);

                    Picasso.get().load(products.getImage()).into(productimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
