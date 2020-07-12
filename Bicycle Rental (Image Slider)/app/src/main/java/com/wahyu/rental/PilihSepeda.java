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

public class PilihSepeda extends AppCompatActivity {

    ListView mListView;
    ArrayList<Sepeda> mList;
    ListSepedaAdapter mAdapter = null;
    byte[] img = {};
    ImageView imageViewIcon;
    private static final String TAG = "PilihSepeda";
    public static SQLiteHelper mSQLiteHelper;
    public static String EXTRA_ID_PELANGGAN = "extra_id_pelanggan";
    private int id_sepeda;
    private Sepeda sepeda;
    private Pelanggan pelanggan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_sepeda);

        mListView = findViewById(R.id.listSepeda);
        mList = new ArrayList<>();
        mAdapter = new ListSepedaAdapter(this, R.layout.baris_item_sepeda, mList);
        mListView.setAdapter(mAdapter);
        mSQLiteHelper = SQLiteHelper.getInstance(this);
        if (getIntent() != null) {
            id_sepeda = getIntent().getIntExtra(EXTRA_ID_PELANGGAN, -1);
        }
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            String alamat = cursor.getString(2);
            String keterangan = cursor.getString(3);

            //add to list
            pelanggan = new Pelanggan(id, nama, alamat, keterangan);
        }
//            Gson gson = new Gson();
//            newPelanggan.setGambar(img);
//            String Pelanggan = gson.toJson(newPelanggan).toString();
//            Log.d(TAG, "onCreate: "+Pelanggan);
        //get all data from sqlite
        cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEPEDA + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            int harga = cursor.getInt(2);
            String keterangan = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            sepeda = new Sepeda(id, nama, harga, keterangan, gambar);
//            Gson gson = new Gson();
//            newSepeda.setGambar(img);
//            String sepeda = gson.toJson(newSepeda).toString();
//            Log.d(TAG, "onCreate: "+sepeda);
            mList.add(sepeda);
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size() == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Maaf, Tidak Ada Data Sepeda Yang Ditemukan", Toast.LENGTH_SHORT).show();
        }


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //read
                Cursor c = mSQLiteHelper.getData("SELECT id FROM " + TABLE_SEPEDA + " WHERE status = 0");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                //show detail dialog
                showDialogDetail(PilihSepeda.this, arrID.get(i));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEPEDA + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            int harga = cursor.getInt(2);
            String keterangan = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            Sepeda newSepeda = new Sepeda(id, nama, harga, keterangan, gambar);
//            Gson gson = new Gson();
//            newSepeda.setGambar(img);
//            String sepeda = gson.toJson(newSepeda).toString();
//            Log.d(TAG, "onCreate: "+sepeda);
            mList.add(newSepeda);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void updateRecordList() {
        //get all data from sqlite
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEPEDA + " WHERE status = 0");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            int harga = cursor.getInt(2);
            String keterangan = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            mList.add(new Sepeda(id, nama, harga, keterangan, gambar));
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showDialogDetail(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_detail_order);

        imageViewIcon = dialog.findViewById(R.id.dlgImgSepedaOrder);
        final TextView dlgSepedaDiOrder = dialog.findViewById(R.id.dlgSepedaDiOrder);
        final TextView dlgPenyewa = dialog.findViewById(R.id.dlgPenyewa);
        final TextView dlgTglDiOrder = dialog.findViewById(R.id.dlgTglDiOrder);
        final TextView dlgBiayaOrder = dialog.findViewById(R.id.dlgBiayaOrder);
        final Button btnSewa = dialog.findViewById(R.id.btnSimpanOrder);

        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEPEDA + " WHERE id=" + position);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            dlgSepedaDiOrder.setText(nama);
            int harga = cursor.getInt(2);
            dlgBiayaOrder.setText(harga + "");
            byte[] gambar = cursor.getBlob(4);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(gambar, 0, gambar.length));
        }
        cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_PELANGGAN + " WHERE id=" + position);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            dlgPenyewa.setText(nama);
        }

        java.util.Date sekarang = new java.util.Date();
        java.text.SimpleDateFormat kalender = new java.text.SimpleDateFormat("yyyy-MM-dd");
        dlgTglDiOrder.setText(kalender.format(sekarang));

        btnSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Date sekarang = new java.util.Date();
                java.text.SimpleDateFormat kalender = new java.text.SimpleDateFormat("yyyy-MM-dd");

                try {
                    mSQLiteHelper.insertDataSewa(sepeda.getNama(), pelanggan.getNama(), kalender.format(sekarang), sepeda.getHarga(), sepeda.getGambar());
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
                    Intent i = new Intent(PilihSepeda.this, ListSewaActivity.class);
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

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //gallery intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
            } else {
                Toast.makeText(this, "Maaf, Anda Tidak Mempunyai Akses Untuk Gallery", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 888 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON) //enable image guidlines
                    .setAspectRatio(1, 1)// image will be square
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                imageViewIcon.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}