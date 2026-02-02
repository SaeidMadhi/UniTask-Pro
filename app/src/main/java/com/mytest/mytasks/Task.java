package com.mytest.mytasks;

public class Task {
    int id;
    int userId;
    String baslik;
    String tarih;
    int onem;
    int durum;


    public Task(int id, int userId, String baslik, String tarih, int onem, int durum) {
        this.id = id;
        this.userId = userId;
        this.baslik = baslik;
        this.tarih = tarih;
        this.onem = onem;
        this.durum = durum;
    }


    public String getBaslik() { return baslik; }
    public String getTarih() { return tarih; }
    public int getDurum() { return durum; }
    public int getId() { return id; }
    public void setDurum(int durum) { this.durum = durum; }
    public int getOnem() { return onem; }
}