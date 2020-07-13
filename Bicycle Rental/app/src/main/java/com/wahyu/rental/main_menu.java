package com.wahyu.rental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import static com.wahyu.rental.SQLiteHelper.TABLE_SEPEDA;
import static com.wahyu.rental.SQLiteHelper.TABLE_PELANGGAN;
import static com.wahyu.rental.SQLiteHelper.TABLE_SEWA;

public class main_menu extends AppCompatActivity {
    ImageButton mbtnAbout, mbtnDataSepeda, mbtnDataSewa, mbtnDataPelanggan, mbtnKeluar;
    final Context mContext = this;
    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mbtnAbout = findViewById(R.id.btnAbout);
        mbtnDataSepeda = findViewById(R.id.btnDataSepeda);
        mbtnDataSewa = findViewById(R.id.btnDataSewa);
        mbtnDataPelanggan = findViewById(R.id.btnDataPelanggan);
        mbtnKeluar = findViewById(R.id.btnKeluar);

        mbtnAbout.setOnClickListener(new MenuAbout());
        mbtnDataSepeda.setOnClickListener(new MenuDataSepeda());
        mbtnKeluar.setOnClickListener(new MenuKeluar());
        mbtnDataSewa.setOnClickListener(new MenuDataSewa());
        mbtnDataPelanggan.setOnClickListener(new MenuDataPelanggan());
    }

    private class MenuAbout implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(main_menu.this, about_us.class);
            startActivity(i);
        }
    }

    private class MenuDataSepeda implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(main_menu.this, ListSepedaActivity.class);
            startActivity(i);
        }
    }

    private class MenuDataSewa implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(main_menu.this, ListSewaActivity.class);
            startActivity(i);
        }
    }

    private class MenuDataPelanggan implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(main_menu.this, ListPelangganActivity.class);
            startActivity(i);
        }
    }

    private class MenuKeluar implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}