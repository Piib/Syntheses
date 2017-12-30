package com.android.group.synthesesapp.Adapater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.group.synthesesapp.Modele.Entry;
import com.android.group.synthesesapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by geoffrey on 26/12/17.
 */

public class ReformuleAdapter extends ArrayAdapter<Entry> {
    public ReformuleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Entry> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Entry e = getItem(position);

        if (convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.reformule_adapter, null);
        }



        final TextView text = convertView.findViewById(R.id.reformuleText);
        final Button son = convertView.findViewById(R.id.reformuleSon);
        final ImageView image = convertView.findViewById(R.id.reformuleImage);

        text.setVisibility(View.INVISIBLE);
        son.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
        switch (e.getsType()){
            case "text" :
                text.setVisibility(View.VISIBLE);
                text.setText("texte "+e.getsContenu());
                break;
            case "son" :
                son.setVisibility(View.VISIBLE);
                son.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            final MediaPlayer mPlayer = new MediaPlayer();
                            mPlayer.setDataSource(e.getsContenu());
                            mPlayer.prepare();
                            mPlayer.start();
                        }
                        catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
                break;
            case "image" :
                image.setVisibility(View.VISIBLE);

                File imgFile = new  File(e.getsContenu());
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    image.setImageBitmap(myBitmap);
                }
                break;
        }



        return convertView;
    }
}
