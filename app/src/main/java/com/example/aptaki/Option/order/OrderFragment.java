package com.example.aptaki.Option.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aptaki.User.HomeActivity;
import com.example.aptaki.User.OrdersActivity;

public class OrderFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Intent intent= new Intent(getActivity(), OrdersActivity.class);
        startActivity(intent);

        return null;
    }
    public void onBackPressed(){
        Intent intent= new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }
}