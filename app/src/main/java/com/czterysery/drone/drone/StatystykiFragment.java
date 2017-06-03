package com.czterysery.drone.drone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class StatystykiFragment extends Fragment {
    String a;


    public static StatystykiFragment newInstance(String param1) {
        StatystykiFragment fragment = new StatystykiFragment();
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
        View layout = inflater.inflate(R.layout.card_mydrone, container, false);
        Toast.makeText(getActivity(), a, Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return layout;
    }
}
