package com.android.group.synthesesapp.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.group.synthesesapp.Adapater.EnonceAdapter;
import com.android.group.synthesesapp.Adapater.ReformuleAdapter;
import com.android.group.synthesesapp.Modele.Entry;
import com.android.group.synthesesapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Phase_1_Activity extends AppCompatActivity {

    private ImageView mImageView;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phase_1_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestPermissions(new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);


        //Initialisation listeEnoncé
        ArrayList<Entry> enconceList = new ArrayList<>();

        enconceList.add(new Entry(0, "typeEnonce0", "conetenu"));
        enconceList.add(new Entry(1, "typeEnonce1", "conetenu"));
        enconceList.add(new Entry(2, "typeEnonce2", "conetenu"));
        enconceList.add(new Entry(3, "typeEnonce3", "conetenu"));

        ListView listEnonce = (ListView) findViewById(R.id.listEnonce);
        EnonceAdapter enonceAdapter = new EnonceAdapter(getBaseContext(), 0, enconceList);
        listEnonce.setAdapter(enonceAdapter);

        //Initialisation listReformulé
        final ArrayList<Entry> reformuleList = new ArrayList<>();

        reformuleList.add(new Entry(4, "typeReformule0", "contenu"));
        reformuleList.add(new Entry(5, "typeReformule1", "contenu"));
        reformuleList.add(new Entry(6, "typeReformule2", "contenu"));
        reformuleList.add(new Entry(7, "typeReformule3", "contenu"));

        final ListView listReformule = (ListView) findViewById(R.id.listReformule);
        final ReformuleAdapter reformuleAdapter = new ReformuleAdapter(getBaseContext(), 0, reformuleList);
        listReformule.setAdapter(reformuleAdapter);

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
                        if(choix[0]==1) {
                            Entry newEntry = newAudio(reformuleAdapter);
                            reformuleList.add(newEntry);
                        }
                        if(choix[0]==2) {
                            Entry newEntry = newImage(reformuleAdapter);
                            reformuleList.add(newEntry);
                        }
                    }
                });
                builder.show();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.e("dataError", data.toString());
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
//        }
//    }

    private Entry newAudio(final ReformuleAdapter reformuleAdapter) {
        final Entry newSong = new Entry(0, "son", null);
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
        Entry newImage = new Entry(0, "image", String.valueOf(photoFile));
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

}
