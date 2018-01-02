package com.android.group.synthesesapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.group.synthesesapp.R;

import java.util.ArrayList;

/**
 * Created by Piib on 02-01-18.
 */

public class StudentListActivity extends AppCompatActivity {
    private ListView eleves_listView;
    private ArrayList<String> eleve_aList;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlistactivity);

        //titre barre d'action
        setTitle("Liste des élèves");

        //appel à la méthode generateListView pour créer la listview
        generateListView();
    }

    private void generateListView() {
        //récupération du widget
        eleves_listView = (ListView) findViewById( R.id.eleves_listView );

        //liste des articles
        eleve_aList = new ArrayList<String>();

        eleve_aList.add("Nicolas Pablo");
        eleve_aList.add("Borelli Geoffrey");
        eleve_aList.add("Amatala Landry");

        //création de l'adapteur avec la liste
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_eleve, eleve_aList);

        //assigner l'adapteur à la listview
        eleves_listView.setAdapter( listAdapter );

        //ajouter un écouteur
        eleves_listView.setOnItemClickListener(itemListener);
    }

    //listener quand un item de la listview est cliqué
    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            String item= (String) eleves_listView.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), SyntheseEleve.class);
            intent.putExtra("nomEleve", item);
            startActivity(intent);
        }
    };
}
