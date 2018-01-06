package com.android.group.synthesesapp.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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

import com.android.group.synthesesapp.R;

/**
 * Created by Piib on 31-12-17.
 */

//source SendPostRequest, GetPostDataString: https://www.studytutorial.in/android-httpurlconnection-post-and-get-request-tutorial

public class AddClassFragment extends DialogFragment{
    boolean regenerateListView=false;
    EditText champClasse;
    String classe,nomProf,prenomProf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_class, container, false);
        getDialog().setTitle("Ajouter une clasee");

        nomProf = getArguments().getString("nomProf");
        prenomProf = getArguments().getString("prenomProf");

        champClasse=(EditText) rootView.findViewById(R.id.classe);

        //bouton ajout et listener
        Button ajout = (Button) rootView.findViewById(R.id.ajouter);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classe=champClasse.getText().toString();
                if(classe.matches("")){
                    Toast.makeText(getActivity(), "Entrez le nom de la classe", Toast.LENGTH_SHORT).show();
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

    //redéfinition de la méthode onDismiss qui renvoie à la méthode du méme champNom de TeacherMainActivity
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener && regenerateListView==true) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://193.190.248.154/lien_prof_classe.php"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Nom", nomProf);
                postDataParams.put("Prenom", prenomProf);
                postDataParams.put("NomClasse", classe);
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
        protected void onPostExecute(String result) { //réaction à la réponse du serveur
            if(result.matches("UserX")){
                Toast.makeText(getActivity(), "L'utilisateur n'existe pas", Toast.LENGTH_SHORT).show();
            }
            if(result.matches("ClassX")){
                //source: https://stackoverflow.com/questions/10207206/how-to-display-alertdialog-in-a-fragment
                AlertDialog ad = new AlertDialog.Builder(getActivity())
                        .create();
                ad.setCancelable(false);
                ad.setTitle("Classe non existante");
                ad.setMessage("La classe n'existe pas. Pour créer une classe créez un elève dans cette classe et ensuite ajoutez la");
                ad.setButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
            if(result.matches("LinkX")){
                Toast.makeText(getActivity(), "La classe vous est déjà attribuée", Toast.LENGTH_SHORT).show();
            }

            if(result.matches("LinkV")){
                regenerateListView=true;
                dismiss();
                //Toast.makeText(getActivity(), "Classe ajoutée avec succès", Toast.LENGTH_SHORT).show();
            }

            if(!result.matches("UserX")&& !result.matches("ClassX")&& !result.matches("LinkX")&& !result.matches("LinkV")){
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
