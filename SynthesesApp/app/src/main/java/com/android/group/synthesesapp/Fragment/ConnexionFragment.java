package com.android.group.synthesesapp.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group.synthesesapp.Activity.TeacherMainActivity;
import com.android.group.synthesesapp.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Piib on 31-12-17.
 */

public class ConnexionFragment extends DialogFragment {
    EditText champNom,champPrenom,champMdp;
    String nom,prenom,mdp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connexion, container, false);
        getDialog().setTitle("Connexion");

        champNom= (EditText) rootView.findViewById(R.id.nom);
        champPrenom= (EditText) rootView.findViewById(R.id.prenom);
        champMdp= (EditText) rootView.findViewById(R.id.mdp);

        //bouton connexion et listener
        Button connect = (Button) rootView.findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom=champNom.getText().toString();
                prenom=champPrenom.getText().toString();
                mdp=champMdp.getText().toString();
                if(nom.matches("") || prenom.matches("") || mdp.matches("")){
                    Toast.makeText(getActivity(), "Un des champs est vide", Toast.LENGTH_SHORT).show();
                }else{
                    new SendPostRequest().execute();
                }
            }
        });

        //bouton annuler et listener (dismiss si cliqué)
        Button annuler = (Button) rootView.findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://193.190.248.154/connexion.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Nom", nom);
                postDataParams.put("Prenom", prenom);
                postDataParams.put("Pwd", mdp);
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

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
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
            if(result.matches("UserX")){
                Toast.makeText(getActivity(), "Echec de connexion", Toast.LENGTH_SHORT).show();
            }

            if(result.matches("UserV")){
                Intent intent = new Intent(getActivity(), TeacherMainActivity.class);
                intent.putExtra("nomProf", nom);
                intent.putExtra("prenomProf",prenom);
                startActivity(intent);
            }

            if(!result.matches("UserX")&& !result.matches("UserV")){
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
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
}

