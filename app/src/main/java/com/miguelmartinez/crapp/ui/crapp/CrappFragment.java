package com.miguelmartinez.crapp.ui.crapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.miguelmartinez.crapp.R;

public class CrappFragment extends Fragment {

    private CrappViewModel crappViewModel;
    Button Btn_Donar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        crappViewModel =
                ViewModelProviders.of(this).get(CrappViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crapp, container, false);
        Btn_Donar = (Button) root.findViewById(R.id.Btn_Donar);


        Btn_Donar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.paypal.me/miguelfmartinez");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        return root;
    }
}