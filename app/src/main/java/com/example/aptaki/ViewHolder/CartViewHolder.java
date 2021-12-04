package com.example.aptaki.ViewHolder;

import android.app.AppComponentFactory;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import  com.example.aptaki.Interface.ItemClickListener;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aptaki.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductPrice, txtProductQuantity, txtProductsize;
    public ImageView txtproductImage;

    public ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.product_name_details);
        txtProductPrice = itemView.findViewById(R.id.product_price_details);
        txtProductQuantity = itemView.findViewById(R.id.product_quantity);
        txtproductImage = itemView.findViewById(R.id.product_image_details);
        txtProductsize= itemView.findViewById(R.id.product_size_details);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.OnClick(v, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListener itemClickListner)
    {
       this.itemClickListener = itemClickListner;
    }
}