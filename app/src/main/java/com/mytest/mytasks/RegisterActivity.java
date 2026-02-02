package com.mytest.mytasks;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etSifre;
    Button btnKayit;
    TextView tvGirisLink;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etRegEmail);
        etSifre = findViewById(R.id.etRegPassword);
        btnKayit = findViewById(R.id.btnRegister);
        tvGirisLink = findViewById(R.id.tvLoginLink);


        btnKayit.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String sifre = etSifre.getText().toString();

            if(email.equals("") || sifre.equals("")) {
                Toast.makeText(this, "Lütfen boş bırakmayınız", Toast.LENGTH_SHORT).show();
            }
            else {

                boolean basarili = dbHelper.kullaniciEkle(email, sifre);

                if(basarili) {
                    Toast.makeText(this, "Kayıt Başarılı! Giriş yapabilirsiniz.", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(this, "Kayıt Başarısız (Email kullanılıyor olabilir)", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvGirisLink.setOnClickListener(v -> {
            finish();
        });
    }
}