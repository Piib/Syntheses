package com.pablo.courses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;

/**
 * Created by Nicolas Pablo on 19-10-17.
 */

public class MainActivity extends AppCompatActivity {
    private ImageButton aldi_button = null;
    private ImageButton carrefour_button = null;
    private ImageButton delhaize_button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //titre barre d'action
        setTitle("Liste des magasins");

        //récupération des widgets
        aldi_button = (ImageButton) findViewById(R.id.aldi_button);
        carrefour_button = (ImageButton) findViewById(R.id.carrefour_button);
        delhaize_button = (ImageButton) findViewById(R.id.delhaize_button);

        //listener sur les boutons
        aldi_button.setOnClickListener(shopListener);
        carrefour_button.setOnClickListener(shopListener);
        delhaize_button.setOnClickListener(shopListener);

        //quand le bouton exit du menu est appuyé dans ListActivity
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    //listener des boutons
    private View.OnClickListener shopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);

            switch (v.getId()) {
                case R.id.aldi_button:
                    //passage à ListActivity en passant le nom du magasin
                    intent.putExtra("nomMagasin", "Aldi");
                    startActivity(intent);
                    break;

                case R.id.carrefour_button:
                    intent.putExtra("nomMagasin", "Carrefour");
                    startActivity(intent);
                    break;

                case R.id.delhaize_button:
                    intent.putExtra("nomMagasin", "Delhaize");
                    startActivity(intent);
                    break;
            }
        }
    };

    //affiche le menu dans l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //listener de l'action exit du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
