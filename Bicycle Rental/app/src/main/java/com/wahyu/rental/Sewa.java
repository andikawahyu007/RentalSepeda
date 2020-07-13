package com.wahyu.rental;

public class Sewa {
    private int id, harga;
    private String nama_sepeda, nama_penyewa, tanggal;
    byte[] gambar;

    public Sewa(int id, String nama_sepeda, String nama_penyewa, String tanggal, int harga, byte[] gambar) {
        this.id = id;
        this.nama_sepeda = nama_sepeda;
        this.nama_penyewa = nama_penyewa;
        this.tanggal = tanggal;
        this.harga = harga;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_sepeda() {
        return nama_sepeda;
    }

    public void setNama_sepeda(String nama_sepeda) {
        this.nama_sepeda = nama_sepeda;
    }

    public String getNama_penyewa() {
        return nama_penyewa;
    }

    public void setNama_penyewa(String nama_penyewa) {
        this.nama_penyewa = nama_penyewa;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public byte[] getGambar() {
        return gambar;
    }

    public void setGambar(byte[] gambar) {
        this.gambar = gambar;
    }
}