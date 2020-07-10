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

public class ListPelangganActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Pelanggan> mList;
    ListPelangganAdapter mAdapter = null;
    byte[] img = {};
    private static final String TAG = "ListPelangganActivity";
    public static SQLiteHelper mSQLiteHelper;
    ImageButton mLihatPelanggan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pelanggan);

        mListView = findViewById(R.id.listPelanggan);
        mLihatPelanggan = findViewById(R.id.btnTmbPelanggan);
        mList = new ArrayList<>();
        mAdapter = new ListPelangganAdapter(this, R.layout.baris_item_pelanggan, mList);
        mListView.setAdapter(mAdapter);
        mSQLiteHelper = SQLiteHelper.getInstance(this);
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

        mLihatPelanggan.setOnClickListener(new TambahPelanggan());

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Hapus"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(ListPelangganActivity.this);

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //update
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM " + TABLE_PELANGGAN + "");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //read
                Cursor c = mSQLiteHelper.getData("SELECT id FROM " + TABLE_PELANGGAN + "");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                //show detail dialog
                showDialogDetail(ListPelangganActivity.this, arrID.get(i));

            }
        });
    }

    private class TambahPelanggan implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(ListPelangganActivity.this, tambah_pelanggan.class);
            startActivity(i);
        }
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

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListPelangganActivity.this);
        dialogDelete.setTitle("PERINGATAN!!!");
        dialogDelete.setMessage("Apakah Anda Yakin Untuk Menghapus Data Pelanggan Ini ??");
        dialogDelete.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    mSQLiteHelper.deleteDataPelanggan(idRecord);
                    Toast.makeText(ListPelangganActivity.this, "Data Pelanggan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
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

        final TextView dlgNamaPel = dialog.findViewById(R.id.dlgNamaPel);
        final TextView dlgAlamat = dialog.findViewById(R.id.dlgAlamat);
        final TextView dlgKetPel = dialog.findViewById(R.id.dlgKetPel);

        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE id=" + position);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            dlgNamaPel.setText(nama);
            String alamat = cursor.getString(2);
            dlgAlamat.setText(alamat);
            String keterangan = cursor.getString(3);
            dlgKetPel.setText(keterangan);
        }

        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }


}