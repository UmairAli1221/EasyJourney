package com.example.umairali.easyjourney.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.umairali.easyjourney.MapsActivity;
import com.example.umairali.easyjourney.R;

/**
 * Created by umair.ali on 11/2/2016.
 */

public class newtravelFragment extends Fragment {
    EditText time,date,Startpoint,EndPoint;
    Context context;
    Button btn;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.newtravell, container, false);
        context = myView.getContext();
        time=(EditText)myView.findViewById(R.id.time);
        date=(EditText)myView.findViewById(R.id.date);
        Startpoint=(EditText)myView.findViewById(R.id.spoint);
        EndPoint=(EditText)myView.findViewById(R.id.epoint);
        btn = (Button) myView.findViewById(R.id.searchbtn);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapsActivity.class);
                startActivity(i);
            }


        });
        return myView;
    }
}
