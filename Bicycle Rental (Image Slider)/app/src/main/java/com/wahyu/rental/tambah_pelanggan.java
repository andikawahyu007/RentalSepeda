package com.wahyu.rental;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

import static com.wahyu.rental.SQLiteHelper.TABLE_PELANGGAN;

public class tambah_pelanggan extends AppCompatActivity {

    EditText mEdtNama, mEdtAlamat, mEdtKetPel;
    Button mBtnTmbPelanggan;
    ImageView mImageAdd;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pelanggan);

        mEdtNama = findViewById(R.id.edtNamaPel);
        mEdtAlamat = findViewById(R.id.edtAlamat);
        mEdtKetPel = findViewById(R.id.edtKetPel);
        mBtnTmbPelanggan = findViewById(R.id.btnTmbPelanggan);

        mSQLiteHelper = SQLiteHelper.getInstance(this);

        mEdtNama.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEdtNama.setHint("");
                } else {
                    mEdtNama.setHint("Nama Pelanggan");
                }
            }
        });

        mEdtAlamat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEdtAlamat.setHint("");
                } else {
                    mEdtAlamat.setHint("Alamat");
                }
            }
        });

        mEdtKetPel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    mEdtKetPel.setHint("");
                } else {
                    mEdtKetPel.setHint("Keterangan");
                }
            }
        });

        mBtnTmbPelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mSQLiteHelper.insertDataPelanggan(
                            mEdtNama.getText().toString().trim(),
                            mEdtAlamat.getText().toString().trim(),
                            mEdtKetPel.getText().toString().trim()
                    );
                    Toast.makeText(tambah_pelanggan.this, "Data Pelanggan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();

                    mEdtNama.setText("");
                    mEdtAlamat.setText("");
                    mEdtKetPel.setText("");
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}