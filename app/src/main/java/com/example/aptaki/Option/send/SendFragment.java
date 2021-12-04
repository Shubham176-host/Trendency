package com.example.aptaki.Option.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aptaki.User.HomeActivity;
import com.example.aptaki.User.SearchProductsActivity;

public class SendFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Intent intent= new Intent(getActivity(), SearchProductsActivity.class);
        startActivity(intent);

        return null;
    }
    public void onBackPressed(){
        Intent intent= new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }

}