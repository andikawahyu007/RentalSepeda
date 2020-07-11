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
import static com.wahyu.rental.SQLiteHelper.TABLE_SEPEDA;

public class PilihPelanggan extends AppCompatActivity {

    ListView mListView;
    ArrayList<Pelanggan> mList;
    ListPelangganAdapter mAdapter = null;
    byte[] img = {};
    private static final String TAG = "PilihPelanggan";
    public static SQLiteHelper mSQLiteHelper;
    public static String EXTRA_ID_SEPEDA = "extra_id_sepeda";
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
        if (getIntent() != null) {
            id_sepeda = getIntent().getIntExtra(EXTRA_ID_SEPEDA, -1);
        }
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEPEDA + " WHERE id=" + id_sepeda);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            int harga = cursor.getInt(2);
            String keterangan = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            int terpinjam = cursor.getInt(5);
            sepeda = new Sepeda(id, nama, harga, keterangan, gambar, terpinjam);
        }
        //get all data from sqlite
        cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE status = 0");
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
                showDialogDetail(PilihPelanggan.this, arrID.get(i));

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

    private void showDialogDetail(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_detail_pelanggan);

        final TextView edtUpdNama = dialog.findViewById(R.id.dlgNamaPel);
        final TextView edtUpdAlamat = dialog.findViewById(R.id.dlgAlamat);
        final TextView edtUpdKet = dialog.findViewById(R.id.dlgKetPel);
        final Button btnSewa = dialog.findViewById(R.id.btnSimpanSewa);

        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE id=" + position);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            edtUpdNama.setText(nama);
            String alamat = cursor.getString(2);
            edtUpdAlamat.setText(alamat + "");
            String keterangan = cursor.getString(3);
            edtUpdKet.setText(keterangan);
            pelanggan = new Pelanggan(id, nama, alamat, keterangan);
        }

        btnSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Date sekarang = new java.util.Date();
                java.text.SimpleDateFormat kalender = new java.text.SimpleDateFormat("dd-MM-yyyy");

                try {
                    mSQLiteHelper.insertDataSewa(sepeda.getNama(), pelanggan.getNama(), kalender.format(sekarang), sepeda.getGambar());
                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    mSQLiteHelper.updateStatusSepeda(1, sepeda.getId());
                    mSQLiteHelper.updateStatusPelanggan(1, pelanggan.getId());
                    Toast.makeText(view.getContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent Sewa = new Intent(view.getContext(), ListSewaActivity.class);
                    view.getContext().startActivity(Sewa);
                    Intent i = new Intent(PilihPelanggan.this, ListSewaActivity.class);
                    startActivity(i);
                    finish();
                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(view.getContext(), "Data gagal disimpan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);

        dialog.show();
    }
}