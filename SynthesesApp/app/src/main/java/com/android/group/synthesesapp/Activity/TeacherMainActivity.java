package com.android.group.synthesesapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.group.synthesesapp.Fragment.AddClassFragment;
import com.android.group.synthesesapp.Fragment.AddUserFragment;
import com.android.group.synthesesapp.R;
import com.android.group.synthesesapp.Tool.MyApplication;

import java.util.ArrayList;

/**
 * Created by Piib on 31-12-17.
 */

public class TeacherMainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
    private ListView classes_listView ;
    private ArrayList<String> classe_aList;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachermainactivity);

        Intent intentTeacher = getIntent();
        String nomProf=intentTeacher.getStringExtra("nomProf");
        if (nomProf != null) {
            ((MyApplication) getApplicationContext()).nomProf=nomProf;
        }

        //titre barre d'action
        setTitle("Classes de " + ((MyApplication) getApplicationContext()).nomProf);
        //appel à la méthode generateListView pour créer la listview
        generateListView();
    }

    private void generateListView() {
        //récupération du widget
        classes_listView = (ListView) findViewById( R.id.classes_listView );

        //liste des articles
        classe_aList = new ArrayList<String>();

        classe_aList.add("CP A");
        classe_aList.add("CP B");
        classe_aList.add("CM1 A");

        //création de l'adapteur avec la liste
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_classe, classe_aList);

        //assigner l'adapteur à la listview
        classes_listView.setAdapter( listAdapter );

        //ajouter un écouteur
        classes_listView.setOnItemClickListener(itemListener);
    }

    //listener quand un item de la listview est cliqué
    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            String item= (String) classes_listView.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), StudentListActivity.class);
            ((MyApplication) getApplicationContext()).classe=item;
            startActivity(intent);
        }
    };

    //exécution de code quand dismiss() est appelé dans le DialogFragment
    @Override
    public void onDismiss(final DialogInterface dialog) {
        //refresh la listview après avoir cliquer sur OK ou Effacer du DialogFragment
        generateListView();
    }

    //affiche le menu dans l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher, menu);
        return true;
    }

    //listener des actions du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_addUser:
                FragmentManager fmUser = getSupportFragmentManager();
                AddUserFragment dialogFragmentUser = new AddUserFragment ();
                dialogFragmentUser.show(fmUser, "frgament_ajout_utilisateur");
                return true;

            case R.id.action_addClass:
                Bundle bundle = new Bundle();
                bundle.putString("nomProf", ((MyApplication) getApplicationContext()).nomProf);

                FragmentManager fmClass = getSupportFragmentManager();
                AddClassFragment dialogFragment = new AddClassFragment ();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fmClass, "frgament_ajout_classe");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
