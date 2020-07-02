package com.wahyu.rental;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ListSepedaActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Sepeda> mList;
    ListSepedaAdapter mAdapter = null;
    byte[] img = {};
    ImageView imageViewIcon;
    private static final String TAG = "ListSepedaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sepeda);

        mListView = findViewById(R.id.listSepeda);
        mList = new ArrayList<>();
        mAdapter = new ListSepedaAdapter(this, R.layout.row, mList);
        mListView.setAdapter(mAdapter);

        //get all data from sqlite
        Cursor cursor = tambah_sepeda.mSQLiteHelper.getData("SELECT * FROM Data_Sepeda");
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            int harga = cursor.getInt(2);
            String keterangan = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            //add to list
            Sepeda newSepeda = new Sepeda(id, nama, harga, keterangan, gambar);
            Gson gson = new Gson();
//            newSepeda.setGambar(img);
//            String sepeda = gson.toJson(newSepeda).toString();
//            Log.d(TAG, "onCreate: "+sepeda);
            mList.add(newSepeda);
        }
        mAdapter.notifyDataSetChanged();
        if (mList.size() == 0) {
            //if there is no record in table of database which means listview is empty
            Toast.makeText(this, "Maaf, Tidak Ada Data Sepeda Yang Ditemukan", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //alert dialog to display options of update and delete
                final CharSequence[] items = {"Update", "Hapus"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(ListSepedaActivity.this);

                dialog.setTitle("Mohon, Untuk Memilih Salah Satu Tombol");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //update
                            Cursor c = tambah_sepeda.mSQLiteHelper.getData("SELECT id FROM Data_Sepeda");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()) {
                                arrID.add(c.getInt(0));
                            }
                            //show update dialog
                            showDialogUpdate(ListSepedaActivity.this, arrID.get(position));
                        }
                        if (i == 1) {
                            //delete
                            Cursor c = tambah_sepeda.mSQLiteHelper.getData("SELECT id FROM Data_Sepeda");
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
                Cursor c = tambah_sepeda.mSQLiteHelper.getData("SELECT id FROM Data_Sepeda");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()) {
                    arrID.add(c.getInt(0));
                }
                //show detail dialog
                showDialogDetail(ListSepedaActivity.this, arrID.get(i));

            }
        });

    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(ListSepedaActivity.this);
        dialogDelete.setTitle("PERINGATAN!!!");
        dialogDelete.setMessage("Apakah Anda Yakin Untuk Menghapus Data Sepeda Ini ??");
        dialogDelete.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    tambah_sepeda.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(ListSepedaActivity.this, "Data Sepeda Berhasil Dihapus", Toast.LENGTH_SHORT).show();
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

    private void showDialogUpdate(Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");

        imageViewIcon = dialog.findViewById(R.id.ImgUpdGambar);
        final EditText edtUpdNama = dialog.findViewById(R.id.edtUpdNama);
        final EditText edtUpdHarga = dialog.findViewById(R.id.edtUpdHarga);
        final EditText edtUpdKet = dialog.findViewById(R.id.edtUpdKet);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdDataSpd);

        //get all data from sqlite
        Cursor cursor = tambah_sepeda.mSQLiteHelper.getData("SELECT * FROM Data_Sepeda WHERE id=" + position);
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            edtUpdNama.setText(nama);
            int harga = cursor.getInt(2);
            edtUpdHarga.setText(harga);
            String keterangan = cursor.getString(3);
            edtUpdKet.setText(keterangan);
            byte[] gambar = cursor.getBlob(4);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(gambar, 0, gambar.length));
            //add to list
            mList.add(new Sepeda(id, nama, harga, keterangan, gambar));
        }

        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set hieght of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        //in update dialog click image view to update image
        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check external storage permission
                ActivityCompat.requestPermissions(
                        ListSepedaActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tambah_sepeda.mSQLiteHelper.updateData(
                            edtUpdNama.getText().toString().trim(),
                            Integer.parseInt(edtUpdHarga.getText().toString().trim()),
                            edtUpdKet.getText().toString().trim(),
                            tambah_sepeda.imageViewToByte(imageViewIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Recipe Berhasil Dirubah", Toast.LENGTH_SHORT).show();
                } catch (Exception error) {
                    Log.e("Error", error.getMessage());
                }
                updateRecordList();
            }
        });

    }

    private void updateRecordList() {
        //get all data from sqlite
        Cursor cursor = tambah_sepeda.mSQLiteHelper.getData("SELECT * FROM Data_Sepeda");
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
        dialog.setContentView(R.layout.detail_dialog);
        dialog.setTitle("Detail Recipe");

        imageViewIcon = dialog.findViewById(R.id.detGambar);
        final TextView edtUpdNama = dialog.findViewById(R.id.detNama);
        final TextView edtUpdHarga = dialog.findViewById(R.id.detHarga);

        Button btnKembali = dialog.findViewById(R.id.btnKembali);

        //get all data from sqlite
        Cursor cursor = tambah_sepeda.mSQLiteHelper.getData("SELECT * FROM Data_Sepeda WHERE id=" + position);
        mList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nama = cursor.getString(1);
            edtUpdNama.setText(nama);
            int harga = cursor.getInt(2);
            edtUpdHarga.setText(harga+"");
            String keterangan = cursor.getString(3);
            byte[] gambar = cursor.getBlob(4);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(gambar, 0, gambar.length));
            //add to list
            mList.add(new Sepeda(id, nama, harga, keterangan, gambar));
        }

        //set width of dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                //get all data from sqlite
                Cursor cursor = tambah_sepeda.mSQLiteHelper.getData("SELECT * FROM Data_Sepeda");
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
        });

        mAdapter.notifyDataSetChanged();

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