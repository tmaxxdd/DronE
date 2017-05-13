package com.czterysery.hop.drone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MapaFragment extends Fragment {
    String a;

    public static MapaFragment newInstance(String param1) {
        MapaFragment fragment = new MapaFragment();
        Bundle args = new Bundle();
        args.putString("1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            a = getArguments().getString("1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout  = inflater.inflate(R.layout.fragment_mapa, container, false);
        return layout;
    }
}

