package com.example.umairali.easyjourney.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.umairali.easyjourney.R;

/**
 * Created by umair.ali on 11/2/2016.
 */

public class historyFragment extends Fragment {
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView=inflater.inflate(R.layout.history,container,false);
        return myView;
    }
}
