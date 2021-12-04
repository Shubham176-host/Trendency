package com.example.aptaki.Option.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aptaki.User.HomeActivity;
import com.example.aptaki.User.CartActivity;

public class CartFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Intent intent= new Intent(getActivity(), CartActivity.class);
        startActivity(intent);

        return null;
    }
    public void onBackPressed(){
        Intent intent= new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }
}