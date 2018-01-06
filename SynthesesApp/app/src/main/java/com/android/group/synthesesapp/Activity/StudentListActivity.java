package com.android.group.synthesesapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.group.synthesesapp.R;
import com.android.group.synthesesapp.Tool.MyApplication;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Piib on 02-01-18.
 */

public class StudentListActivity extends AppCompatActivity {
    private ListView eleves_listView;
    private ArrayList<String> eleve_aList;
    private ArrayAdapter<String> listAdapter ;
    private String[] listeEleves, manipulationNom;
    private String nomCompletEleve,prenomEleve,nomEleve,typeRequete;

    private URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlistactivity);

        //titre barre d'action
        setTitle("Elèves de " + ((MyApplication) getApplicationContext()).classe);

        typeRequete="Load";
        new SendPostRequest().execute();
    }

    private void generateListView() {
        //récupération du widget
        eleves_listView = (ListView) findViewById( R.id.eleves_listView );

        //liste des articles
        eleve_aList = new ArrayList<String>();

        for(int i = 0; i< listeEleves.length; i++){
            manipulationNom = listeEleves[i].split(":");
            nomCompletEleve =manipulationNom[0]+" "+manipulationNom[1];
            eleve_aList.add(nomCompletEleve);
        }

        //création de l'adapteur avec la liste
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_eleve, eleve_aList);

        //assigner l'adapteur à la listview
        eleves_listView.setAdapter( listAdapter );

        //ajouter un écouteur
        eleves_listView.setOnItemClickListener(itemListener);

        eleves_listView.setOnItemLongClickListener(itemLongClickListenner);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                JSONObject postDataParams = new JSONObject();
                if(typeRequete.matches("Load")){
                    url = new URL("http://193.190.248.154/requeteEleves.php"); // here is your URL path
                    postDataParams.put("Classe", ((MyApplication) getApplicationContext()).classe);
                }else{
                    url = new URL("http://193.190.248.154/deleteEleve.php"); // here is your URL path
                    postDataParams.put("Nom", nomEleve);
                    postDataParams.put("Prenom", prenomEleve);
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
                listeEleves =result.split(";");
                if(listeEleves[0]!="") { //si il y a au moins une classe
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
            String item= (String) eleves_listView.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), SyntheseEleve.class);
            int espace=item.indexOf(" ");
            nomEleve=item.substring(0,espace);
            prenomEleve=item.substring(espace+1);
            ((MyApplication) getApplicationContext()).nomEleve=nomEleve;
            ((MyApplication) getApplicationContext()).prenomEleve=prenomEleve;
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClickListenner=new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int pos, long id) {
            String item= (String) eleves_listView.getItemAtPosition(pos);
            int espace=item.indexOf(" ");
            nomEleve=item.substring(0,espace);
            prenomEleve=item.substring(espace+1);

            //source: https://stackoverflow.com/questions/8227820/alert-dialog-two-buttons
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentListActivity.this);
            builder.setMessage("Etes-vous sûr de supprimer cet élève?")
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
