package com.example.administrator.rate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class ItemAdapter extends ArrayAdapter<Item> {
    private int layoutId;

    public ItemAdapter(Context context, int layoutId, List<Item> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
    }


  /*  public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);

        return

    }
    */
}
