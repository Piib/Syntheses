package com.android.group.synthesesapp.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.group.synthesesapp.Adapater.EnonceAdapter;
import com.android.group.synthesesapp.Adapater.ReformuleAdapter;
import com.android.group.synthesesapp.Fragment.ConnexionFragment;
import com.android.group.synthesesapp.Modele.Entry;
import com.android.group.synthesesapp.Modele.User;
import com.android.group.synthesesapp.R;
import com.android.group.synthesesapp.Tool.BackgroundTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.ToDoubleBiFunction;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Phase_1_Activity extends AppCompatActivity {

    private String mCurrentPhotoPath;
    private ArrayList<Entry> enconceList;
    private ArrayList<Entry> reformuleList;
    private ArrayList<Entry> ajoutListe;
    private User user;

    //affiche le menu dans l'action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phase_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
        JSONObject listEnvoie = new JSONObject();
        try {
            listEnvoie.put("userId", user.getiIdu());
            listEnvoie.put("phase", 1);
            JSONArray arrayEntries = new JSONArray();
            for(Entry entry : ajoutListe){
                JSONObject jEntry = new JSONObject();
                jEntry.put("order", entry.getiOrder());
                jEntry.put("type", entry.getsType());
                if (entry.getsType()=="text") {
                    jEntry.put("content", entry.getsContenu());
                }
                if (entry.getsType()=="son"){
                    File file = new File(entry.getsContenu());
                    byte[] data = new byte[(int) file.length()];
                    try {
                        new FileInputStream(file).read(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String encoded = Base64.encodeToString(data, Base64.DEFAULT);
                    jEntry.put("content", encoded);
                }
                if (entry.getsType()=="image"){
                    Bitmap bm = BitmapFactory.decodeFile(entry.getsContenu());
                    ByteArrayOutputStream bao = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                    byte[] ba = bao.toByteArray();
                    String encoded = Base64.encodeToString(ba, Base64.DEFAULT);
                    jEntry.put("content", encoded);
                    //Log.e("byteImage", bytes);
                }
                arrayEntries.put(jEntry);
            }
            listEnvoie.put("entries", arrayEntries);
            Log.e("resultat", listEnvoie.toString());
            requetePost(listEnvoie, "http://193.190.248.154/ajoutNote.php");
            Intent intent = new Intent(Phase_1_Activity.this, Phase_2_Activity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return true;
    }

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
        setContentView(R.layout.activity_phase_1_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = getIntent().getParcelableExtra("user");



        //Initialisation listeEnoncé
        enconceList = new ArrayList<>();



        enconceList.add(new Entry(0, "typeEnonce0", "conetenu", 0));
        enconceList.add(new Entry(1, "typeEnonce1", "conetenu", 1));
        enconceList.add(new Entry(2, "typeEnonce2", "conetenu", 2));
        enconceList.add(new Entry(3, "typeEnonce3", "conetenu", 3));

        ListView listEnonce = (ListView) findViewById(R.id.listEnonce);
        EnonceAdapter enonceAdapter = new EnonceAdapter(getBaseContext(), 0, enconceList);
        listEnonce.setAdapter(enonceAdapter);


        //Initialisation listReformulé
        reformuleList = appelServeur("http://193.190.248.154/getNote.php?userId="+user.getiIdu()+"&&phase=1");
        ajoutListe=new ArrayList<>();

        final ListView listReformule = (ListView) findViewById(R.id.listReformule);
        final ReformuleAdapter reformuleAdapter = new ReformuleAdapter(getBaseContext(), 0, reformuleList);
        listReformule.setAdapter(reformuleAdapter);
        listReformule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Phase_1_Activity.this);

                ImageView imv = new ImageView(Phase_1_Activity.this);
                imv.setImageDrawable(getDrawable(R.drawable.icone_delete));
                imv.setMinimumWidth(500);
                imv.setMinimumHeight(500);
                RelativeLayout rl = new RelativeLayout(Phase_1_Activity.this);
                rl.addView(imv);

                builder
                        .setView(rl)
                        .setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if(ajoutListe.indexOf(reformuleList.get(position))==-1){
                                    Log.e("deleteTruc", "est du serveur");
                                    JSONObject jObject = new JSONObject();
                                    try {
                                        jObject.put("order", reformuleList.get(position).getiOrder());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    requetePost(jObject, "http://193.190.248.154/deleteNote.php");
                                }
                                else {
                                    Log.e("deleteTruc", "estPasServeur");
                                    ajoutListe.remove(reformuleList.get(position));
                                }
                                reformuleList.remove(position);
                                reformuleAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        })
                        .show();

            }
        });

        final String[] choixAjout = new String[3];
        choixAjout[0]="Text";
        choixAjout[1]="Son";
        choixAjout[2]="Image";



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] choix = {-1};
                AlertDialog.Builder builder = new AlertDialog.Builder(Phase_1_Activity.this);
                builder.setTitle("Ajouter +");
//                builder.setMessage("ok");
                builder.setSingleChoiceItems(choixAjout, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choix[0] =which;
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(choix[0]==0) {
                            Entry newEntry = newText(reformuleAdapter);
                            reformuleList.add(newEntry);
                            ajoutListe.add(newEntry);
                            reformuleAdapter.notifyDataSetChanged();
                        }
                        if(choix[0]==1) {
                            Entry newEntry = newAudio(reformuleAdapter);
                            reformuleList.add(newEntry);
                            ajoutListe.add(newEntry);
                            reformuleAdapter.notifyDataSetChanged();

                        }
                        if(choix[0]==2) {
                            Entry newEntry = newImage(reformuleAdapter);
                            reformuleList.add(newEntry);
                            ajoutListe.add(newEntry);
                            reformuleAdapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private Entry newText(final ReformuleAdapter reformuleAdapter) {
        final Entry newText = new Entry(0, "text", null, reformuleList.size());
        AlertDialog.Builder builder= new AlertDialog.Builder(Phase_1_Activity.this);
        builder.setTitle("TEXTE");
        RelativeLayout content = new RelativeLayout(Phase_1_Activity.this);
        final EditText textView = new EditText(Phase_1_Activity.this);
        content.addView(textView);
        textView.setMinWidth(content.getMeasuredWidth());
        builder.setView(content);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newText.setsContenu(textView.getText().toString());
                reformuleAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
        return newText;
    }

    private Entry newAudio(final ReformuleAdapter reformuleAdapter) {
        final Entry newSong = new Entry(0, "son", null, reformuleList.size());
        //////////////////////////////////////////////////////////////////
        final MediaRecorder mMediaRecorder = new MediaRecorder();
        final String soundFilePath=getFilePath();
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            mMediaRecorder.setOutputFile(soundFilePath);

            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(Phase_1_Activity.this);
        final AlertDialog d = builder.create();
        d.setTitle("SON");
        final LinearLayout content = new LinearLayout(Phase_1_Activity.this);
        final Button bStart = new Button(Phase_1_Activity.this);
        final Button bStop = new Button(Phase_1_Activity.this);
        bStop.setText("stop");
        bStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.removeView(bStop);
                mMediaRecorder.stop();
                mMediaRecorder.release();
                MediaPlayer mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(soundFilePath);
                    mPlayer.prepare();
                    mPlayer.start();
                    d.dismiss();
                    reformuleAdapter.notifyDataSetChanged();
                    newSong.setsContenu(soundFilePath);
                } catch (IOException e) {
                    Log.e("testAudio", "prepare() failed");
                }
            }
        });
        bStart.setText("start");
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaRecorder.start();
                content.removeView(bStart);
                content.addView(bStop);
            }
        });
        content.addView(bStart);
        d.setView(content);
        d.show();



        //////////////////////////////////////////////////////////////////
        return newSong;
    }

    private Entry newImage(final ReformuleAdapter reformuleAdapter){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        File photoFile = null;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("errorGeo", "error la");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.e("teste", String.valueOf(photoFile));
                Log.e("filepathgeo", getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath());
                Uri photoURI = FileProvider.getUriForFile(Phase_1_Activity.this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
        Entry newImage = new Entry(0, "image", String.valueOf(photoFile), reformuleList.size());
        reformuleAdapter.notifyDataSetChanged();
        return newImage;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = String.valueOf(new Date().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("photofile", mCurrentPhotoPath);
        return image;
    }





    private String getFilePath() {

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "Synthese");

        if (!file.exists())
            file.mkdirs();
        String d=String.valueOf(new Date().getTime());
        file = new File(filepath, "Synthese/SoundRecorded_"+d);
        return (file.getAbsolutePath() + ".mp4");
    }

    public ArrayList<Entry> appelServeur(String url){
        BackgroundTask bgTask = new BackgroundTask();
        String [] param = new String[1];
        param[0]="ole";
        ArrayList<Entry> entries = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(bgTask.execute(url).get());
            if (jArray != null) {
                Log.e("if", "vrai");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jEntry = new JSONObject(jArray.get(i).toString());
                    int order = jEntry.getInt("order");
                    String type = jEntry.getString("type");
                    String content = jEntry.getString("content");
                    switch (type){
                        case "image":{
                            String imageName = "image_"+new Date().getTime();
                            byte[] pdfAsBytes = Base64.decode(content, 0);
                            File filePath = new File(Environment.getExternalStorageDirectory()+"/"+imageName+".jpg");
                            FileOutputStream os = new FileOutputStream(filePath, true);
                            os.write(pdfAsBytes);
                            os.close();
                            content=Environment.getExternalStorageDirectory()+"/"+imageName+".jpg";
                            break;
                        }
                        case "son":{
                            String sonName = "son"+new Date().getTime();
                            byte[] pdfAsBytes = Base64.decode(content, 0);
                            File filePath = new File(Environment.getExternalStorageDirectory()+"/"+sonName+".jpg");
                            FileOutputStream os = new FileOutputStream(filePath, true);
                            os.write(pdfAsBytes);
                            os.close();
                            content=Environment.getExternalStorageDirectory()+"/"+sonName+".jpg";
                            break;
                        }
                    }
                    Entry e = new Entry(0, type, content, order);
                    entries.add(e);
                }
            }
            else {
                Log.e("else", "faux");
            }
            Log.e("laListe", entries.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return entries;
    }

}
