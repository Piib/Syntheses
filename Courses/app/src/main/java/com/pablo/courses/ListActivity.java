package com.pablo.courses;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Pablo on 20-10-17.
 */

public class ListActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
    private String nomMagasin;
    private DatabaseHandler db=new DatabaseHandler(this);;
    private ListView courses_listView ;
    private List<Article> articles_list;
    private ArrayList<String> article_aList;
    private ArrayList<Integer> id_aList;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //récupération du nom du magasin depuis MainActivity et déterminer le titre en fonction du nom du magasin
        Intent intent = getIntent();
        nomMagasin = intent.getStringExtra("nomMagasin");
        setTitle(nomMagasin);

        //appel à la méthode generateListView pour créer la listview
        generateListView();
    }

    private void generateListView() {
        //récupération du widget
        courses_listView = (ListView) findViewById( R.id.courses_listView );

        //liste des articles
        article_aList = new ArrayList<String>();

        //liste des ID des articles
        id_aList = new ArrayList<Integer>();

        //remplir la liste avec les articles
        articles_list = db.getAllArticles(nomMagasin);

        //pour chaque article dans la liste ajouter son nom et le nombre ainsi que son ID
        for (Article cn : articles_list) {
            article_aList.add(cn.getNbArt()+"x "+cn.getArticle());
            id_aList.add(cn.getID());
        }

        //création de l'adapteur avec la liste
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, article_aList);

        //assigner l'adapteur à la listview
        courses_listView.setAdapter( listAdapter );

        //ajouter un écouteur
        courses_listView.setOnItemClickListener(itemListener);
    }

    //listener quand un item de la listview est cliqué
    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            //récupération de l'item et de l'ID de l'article sélectionné
            String item= (String) courses_listView.getItemAtPosition(position);
            int articleID=id_aList.get(position);

            //manipulation de string pour séparer le nom et la quantité de l'article
            int x=item.indexOf("x");
            String article= item.substring(x+2);
            String nbArticles=item.substring(0,x);

            //bundle pour passer les valeurs au DialogFragment
            Bundle bundle = new Bundle();
            bundle.putString("article", article);
            bundle.putString("nbArticles", nbArticles);
            bundle.putString("nomMagasin",nomMagasin);
            bundle.putInt("articleID",articleID);

            //affichage du DialogFragment en passant le bundle en argument
            FragmentManager fm = getSupportFragmentManager();
            Fragment_article dialogFragment = new Fragment_article ();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(fm, "frgament_edit_article");
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
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    //listener des actions du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                //ajout article et refresh de la listview
                db.addArticle(new Article(nomMagasin,"NomArticle",1));
                generateListView();
                return true;

            case R.id.action_delete:
                //suppression de la liste de courses pour un magasin et refresh de la listview
                db.deleteAll(nomMagasin);
                generateListView();
                return true;

            case R.id.action_quit:
                //retour à MainActivity pour quitter l'application
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
