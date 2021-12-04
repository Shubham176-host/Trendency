package com.example.aptaki.Option.tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aptaki.LoginActivity;

import io.paperdb.Paper;

public class ToolsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Paper.init(getActivity());
        Intent intent= new Intent(getActivity(), LoginActivity.class);
        Paper.book().destroy();
        startActivity(intent);

        return null;
    }
}