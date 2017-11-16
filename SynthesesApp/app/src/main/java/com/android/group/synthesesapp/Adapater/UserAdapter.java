package com.android.group.synthesesapp.Adapater;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.synthesesapp.Modele.User;
import com.android.group.synthesesapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geoffrey on 16/11/17.
 */

public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, ArrayList<User> listBuilding) {
        super(context, 0, listBuilding);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.user, null);
        }

        final User u = getItem(position);

        final TextView uNom = convertView.findViewById(R.id.nomUser);
        uNom.setText(u.getsNom());

        return convertView;
    }
}
