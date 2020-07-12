package com.wahyu.rental;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.wahyu.rental.SQLiteHelper.TABLE_PELANGGAN;

public class PilihPelanggan extends AppCompatActivity {

    ListView mListView;
    ArrayList<Pelanggan> mList;
    ListPelangganAdapter mAdapter = null;
    byte[] img = {};
    private static final String TAG = "PilihPelanggan";
    public static SQLiteHelper mSQLiteHelper;
    private int id_sepeda;
    private Sepeda sepeda;
    private Pelanggan pelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_pelanggan);

        mListView = findViewById(R.id.listPelanggan);
        mList = new ArrayList<>();
        mAdapter = new ListPelangganAdapter(this, R.layout.baris_item_pelanggan, mList);
        mListView.setAdapter(mAdapter);
        mSQLiteHelper = SQLiteHelper.getInstance(this);

        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            String alamat = cursor.getString(2);
            String keterangan = cursor.getString(3);

            //add to list
            Pelanggan newPelanggan = new Pelanggan(id, nama, alamat, keterangan);
            Gson gson = new Gson();
//            newPelanggan.setGambar(img);
//            String Pelanggan = gson.toJson(newPelanggan).toString();
//            Log.d(TAG, "onCreate: "+Pelanggan);
            mList.add(newPelanggan);
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size() == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Maaf, Tidak Ada Data Pelanggan Yang Ditemukan", Toast.LENGTH_SHORT).show();
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //read
                Cursor c = mSQLiteHelper.getData("SELECT id FROM " + TABLE_PELANGGAN + " WHERE status = 0");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                //show detail dialog
                Intent pilihSepeda = new Intent(view.getContext(), PilihSepeda.class);
                pilihSepeda.putExtra(PilihSepeda.EXTRA_ID_PELANGGAN, arrID.get(i));
                view.getContext().startActivity(pilihSepeda);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            String alamat = cursor.getString(2);
            String keterangan = cursor.getString(3);

            //add to list
            Pelanggan newPelanggan = new Pelanggan(id, nama, alamat, keterangan);
//            Gson gson = new Gson();
//            newPelanggan.setGambar(img);
//            String Pelanggan = gson.toJson(newPelanggan).toString();
//            Log.d(TAG, "onCreate: "+Pelanggan);
            mList.add(newPelanggan);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateRecordList() {
        //get all data from sqlite
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            String alamat = cursor.getString(2);
            String keterangan = cursor.getString(3);

            //add to list
            Pelanggan newPelanggan = new Pelanggan(id, nama, alamat, keterangan);
        }
        mAdapter.notifyDataSetChanged();
    }
}