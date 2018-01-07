package com.android.group.synthesesapp.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.group.synthesesapp.Adapater.PasswordAdapter;
import com.android.group.synthesesapp.Adapater.UserAdapter;
import com.android.group.synthesesapp.Fragment.ConnexionFragment;
import com.android.group.synthesesapp.Modele.Entry;
import com.android.group.synthesesapp.Modele.User;
import com.android.group.synthesesapp.Tool.BackgroundTask;
import com.android.group.synthesesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ConnexionActivity extends AppCompatActivity {

    public UserAdapter userAdapter;


    private void requetePost(JSONObject jObject, final String url) {
        final OkHttpClient client = new OkHttpClient();
        Log.e("debut requete", "debut requete");
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jObject.toString());

        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Your Token")
                .addHeader("cache-control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.e("failure Response", url+" "+mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                String mMessage = response.body().string();
                if (response.isSuccessful()){

                    Log.e("success POST", mMessage);

                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions(new String[]{
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        final ArrayList<User> users;

        users = appelServeur("http://193.190.248.154/getAllStudent.php");
        userAdapter = new UserAdapter(getApplicationContext(), users);

        GridView gridUser = (GridView) findViewById(R.id.gridUser);

        gridUser.setAdapter(userAdapter);

        gridUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final User userSelected = users.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ConnexionActivity.this);
                builder.setTitle("Symbole");
                GridView gridSymbole = new GridView(ConnexionActivity.this);
                final ArrayList<String> listPass = new ArrayList<String>();
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
                final int[] indexSymbole = {-1};
                final View[] precedentView = {null};
                gridSymbole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(precedentView[0] !=null){
                            precedentView[0].setBackgroundColor(Color.WHITE);
                        }
                        precedentView[0] =view;
                        indexSymbole[0] =position;
                        view.setBackgroundColor(Color.rgb(3, 183, 0));
                    }
                });
                builder.setView(gridSymbole);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(userSelected.getsPwd().length()!=0) {
                            Log.e("ca passe", listPass.get(indexSymbole[0]) + " - " + userSelected.getsPwd());
                            if (listPass.get(indexSymbole[0]).equals(userSelected.getsPwd())) {
                                Toast.makeText(ConnexionActivity.this, "Connexion réussie", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ConnexionActivity.this, Phase_1_Activity.class);
                                intent.putExtra("user", userSelected);
                                startActivity(intent);


                            } else {
                                Toast.makeText(ConnexionActivity.this, "Connexion echouée", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            JSONObject changeMdp = new JSONObject();
                            try {
                                changeMdp.put("userId", userSelected.getiIdu());
                                changeMdp.put("password", listPass.get(indexSymbole[0]));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            requetePost(changeMdp, "http://193.190.248.154/setElevePwd.php");
                            userSelected.setsPwd(listPass.get(indexSymbole[0]));
                            Intent intent = new Intent(ConnexionActivity.this, Phase_1_Activity.class);
                            intent.putExtra("user", userSelected);
                            startActivity(intent);

                        }
                    }
                });
                builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ConnexionActivity.this, "Connexion annulée", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //appelServeur();

            }
        });
    }

    public ArrayList<User> appelServeur(String url){
        BackgroundTask bgTask = new BackgroundTask();
        String [] param = new String[1];
        param[0]="ole";
        ArrayList<User> users = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(bgTask.execute(url).get());
                if (jArray != null) {
                    Log.e("if", "vrai");
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jUser = new JSONObject(jArray.get(i).toString());
                        String nom = jUser.getString("Nom");
                        String prenom = jUser.getString("Prenom");
                        String pwd = jUser.getString("Pwd");
                        int idUser = jUser.getInt("idUser");
                        User u = new User(nom, prenom, idUser, pwd, "Elève");
                        users.add(u);
                    }
                }
                else {
                    Log.e("else", "faux");
                }
                Log.e("laListe", users.toString());
            }
            catch (Exception e){
                e.printStackTrace();
        }
        return users;
    }

    //affiche le menu dans l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_connexion, menu);
        return true;
    }

    //listener de l'action connexion du menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //afficher le fragment pour la connexion du professeur
        FragmentManager fm = getSupportFragmentManager();
        ConnexionFragment dialogFragment = new ConnexionFragment ();
        dialogFragment.show(fm, "fragment_connexion");
        return true;
    }


}
