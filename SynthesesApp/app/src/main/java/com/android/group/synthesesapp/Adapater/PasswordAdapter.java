package com.android.group.synthesesapp.Adapater;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.synthesesapp.Modele.User;
import com.android.group.synthesesapp.R;

import java.util.ArrayList;

/**
 * Created by geoffrey on 16/11/17.
 */

public class PasswordAdapter extends ArrayAdapter<String>{

    public PasswordAdapter(Context context, ArrayList<String> password) {
        super(context, 0, password);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.password, null);
        }

        final String p = getItem(position);

        final ImageView iPassword = convertView.findViewById(R.id.imagePassword);

        int drawable = GetImage(getContext(), p);

        iPassword.setImageResource(drawable);

        return convertView;
    }

    public static int GetImage(Context c, String ImageName) {
        //return c.getResources().getDrawable(c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName()));
        return c.getResources().getIdentifier(ImageName, "drawable", c.getPackageName());
    }
}
