package com.android.group.synthesesapp.Fragment;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group.synthesesapp.Activity.TeacherMainActivity;
import com.android.group.synthesesapp.R;

/**
 * Created by Piib on 31-12-17.
 */

public class ConnexionFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connexion, container, false);
        getDialog().setTitle("Connexion");

        final EditText champUtilisateur= (EditText) rootView.findViewById(R.id.utilisateur);

        //bouton connexion et listener
        Button connect = (Button) rootView.findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                try {
                    Intent intent = new Intent(getActivity(), TeacherMainActivity.class);
                    intent.putExtra("nomProf", "piib");
                    startActivity(intent);
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

    //redéfinition de la méthode onDismiss qui renvoie à la méthode du méme nom de ListActivity
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}

