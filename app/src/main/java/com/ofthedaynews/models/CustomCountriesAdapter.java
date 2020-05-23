/**
 * Author: Mohammed Basel Nasrini
 * Last edited: 2020-05-21
 */
package com.ofthedaynews.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ofthedaynews.R;

public class CustomCountriesAdapter extends ArrayAdapter {
    private Context context;
    private String[] names;
    private TypedArray images;

    public CustomCountriesAdapter(Context context, String[] names, TypedArray images) {
        super(context, R.layout.country_spinner_row, names);
        this.names = names;
        this.context = context;

        this.images = images;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.country_spinner_row, null);
        TextView tv = row.findViewById(R.id.txtCountry);
        ImageView iv = row.findViewById(R.id.flagImageView);
        tv.setText(names[position]);
        iv.setImageResource(images.getResourceId(position, -1));
        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.country_spinner_row, null);
        TextView tv = row.findViewById(R.id.txtCountry);
        ImageView iv = row.findViewById(R.id.flagImageView);
        tv.setText(names[position]);
        iv.setImageResource(images.getResourceId(position, -1));
        return row;
    }
}
