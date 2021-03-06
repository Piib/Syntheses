package com.android.group.synthesesapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.content.Intent;
import android.graphics.Bitmap;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import android.util.Log;

import org.json.JSONObject;

import com.android.group.synthesesapp.R;

/**
 * Created by Piib on 31-12-17.
 */

public class AddUserFragment extends DialogFragment {
    Spinner spinner;
    EditText champNom, champPrenom,champClasse, champMdp;
    String nom,prenom,classe,mdp;
    int status=0; //par défaut ajout d'un élève

    Button GetImageFromGalleryButton;
    ImageView ShowSelectedImage;
    Bitmap FixBitmap;
    ByteArrayOutputStream byteArrayOutputStream;
    String ImageName = "image_data" ;
    String ServerUploadPath ="http://193.190.248.154/ajoutImage.php" ; //URL pour l'ajout de la photo de profil de l'élève
    byte[] byteArray ;
    String ConvertImage ;
    HttpURLConnection httpURLConnection ;
    URL urlImage;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_user, container, false);
        getDialog().setTitle("Ajouter un utilisateur");

        nom="";
        prenom="";
        classe="";
        mdp="";

        spinner=(Spinner) rootView.findViewById(R.id.statut);

        champNom =(EditText) rootView.findViewById(R.id.nom);

        champClasse =(EditText) rootView.findViewById(R.id.classe);

        champMdp =(EditText) rootView.findViewById(R.id.mdp);

        champPrenom=(EditText) rootView.findViewById(R.id.prenom);

        GetImageFromGalleryButton = (Button)rootView.findViewById(R.id.button);

        ShowSelectedImage = (ImageView) rootView.findViewById(R.id.imageView);

        //comme l'ajout d'un elève est par défaut, le champ du mot de passe est caché
        champMdp.setVisibility(View.GONE);
        champMdp.setVisibility(View.INVISIBLE);

        //affiche ou cache les champs en fonction de la position du spinner (élève ou professeur)
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0){
                    champMdp.setVisibility(View.GONE);
                    champMdp.setVisibility(View.INVISIBLE);
                    champClasse.setVisibility(View.VISIBLE);
                    GetImageFromGalleryButton.setVisibility(View.VISIBLE);
                    ShowSelectedImage.setVisibility(View.VISIBLE);
                }else{
                    champMdp.setVisibility(View.VISIBLE);
                    champClasse.setVisibility(View.GONE);
                    champClasse.setVisibility(View.INVISIBLE);
                    GetImageFromGalleryButton.setVisibility(View.GONE);
                    GetImageFromGalleryButton.setVisibility(View.INVISIBLE);
                    ShowSelectedImage.setVisibility(View.INVISIBLE);
                    ShowSelectedImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        //source pour l'ajout et envoi de l'image: https://www.android-examples.com/android-select-image-from-gallery-upload-to-server-example/

        byteArrayOutputStream = new ByteArrayOutputStream();

        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Sélectionner l'image depuis la galerie"), 1);

            }
        });

        Button ajout = (Button) rootView.findViewById(R.id.ajouter);
        ajout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom=champNom.getText().toString();
                prenom=champPrenom.getText().toString();
                classe=champClasse.getText().toString();
                mdp=champMdp.getText().toString();
                status=spinner.getSelectedItemPosition();
                if(status==0){ //eleve
                    if(nom.matches("") || prenom.matches("") || classe.matches("")){
                        Toast.makeText(getActivity(), "Un des champs est vide", Toast.LENGTH_SHORT).show();
                    }else{
                        if(ShowSelectedImage.getDrawable()==null){
                            Toast.makeText(getActivity(), "Ajoutez la photo de profil", Toast.LENGTH_SHORT).show();
                        }else{
                            //d'abord la requête serveur pour l'ajout dans la DB
                            new SendPostRequest().execute();
                        }
                    }
                }else{ //prof
                    if(nom.matches("") || prenom.matches("") || mdp.matches("")){
                        Toast.makeText(getActivity(), "Un des champs est vide", Toast.LENGTH_SHORT).show();
                    }else{
                        new SendPostRequest().execute();
                    }
                }
            }
        });

        Button annuler = (Button) rootView.findViewById(R.id.annuler);
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == Activity.RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                FixBitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), uri);

                ShowSelectedImage.setImageBitmap(FixBitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public void UploadImageToServer(){

        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected String doInBackground(Void... params) {

                ImageProcessClass imageProcessClass = new ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put(ImageName, ConvertImage);

                //passe le nom et prénom de l'élève pour le nom du fichier sur le serveur
                HashMapParams.put("nom",nom);
                HashMapParams.put("prenom",prenom);

                String FinalData = imageProcessClass.ImageHttpRequest(ServerUploadPath, HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                urlImage = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) urlImage.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null) {

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }
    }

    //source SendPostRequest, onPostExecute, GetPostDataString: https://www.studytutorial.in/android-httpurlconnection-post-and-get-request-tutorial

    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                JSONObject postDataParams = new JSONObject();

                //URL par défaut car ajout d'élève par défaut
                URL url= new URL("http://193.190.248.154/ajoutEleve.php");

                //URL et paramètres différents en fonciton de la position du spinner (élève/professeur)
                if(status==0){ //eleve
                    postDataParams.put("Nom", nom);
                    postDataParams.put("Prenom", prenom);
                    postDataParams.put("NomClasse", classe);
                }else{ //prof
                    url = new URL("http://193.190.248.154/ajoutProf.php");
                    postDataParams.put("Nom", nom);
                    postDataParams.put("Prenom", prenom);
                    postDataParams.put("Pwd", mdp);
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

        //réaction au code renvoyé par le serveur suite à la requête
        @Override
        protected void onPostExecute(String result) {
            if(result.matches("UserX")){
                Toast.makeText(getActivity(), "L'utilisateur existe déjà", Toast.LENGTH_SHORT).show();
            }

            //si la requête a été effectuée alors seulement l'image est envoyée au serveur (uniquement pour le formulaire de l'ajout d'élève)
            if(result.matches("LinkV")){
                Toast.makeText(getActivity(), "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();
                if(status==0){
                    UploadImageToServer();
                }
                dismiss();
            }

            //réponses du serveur si les entrées ne sont pas dans un bon format
            if(!result.matches("UserX")&& !result.matches("LinkV")){
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

