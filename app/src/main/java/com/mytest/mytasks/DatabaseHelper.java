package com.mytest.mytasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UniTaskPro.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "kullanicilar";
    public static final String TABLE_TASKS = "gorevler";

    public static final String COL_USER_ID = "id";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASS = "sifre";

    public static final String COL_TASK_ID = "id";
    public static final String COL_TASK_USER_ID = "kullanici_id";
    public static final String COL_TASK_TITLE = "baslik";
    public static final String COL_TASK_DATE = "tarih";
    public static final String COL_TASK_IMPORTANCE = "onem_derecesi"; // 1: Yeşil, 2: Sarı, 3: Kırmızı
    public static final String COL_TASK_STATUS = "durum"; // 0: Yapılacak, 1: Tamamlandı

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_USER_PASS + " TEXT)";
        db.execSQL(createUsersTable);

        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TASK_USER_ID + " INTEGER, " +
                COL_TASK_TITLE + " TEXT, " +
                COL_TASK_DATE + " TEXT, " +
                COL_TASK_IMPORTANCE + " INTEGER, " +
                COL_TASK_STATUS + " INTEGER)";
        db.execSQL(createTasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }



    public boolean kullaniciEkle(String email, String sifre) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PASS, sifre);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean kullaniciKontrol(String email, String sifre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                COL_USER_EMAIL + "=? AND " + COL_USER_PASS + "=?", new String[]{email, sifre});

        boolean mevcut = cursor.getCount() > 0;
        cursor.close();
        return mevcut;
    }

    public int kullaniciIdGetir(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_USER_ID + " FROM " + TABLE_USERS +
                " WHERE " + COL_USER_EMAIL + "=?", new String[]{email});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1;
    }


    public boolean gorevEkle(int userId, String baslik, String tarih, int onemDerecesi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK_USER_ID, userId);
        values.put(COL_TASK_TITLE, baslik);
        values.put(COL_TASK_DATE, tarih); //
        values.put(COL_TASK_IMPORTANCE, onemDerecesi); // 3:Çok, 2:Önemli, 1:Önemsiz
        values.put(COL_TASK_STATUS, 0);

        long result = db.insert(TABLE_TASKS, null, values);
        return result != -1;
    }


    public java.util.ArrayList<Task> gorevleriGetirSirali(int userId, String siralamaTuru) {
        java.util.ArrayList<Task> gorevListesi = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String sorgu = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COL_TASK_USER_ID + "=" + userId;

        if (siralamaTuru.equals("tarih")) {
            // Tarihe göre artan (En yakın tarih en üstte)
            sorgu += " ORDER BY " + COL_TASK_DATE + " ASC";
        } else if (siralamaTuru.equals("onem")) {
            // Öneme göre azalan (3 en üstte, sonra 2, sonra 1)
            sorgu += " ORDER BY " + COL_TASK_IMPORTANCE + " DESC";
        }

        Cursor cursor = db.rawQuery(sorgu, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int uId = cursor.getInt(1);
                String baslik = cursor.getString(2);
                String tarih = cursor.getString(3);
                int onem = cursor.getInt(4);
                int durum = cursor.getInt(5);
                gorevListesi.add(new Task(id, uId, baslik, tarih, onem, durum));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return gorevListesi;
    }
    public java.util.ArrayList<Task> gorevleriGetir(int userId) {
        java.util.ArrayList<Task> gorevListesi = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Sadece giriş yapanin görevlerini çek
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COL_TASK_USER_ID + "=?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(0);
                int uId = cursor.getInt(1);
                String baslik = cursor.getString(2);
                String tarih = cursor.getString(3);
                int onem = cursor.getInt(4);
                int durum = cursor.getInt(5);


                gorevListesi.add(new Task(id, uId, baslik, tarih, onem, durum));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return gorevListesi;
    }


    public void gorevSil(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COL_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
    }
}