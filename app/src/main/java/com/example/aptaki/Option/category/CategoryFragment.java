package com.example.aptaki.Option.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aptaki.CategoryActivity;
import com.example.aptaki.User.HomeActivity;

public class CategoryFragment extends Fragment
{
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        Intent intent= new Intent(getActivity() , CategoryActivity.class);
        startActivity(intent);
        return null;
    }
    public void onBackPressed()
    {
        Intent intent= new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }
}
