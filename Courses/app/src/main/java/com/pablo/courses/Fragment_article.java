package com.pablo.courses;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Nicolas Pablo on 21-10-17.
 */

public class Fragment_article extends DialogFragment {
    private DatabaseHandler db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_article, container, false);
        getDialog().setTitle("Modification de l'article");

        //récupération des valeurs du bundle
        final String nomMagasin = getArguments().getString("nomMagasin");
        String nomArticle = getArguments().getString("article");
        String nombreArticles= getArguments().getString("nbArticles");
        final int articleID=getArguments().getInt("articleID");

        //réupération des widgets et ajout du nom de l'article et la quantité aux EditText
        final EditText article= (EditText) rootView.findViewById(R.id.article);
        final EditText nbArticles= (EditText) rootView.findViewById(R.id.nbArticles);
        article.setText(nomArticle);
        nbArticles.setText(nombreArticles);

        //bouton OK et listener
        Button ok = (Button) rootView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update de l'article puis dismiss
                try {
                    db = new DatabaseHandler(getActivity());
                    db.updateArticle(articleID, article.getText().toString(), Integer.parseInt(nbArticles.getText().toString()));
                    dismiss();
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Mauvaise entrée", Toast.LENGTH_SHORT).show();
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

        //bouton effacer et listener
        Button effacer = (Button) rootView.findViewById(R.id.effacer);
        effacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //suppression de l'article puis dismiss
                db = new DatabaseHandler(getActivity());
                db.deleteArticle(articleID);
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
