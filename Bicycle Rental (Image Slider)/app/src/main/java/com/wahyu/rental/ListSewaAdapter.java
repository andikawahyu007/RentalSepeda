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

public class ListSewaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Sepeda> recordList;

    public ListSewaAdapter(Context context, int layout, ArrayList<Sepeda> recordList) {
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
        ImageView ImgBrsItem;
        TextView txtBrsNama, txtBrsHarga;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtBrsNama = row.findViewById(R.id.txtBrsNama);
            holder.txtBrsHarga = row.findViewById(R.id.txtBrsHarga);
            holder.ImgBrsItem = row.findViewById(R.id.ImgBrsItem);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Sepeda model = recordList.get(i);

        holder.txtBrsNama.setText(model.getNama());
        holder.txtBrsHarga.setText(model.getHarga()+"");

        byte[] recordGambar = model.getGambar();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordGambar, 0, recordGambar.length);
        holder.ImgBrsItem.setImageBitmap(bitmap);

        return row;
    }
}