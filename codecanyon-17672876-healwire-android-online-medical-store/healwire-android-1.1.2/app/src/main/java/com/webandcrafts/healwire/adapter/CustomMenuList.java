package com.webandcrafts.healwire.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webandcrafts.healwire.R;

/**
 * Created by vineeth on 7/29/2016.
 */
public class CustomMenuList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public CustomMenuList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.drawer_list_item, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.drawer_list_item, null, true);
        TextView textViewMenuItem = (TextView) rowView.findViewById(R.id.textViewMenuItem);

        ImageView imageViewMenuIcon = (ImageView) rowView.findViewById(R.id.imageViewMenuIcon);
        textViewMenuItem.setText(web[position]);

        imageViewMenuIcon.setImageResource(imageId[position]);
        return rowView;
    }


}




