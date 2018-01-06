package com.android.group.synthesesapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.group.synthesesapp.Fragment.AddUserFragment;
import com.android.group.synthesesapp.R;
import com.android.group.synthesesapp.Tool.MyApplication;

/**
 * Created by Piib on 02-01-18.
 */

public class SyntheseEleve extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthseeleve);

        setTitle("Synthèse de " + ((MyApplication) getApplicationContext()).nomEleve + " " +((MyApplication) getApplicationContext()).prenomEleve);
    }

    //affiche le menu dans l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return true;
    }

    //listener de l'action connexion du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}