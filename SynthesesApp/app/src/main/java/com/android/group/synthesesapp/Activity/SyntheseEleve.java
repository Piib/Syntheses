package com.android.group.synthesesapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.group.synthesesapp.R;
import com.android.group.synthesesapp.Tool.Share;

/**
 * Created by Piib on 02-01-18.
 */

public class SyntheseEleve extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synthseeleve);

        setTitle("Synthèse de " + ((Share) getApplicationContext()).nomEleve + " " +((Share) getApplicationContext()).prenomEleve);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}