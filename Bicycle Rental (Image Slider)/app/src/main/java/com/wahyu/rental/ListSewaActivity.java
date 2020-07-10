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

import static com.wahyu.rental.SQLiteHelper.TABLE_SEWA;
import static com.wahyu.rental.SQLiteHelper.TABLE_SEWA;

public class ListSewaActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Sewa> mList;
    ListSewaAdapter mAdapter = null;
    byte[] img = {};
    ImageView imageViewIcon;
    private static final String TAG = "ListSewaActivity";
    public static SQLiteHelper mSQLiteHelper;
    ImageButton mLihatSepeda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sewa);

        mListView = findViewById(R.id.listSewa);
        mLihatSepeda = findViewById(R.id.btnTmbSewa);
        mList = new ArrayList<>();
        mAdapter = new ListSewaAdapter(this, R.layout.baris_item_sewa, mList);
        mListView.setAdapter(mAdapter);
        mSQLiteHelper = SQLiteHelper.getInstance(this);
        //get all data from sqlite
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEWA + "");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama_sepeda = cursor.getString(1);
            String nama_penyewa = cursor.getString(2);
            String tanggal = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            Sewa newSewa = new Sewa(id, nama_sepeda, nama_penyewa, tanggal, gambar);
//            Gson gson = new Gson();
//            newSepeda.setGambar(img);
//            String sepeda = gson.toJson(newSepeda).toString();
//            Log.d(TAG, "onCreate: "+sepeda);
            mList.add(newSewa);
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size() == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Maaf, Tidak Ada Data Sewa Yang Ditemukan", Toast.LENGTH_SHORT).show();
        }

        mLihatSepeda.setOnClickListener(new PilihanSepeda());

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Hapus"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(ListSewaActivity.this);

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //update
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM " + TABLE_SEWA + "");
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
                Cursor c = mSQLiteHelper.getData("SELECT id FROM " + TABLE_SEWA + "");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                //show detail dialog
                showDialogDetail(ListSewaActivity.this, arrID.get(i));


            }
        });
    }

    private class PilihanSepeda implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(ListSewaActivity.this, PilihSepeda.class);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEWA + "");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama_sepeda = cursor.getString(1);
            String nama_penyewa = cursor.getString(2);
            String tanggal = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            Sewa newSewa = new Sewa(id, nama_sepeda, nama_penyewa, tanggal, gambar);
            Gson gson = new Gson();
//            newSepeda.setGambar(img);
//            String sepeda = gson.toJson(newSepeda).toString();
//            Log.d(TAG, "onCreate: "+sepeda);
            mList.add(newSewa);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListSewaActivity.this);
        dialogDelete.setTitle("PERINGATAN!!!");
        dialogDelete.setMessage("Apakah Anda Yakin Untuk Menghapus Data Sepeda Ini ??");
        dialogDelete.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(ListSewaActivity.this, "Data Sepeda Berhasil Dihapus", Toast.LENGTH_SHORT).show();
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
        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEWA + "");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama_sepeda = cursor.getString(1);
            String nama_penyewa = cursor.getString(2);
            String tanggal = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            Sewa newSewa = new Sewa(id, nama_sepeda, nama_penyewa, tanggal, gambar);
            Gson gson = new Gson();
//            newSepeda.setGambar(img);
//            String sepeda = gson.toJson(newSepeda).toString();
//            Log.d(TAG, "onCreate: "+sepeda);
            mList.add(newSewa);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void showDialogDetail(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_detail_sewa);

        imageViewIcon = dialog.findViewById(R.id.dlgGambarSewa);
        final TextView edtUpdNama = dialog.findViewById(R.id.dlgDisewa);
        final TextView edtUpdHarga = dialog.findViewById(R.id.dlgPenyewa);
        final TextView dlgTglSewa = dialog.findViewById(R.id.dlgTglSewa);

        Cursor cursor = mSQLiteHelper.getData("SELECT * FROM " + TABLE_SEWA + " WHERE id=" + position);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            edtUpdNama.setText(nama);
            int harga = cursor.getInt(2);
            edtUpdHarga.setText(harga + "");
            String keterangan = cursor.getString(3);
            dlgTglSewa.setText(keterangan);
            byte[] gambar = cursor.getBlob(4);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(gambar, 0, gambar.length));
        }

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