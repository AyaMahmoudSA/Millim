package com.av.millim.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.av.millim.R;

/**
 * Created by Maiada on 12/10/2017.
 */

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rooView;
    }
}
