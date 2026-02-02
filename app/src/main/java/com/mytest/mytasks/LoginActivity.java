package com.mytest.mytasks; // Kendi paket isminle aynı olduğundan emin ol

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etSifre;
    Button btnGiris;
    TextView tvKayitOl;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences kontrolSP = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE);
        int kayitliId = kontrolSP.getInt("USER_ID", -1);

        if (kayitliId != -1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etLoginEmail);
        etSifre = findViewById(R.id.etLoginPassword);
        btnGiris = findViewById(R.id.btnLogin);
        tvKayitOl = findViewById(R.id.tvRegisterLink);


        btnGiris.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String sifre = etSifre.getText().toString();

            if(email.equals("") || sifre.equals("")) {
                Toast.makeText(LoginActivity.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            }
            else {
                // Veritabanından kontrol et
                boolean kontrol = dbHelper.kullaniciKontrol(email, sifre);

                if(kontrol) {
                    Toast.makeText(LoginActivity.this, "Giriş Başarılı!", Toast.LENGTH_SHORT).show();


                    int userId = dbHelper.kullaniciIdGetir(email);
                    SharedPreferences sp = getSharedPreferences("KullaniciBilgi", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("USER_ID", userId);
                    editor.apply();


                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Hatalı Email veya Şifre", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvKayitOl.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}