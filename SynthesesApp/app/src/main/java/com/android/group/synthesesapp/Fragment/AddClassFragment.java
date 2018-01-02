package com.android.group.synthesesapp.Fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.group.synthesesapp.R;

/**
 * Created by Piib on 31-12-17.
 */

public class AddClassFragment extends DialogFragment{
    private boolean regenerateListView=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_class, container, false);
        getDialog().setTitle("Ajouter une clasee");

        //bouton connexion et listener
        Button ajout = (Button) rootView.findViewById(R.id.ajouter);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                try {
                    //ajouter entrée à la DB si la classe existe pas
                    //ajouter à la liste si la classe existe déjà
                        regenerateListView=true;
                    dismiss();
                }catch(Exception e){
                    //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //bouton annuler et listener
        Button annuler = (Button) rootView.findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss si cliqué
                dismiss();
            }
        });

        return rootView;
    }

    //redéfinition de la méthode onDismiss qui renvoie à la méthode du méme nom de TeacherMainActivity
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener && regenerateListView==true) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
