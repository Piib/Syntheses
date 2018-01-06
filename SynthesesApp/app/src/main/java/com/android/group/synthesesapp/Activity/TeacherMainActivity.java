package com.android.group.synthesesapp.Activity;

import android.app.AlertDialog;
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

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;

import android.util.Log;

import org.json.JSONObject;

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
    private String[] classes;
    private String nomClasse,typeRequete;

    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachermainactivity);

        Intent intentTeacher = getIntent();
        String nomProf=intentTeacher.getStringExtra("nomProf");
        String prenomProf=intentTeacher.getStringExtra("prenomProf");
        if (nomProf != null && prenomProf != null) {
            ((MyApplication) getApplicationContext()).nomProf=nomProf;
            ((MyApplication) getApplicationContext()).prenomProf=prenomProf;
        }

        //titre barre d'action
        setTitle("Classes de " + ((MyApplication) getApplicationContext()).nomProf + " " + ((MyApplication) getApplicationContext()).prenomProf);

        typeRequete="Load";
        new SendPostRequest().execute();
    }

    private void generateListView() {
        //récupération du widget
        classes_listView = (ListView) findViewById( R.id.classes_listView );

        //liste des articles
        classe_aList = new ArrayList<String>();

        for(int i=0;i<classes.length;i++){
            classe_aList.add(classes[i]);
        }

        //création de l'adapteur avec la liste
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_classe, classe_aList);

        //assigner l'adapteur à la listview
        classes_listView.setAdapter( listAdapter );

        //ajouter un écouteur
        classes_listView.setOnItemClickListener(itemListener);

        classes_listView.setOnItemLongClickListener(itemLongClickListenner);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                JSONObject postDataParams = new JSONObject();
                if(typeRequete.matches("Load")){
                    url = new URL("http://193.190.248.154/requeteClasses.php"); // here is your URL path
                    postDataParams.put("Nom", ((MyApplication) getApplicationContext()).nomProf);
                    postDataParams.put("Prenom", ((MyApplication) getApplicationContext()).prenomProf);
                }else{
                    url = new URL("http://193.190.248.154/deleteClasse.php"); // here is your URL path
                    postDataParams.put("Classe", nomClasse);
                }
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if(typeRequete.matches("Load")){
                classes=result.split(";");
                if(classes[0]!="") { //si il y a au moins une classe
                    //appel à la méthode generateListView pour créer la listview
                    generateListView();
                }
            }else{
                typeRequete="Load";
                new SendPostRequest().execute();
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
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
        new SendPostRequest().execute();
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
                bundle.putString("prenomProf",((MyApplication) getApplicationContext()).prenomProf);

                FragmentManager fmClass = getSupportFragmentManager();
                AddClassFragment dialogFragment = new AddClassFragment ();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(fmClass, "frgament_ajout_classe");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private AdapterView.OnItemLongClickListener itemLongClickListenner=new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int pos, long id) {
            nomClasse= (String) classes_listView.getItemAtPosition(pos);


            //source: https://stackoverflow.com/questions/8227820/alert-dialog-two-buttons
            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMainActivity.this);
            builder.setMessage("Etes-vous sûr de supprimer cette classe et tous ses élèves?")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            typeRequete="Delete";
                            new SendPostRequest().execute();
                        }
                    })
                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
    };
}
