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
import com.android.group.synthesesapp.Tool.Share;

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
    private ArrayList<Integer> id_aList; //contient l'id de chaque élève pour l'envoyer a l'activité suivante
    private String[] listeEleves, manipulationNom;
    private String nomCompletEleve,prenomEleve,nomEleve,typeRequete; //typeRequete différencie le chargement de la suppresion pour la requête serveur
    private URL url; //URL pour les requêtes Load ou Delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlistactivity);

        setTitle("Elèves de " + ((Share) getApplicationContext()).classe);

        typeRequete="Load";
        new SendPostRequest().execute();
    }

    private void generateListView() {
        eleves_listView = (ListView) findViewById( R.id.eleves_listView );

        eleve_aList = new ArrayList<String>();

        id_aList = new ArrayList<Integer>();

        //découpe encore une fois le resultat de la requête pour obtenir le nom et prénom de l'élève
        for(int i = 0; i< listeEleves.length; i++){
            manipulationNom = listeEleves[i].split(":");
            nomCompletEleve =manipulationNom[0]+" "+manipulationNom[1];
            eleve_aList.add(nomCompletEleve);
            //recupère l'idUser et ajout dans l'ordre de la liste
            id_aList.add(Integer.parseInt(manipulationNom[2]));
        }

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow_eleve, eleve_aList);

        eleves_listView.setAdapter( listAdapter );

        eleves_listView.setOnItemClickListener(itemListener);

        eleves_listView.setOnItemLongClickListener(itemLongClickListenner);
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                //si Load alors URL et paramètres différents que Delete
                JSONObject postDataParams = new JSONObject();
                if(typeRequete.matches("Load")){
                    url = new URL("http://193.190.248.154/requeteEleves.php");
                    postDataParams.put("Classe", ((Share) getApplicationContext()).classe);
                }else{
                    url = new URL("http://193.190.248.154/deleteEleve.php");
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

        //réaction au code renvoyé par le serveur suite à la requête
        @Override
        protected void onPostExecute(String result) {
            if(typeRequete.matches("Load")){
                //découpe le resultats en fonction des séparteurs ;
                listeEleves =result.split(";");
                if(listeEleves[0]!="") { //si il y a au moins un eleve
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

    private AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            //découpe l'item avec l'espace comme séparateur pour déterminer le nom et prénom de l'élève
            String item= (String) eleves_listView.getItemAtPosition(position);
            int espace=item.indexOf(" ");
            nomEleve=item.substring(0,espace);
            prenomEleve=item.substring(espace+1);
            ((Share) getApplicationContext()).nomEleve=nomEleve;
            ((Share) getApplicationContext()).prenomEleve=prenomEleve;
            //on connait l'idUser en le récupérant de id_aList avec la position dans la liste
            ((Share) getApplicationContext()).idEleve=id_aList.get(position);

            Intent intent = new Intent(getApplicationContext(), SyntheseEleve.class);
            startActivity(intent);
        }
    };

    private AdapterView.OnItemLongClickListener itemLongClickListenner=new AdapterView.OnItemLongClickListener(){
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int pos, long id) {
            //découpe l'item avec l'espace comme séparateur pour déterminer le nom et prénom de l'élève
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
