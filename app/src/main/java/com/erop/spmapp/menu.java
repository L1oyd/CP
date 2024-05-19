package com.erop.spmapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class menu extends Fragment {
    private static final String PREFS_NAME = "SwitchPrefs";
    private static final String SWITCH1_KEY = "switch1";
    private static final String SWITCH2_KEY = "switch2";
    private static final String SWITCH3_KEY = "switch3";

    private BottomNavigationView bottomNavigationView;
    private Switch switchItem1, switchItem2, switchItem3;

    public static menu newInstance() {
        return new menu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        switchItem1 = view.findViewById(R.id.SPM);
        switchItem2 = view.findViewById(R.id.SP);
        switchItem3 = view.findViewById(R.id.SPB);

        bottomNavigationView = ((MainActivity) getActivity()).getBottomNavigationView();

        loadSwitchStates();

        switchItem1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bottomNavigationView.getMenu().findItem(R.id.navigation_cards).setVisible(isChecked);
            saveSwitchState(SWITCH1_KEY, isChecked);
        });

        switchItem2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bottomNavigationView.getMenu().findItem(R.id.navigation_news).setVisible(isChecked);
            saveSwitchState(SWITCH2_KEY, isChecked);
        });

        switchItem3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bottomNavigationView.getMenu().findItem(R.id.navigation_players).setVisible(isChecked);
            saveSwitchState(SWITCH3_KEY, isChecked);
        });

        return view;
    }

    private void saveSwitchState(String key, boolean state) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, state);
        editor.apply();
    }

    private void loadSwitchStates() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean switch1State = sharedPreferences.getBoolean(SWITCH1_KEY, true);
        boolean switch2State = sharedPreferences.getBoolean(SWITCH2_KEY, true);
        boolean switch3State = sharedPreferences.getBoolean(SWITCH3_KEY, true);

        switchItem1.setChecked(switch1State);
        switchItem2.setChecked(switch2State);
        switchItem3.setChecked(switch3State);

        bottomNavigationView.getMenu().findItem(R.id.navigation_cards).setVisible(switch1State);
        bottomNavigationView.getMenu().findItem(R.id.navigation_news).setVisible(switch2State);
        bottomNavigationView.getMenu().findItem(R.id.navigation_players).setVisible(switch3State);
    }
}
