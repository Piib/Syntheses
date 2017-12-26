package com.android.group.synthesesapp.Activity;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.group.synthesesapp.Adapater.PasswordAdapter;
import com.android.group.synthesesapp.Adapater.UserAdapter;
import com.android.group.synthesesapp.Modele.User;
import com.android.group.synthesesapp.Tool.BackgroundTask;
import com.android.group.synthesesapp.R;

import java.util.ArrayList;


public class ConnexionActivity extends AppCompatActivity {

    public UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<User> users= new ArrayList<>();
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));cd
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));
        users.add(new User("Borelli", "Geoffrey", 0, "0", "eleve"));
        users.add(new User("Amaterasu", "Landry", 1, "1", "eleve"));
        users.add(new User("Nicola", "Pablo", 2, "2", "eleve"));




        userAdapter = new UserAdapter(getApplicationContext(), users);

        GridView gridUser = (GridView) findViewById(R.id.gridUser);

        gridUser.setAdapter(userAdapter);

        gridUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                builder.setTitle("Symbole");
                GridView gridSymbole = new GridView(ConnexionActivity.this);
                ArrayList<String> listPass = new ArrayList<String>();
                listPass.add("icone_chat");
                listPass.add("icone_chien");
                listPass.add("icone_coccinelle");
                listPass.add("icone_cochon");
                listPass.add("icone_grenouille");
                listPass.add("icone_lapin");
                listPass.add("icone_penguin");
                listPass.add("icone_poulet");
                listPass.add("icone_vache");
                PasswordAdapter passwordAdapter = new PasswordAdapter(getApplicationContext(), listPass);
                gridSymbole.setNumColumns(3);
                gridSymbole.setAdapter(passwordAdapter);
                final int[] indexSymbole = {-1};
                final View[] precedentView = {null};
                gridSymbole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(precedentView[0] !=null){
                            precedentView[0].setBackgroundColor(Color.WHITE);
                        }
                        precedentView[0] =view;
                        indexSymbole[0] =position;
                        view.setBackgroundColor(Color.rgb(3, 183, 0));
                    }
                });
                builder.setView(gridSymbole);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("choixSymbole", String.valueOf(indexSymbole[0]));
                        if(indexSymbole[0]==position){
                            Toast.makeText(ConnexionActivity.this, "Connexion réussie", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(ConnexionActivity.this, "Connexion echouée", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ConnexionActivity.this, "Connexion annulée", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });








        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                appelServeur();

            }
        });
    }

    public void appelServeur(){
        BackgroundTask bgTask = new BackgroundTask();
        String [] param = new String[1];
        param[0]="ole";
        bgTask.execute("http://artshared.fr/andev1/base_ws.php");
    }


}
