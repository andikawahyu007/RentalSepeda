package com.wahyu.rental;

public class Pelanggan {
    private int id;
    private String nama, keterangan, alamat;

    public Pelanggan(int id, String nama, String alamat, String keterangan) {
        this.id = id;
        this.nama = nama;
        this.keterangan = keterangan;
        this.alamat = alamat;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}