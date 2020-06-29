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

    public void queryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String nama, int harga, String keterangan, byte[] gambar) {
        SQLiteDatabase database = getWritableDatabase();
        //query to insert record in database table
        String sql = "INSERT INTO Data_Sepeda VALUES(NULL, ?, ?, ?, ?)"; //where "Data_Sepeda" is table name in database we will create in mainActivity

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, nama);
        statement.bindLong(2, harga);
        statement.bindString(3, keterangan);
        statement.bindBlob(4, gambar);

        statement.executeInsert();
    }

    public void updateData(String nama, int harga, String keterangan, byte[] gambar, int id) {
        SQLiteDatabase database = getWritableDatabase();
        //query to update record
        String sql = "UPDATE Data_Sepeda SET nama=?, harga=?, keterangan=?, gambar=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, nama);
        statement.bindLong(2, harga);
        statement.bindString(3, keterangan);
        statement.bindBlob(4, gambar);
        statement.bindDouble(5, (double) id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM Data_Sepeda WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double) id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
