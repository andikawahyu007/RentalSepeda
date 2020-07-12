package com.wahyu.rental;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    SQLiteHelper(Context mContext,
                 String name,
                 SQLiteDatabase.CursorFactory factory,
                 int version) {
        super(mContext, name, factory, version);
    }

    public static String TABLE_SEPEDA = "Data_Sepeda";
    public static String TABLE_PELANGGAN = "Data_Pelanggan";
    public static String TABLE_SEWA = "Data_Transaksi_Sewa";
    private static int version_db = 5;
    private static String name_db = "RENTALDB.sqlite";
    private static SQLiteHelper instance = null;

    public static SQLiteHelper getInstance(Context ctx) {
        if (instance == null) {
            instance = new SQLiteHelper(ctx, name_db, null, version_db);
        }
        return instance;
    }

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertDataSepeda(String nama, int harga, String keterangan, byte[] gambar) {
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO " + TABLE_SEPEDA + " VALUES(NULL, ?, ?, ?, ?, ?)"; //where "" + TABLE_SEPEDA + "" is table name in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nama);
        statement.bindLong(2, harga);
        statement.bindString(3, keterangan);
        statement.bindBlob(4, gambar);
        statement.bindLong(5, 0);

        statement.executeInsert();
    }

    public void insertDataPelanggan(String nama, String alamat, String keterangan) {
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO " + TABLE_PELANGGAN + " VALUES(NULL, ?, ?, ?, ?)"; //where "" + TABLE_SEPEDA + "" is table name in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nama);
        statement.bindString(2, alamat);
        statement.bindString(3, keterangan);
        statement.bindLong(4, 0);
        statement.executeInsert();
    }

    public void insertDataSewa(String nama_sepeda, String nama_penyewa, String tanggal, int biaya, byte[] gambar) {
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO " + TABLE_SEWA + " VALUES(NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nama_sepeda);
        statement.bindString(2, nama_penyewa);
        statement.bindString(3, tanggal);
        statement.bindLong(4, biaya);
        statement.bindBlob(5, gambar);

        statement.executeInsert();
    }


    public void updateBiayaSewa(int biaya, int id) {
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE " + TABLE_SEWA + " SET biaya=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindLong(1, biaya);
        statement.bindLong(2, id);

        statement.execute();
        database.close();
    }

    public void updateData(String nama, int harga, String keterangan, byte[] gambar, int id) {
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE " + TABLE_SEPEDA + " SET nama=?, harga=?, keterangan=?, gambar=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, nama);
        statement.bindLong(2, harga);
        statement.bindString(3, keterangan);
        statement.bindBlob(4, gambar);
        statement.bindLong(5, id);

        statement.execute();
        database.close();
    }

    public void updateStatusSepeda(int status, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE " + TABLE_SEPEDA + " SET status=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindLong(1, status);
        statement.bindLong(2, id);

        statement.execute();
        database.close();
    }

    public void updateStatusPelanggan(int status, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE " + TABLE_PELANGGAN + " SET status=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindLong(1, status);
        statement.bindLong(2, id);

        statement.execute();
        database.close();
    }

    public void deleteDataSepeda(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_SEPEDA + " WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, id);

        statement.execute();
        database.close();
    }

    public void deleteDataPelanggan(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_PELANGGAN + " WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, id);

        statement.execute();
        database.close();
    }

    public void deleteDataSewa(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_SEWA + " WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindLong(1, id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SEPEDA + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR, harga INTEGER, keterangan VARCHAR, gambar BLOB, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PELANGGAN + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR, alamat INTEGER, keterangan VARCHAR, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SEWA + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nama_sepeda VARCHAR, nama_penyewa VARCHAR, tgl_sewa DATE, biaya INTEGER, gambar BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SEPEDA + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR, harga INTEGER, keterangan VARCHAR, gambar BLOB, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PELANGGAN + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nama VARCHAR, alamat INTEGER, keterangan VARCHAR, status INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SEWA + "(id INTEGER PRIMARY KEY AUTOINCREMENT, nama_sepeda VARCHAR, nama_penyewa VARCHAR, tgl_sewa DATE, biaya INTEGER, gambar BLOB)");

    }
}
