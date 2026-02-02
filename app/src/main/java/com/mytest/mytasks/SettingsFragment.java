package com.mytest.mytasks;

import android.content.res.Configuration;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    Button btnCikis;
    SwitchMaterial swGeceModu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnCikis = view.findViewById(R.id.btnLogout);
        swGeceModu = view.findViewById(R.id.swDarkMode);

        SharedPreferences sp = getActivity().getSharedPreferences("Ayarlar", Context.MODE_PRIVATE);

        // --- DÜZELTME ---
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            swGeceModu.setChecked(true);
        } else {
            swGeceModu.setChecked(false);
        }

        // --- ANAHTARA BASINCA ---
        swGeceModu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sp.edit();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("GECE_MODU", true);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("GECE_MODU", false);
            }
            editor.apply();
        });

        // --- ÇIKIŞ BUTONU ---
        btnCikis.setOnClickListener(v -> {
            SharedPreferences loginSP = getActivity().getSharedPreferences("KullaniciBilgi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = loginSP.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}