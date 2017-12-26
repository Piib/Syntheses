package com.android.group.synthesesapp.Adapater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.group.synthesesapp.Modele.Entry;
import com.android.group.synthesesapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffrey on 26/12/17.
 */

public class EnonceAdapter extends ArrayAdapter<Entry> {



    public EnonceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Entry> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.enconce_adapter, null);
        }

        final Entry e = getItem(position);

        final TextView text = convertView.findViewById(R.id.enconceText);
        text.setText(e.getsType());


        return convertView;
    }
}
