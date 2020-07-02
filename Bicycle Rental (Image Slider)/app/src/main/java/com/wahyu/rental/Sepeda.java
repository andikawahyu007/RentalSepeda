package com.wahyu.rental;

public class Sepeda {
    private int id, harga;
    private String nama, keterangan;
    private byte[] gambar;

    public Sepeda(int id, String nama, int harga, String keterangan, byte[] gambar) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.keterangan = keterangan;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }


    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public byte[] getGambar() {
        return gambar;
    }

    public void setGambar(byte[] gambar) {
        this.gambar = gambar;
    }
}