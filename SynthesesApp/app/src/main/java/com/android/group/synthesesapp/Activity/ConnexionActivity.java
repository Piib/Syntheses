package com.android.group.synthesesapp.Activity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

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
        users.add(new User("1"));
        users.add(new User("2"));
        users.add(new User("3"));
        users.add(new User("4"));
        users.add(new User("5"));
        users.add(new User("6"));
        users.add(new User("7"));
        users.add(new User("8"));
        users.add(new User("9"));
        users.add(new User("10"));
        users.add(new User("11"));
        users.add(new User("12"));
        users.add(new User("13"));
        users.add(new User("14"));
        users.add(new User("15"));
        users.add(new User("15"));
        users.add(new User("16"));
        users.add(new User("17"));

        userAdapter = new UserAdapter(getApplicationContext(), users);

        GridView gridUser = (GridView) findViewById(R.id.gridUser);

        gridUser.setAdapter(userAdapter);

        gridUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                builder.setView(gridSymbole);
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
