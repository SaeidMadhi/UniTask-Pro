package com.mytest.mytasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton fabEkle;
    DatabaseHelper dbHelper;
    ArrayList<Task> gorevListesi;
    TaskAdapter adapter;
    int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTasks);
        fabEkle = view.findViewById(R.id.fabAddTask);
        dbHelper = new DatabaseHelper(getContext());

        SharedPreferences sp = getActivity().getSharedPreferences("KullaniciBilgi", Context.MODE_PRIVATE);
        userId = sp.getInt("USER_ID", -1);

        listeyiGuncelle();

        fabEkle.setOnClickListener(v -> {
            pencereAcVeEkle();
        });

        return view;
    }

    private void listeyiGuncelle() {

        if(userId == -1) return;

        gorevListesi = dbHelper.gorevleriGetir(userId);
        adapter = new TaskAdapter(getContext(), gorevListesi, dbHelper);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void pencereAcVeEkle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Yeni Görev Oluştur");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);


        final EditText etBaslik = new EditText(getContext());
        etBaslik.setHint("Görev nedir?");
        layout.addView(etBaslik);

        // 2. TARİH SEÇİCİ BUTON
        final Button btnTarih = new Button(getContext());
        btnTarih.setText("Tarih Seç");
        final String[] secilenTarihFormatli = {""};

        btnTarih.setOnClickListener(v -> {
            Calendar takvim = Calendar.getInstance();
            int yil = takvim.get(Calendar.YEAR);
            int ay = takvim.get(Calendar.MONTH);
            int gun = takvim.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                    (view, year, month, dayOfMonth) -> {
                        String duzgunAy = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                        String duzgunGun = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        secilenTarihFormatli[0] = year + "-" + duzgunAy + "-" + duzgunGun;
                        btnTarih.setText(dayOfMonth + "." + (month + 1) + "." + year);
                    }, yil, ay, gun);
            datePicker.show();
        });
        layout.addView(btnTarih);


        TextView tvOnem = new TextView(getContext());
        tvOnem.setText("Önem Derecesi:");
        tvOnem.setPadding(0, 30, 0, 10);
        layout.addView(tvOnem);

        RadioGroup radioGroup = new RadioGroup(getContext());
        RadioButton rbCok = new RadioButton(getContext()); rbCok.setText("Çok Önemli");
        RadioButton rbOrta = new RadioButton(getContext()); rbOrta.setText("Önemli");
        RadioButton rbAz = new RadioButton(getContext()); rbAz.setText("Önemsiz");

        radioGroup.addView(rbCok);
        radioGroup.addView(rbOrta);
        radioGroup.addView(rbAz);
        rbOrta.setChecked(true);
        layout.addView(radioGroup);

        builder.setView(layout);

        builder.setPositiveButton("Kaydet", (dialog, which) -> {
            String baslik = etBaslik.getText().toString();

            // Seçimi al
            int secilenOnem = 1;
            if (rbCok.isChecked()) secilenOnem = 3;
            else if (rbOrta.isChecked()) secilenOnem = 2;

            if (!baslik.isEmpty() && !secilenTarihFormatli[0].isEmpty()) {
                // İŞTE BURASI DÜZELDİ: Artık 4 parametre gönderiyoruz
                dbHelper.gorevEkle(userId, baslik, secilenTarihFormatli[0], secilenOnem);
                listeyiGuncelle();
                Toast.makeText(getContext(), "Görev Eklendi!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lütfen başlık ve tarih seçin", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("İptal", null);
        builder.show();
    }
}