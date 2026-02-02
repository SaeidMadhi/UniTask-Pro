package com.mytest.mytasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FocusFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    TaskAdapter adapter;
    ImageView btnSort;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_focus, container, false);

        recyclerView = view.findViewById(R.id.recyclerFocus);
        btnSort = view.findViewById(R.id.btnSort);
        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences sp = getActivity().getSharedPreferences("KullaniciBilgi", Context.MODE_PRIVATE);
        userId = sp.getInt("USER_ID", -1);


        listeyiGetir("tarih");


        btnSort.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(getContext(), btnSort);

            popup.getMenu().add(0, 1, 0, "Tarihe Göre (Yaklaşan)");
            popup.getMenu().add(0, 2, 0, "Öneme Göre (Acil)");


            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 1) {
                    listeyiGetir("tarih");
                } else if (item.getItemId() == 2) {
                    listeyiGetir("onem");
                }
                return true;
            });
            popup.show();
        });

        return view;
    }

    private void listeyiGetir(String siralamaTuru) {
        if(userId == -1) return;
        ArrayList<Task> siraliListe = dbHelper.gorevleriGetirSirali(userId, siralamaTuru);
        adapter = new TaskAdapter(getContext(), siraliListe, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}