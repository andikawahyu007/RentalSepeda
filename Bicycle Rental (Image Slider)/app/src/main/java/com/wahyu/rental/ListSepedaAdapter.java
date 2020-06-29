package com.wahyu.rental;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListSepedaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<InputData> recordList;

    public ListSepedaAdapter(Context context, int layout, ArrayList<InputData> recordList) {
        this.context = context;
        this.layout = layout;
        this.recordList = recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        ImageView ImgRowGambar;
        TextView txtRowNama, txtRowHarga;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtRowNama = row.findViewById(R.id.txtRowNama);
            holder.txtRowHarga = row.findViewById(R.id.txtRowHarga);
            holder.ImgRowGambar = row.findViewById(R.id.ImgRowGambar);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        InputData model = recordList.get(i);

        holder.txtRowNama.setText(model.getNama());
        holder.txtRowHarga.setText(model.getHarga());

        byte[] recordGambar = model.getGambar();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordGambar, 0, recordGambar.length);
        holder.ImgRowGambar.setImageBitmap(bitmap);

        return row;
    }
}