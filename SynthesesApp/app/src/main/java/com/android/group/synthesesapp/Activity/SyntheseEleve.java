package com.android.group.synthesesapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.group.synthesesapp.Fragment.ConnexionFragment;
import com.android.group.synthesesapp.R;

/**
 * Created by Piib on 02-01-18.
 */

public class SyntheseEleve extends AppCompatActivity{
    private String nomEleve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthseeleve);

        //récupération du nom du magasin depuis MainActivity et déterminer le titre en fonction du nom du magasin
        Intent intent = getIntent();
        nomEleve = intent.getStringExtra("nomEleve");
        setTitle("Synthèse de " + nomEleve);
    }

    //affiche le menu dans l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return true;
    }

    //listener de l'action ajout du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }
}
