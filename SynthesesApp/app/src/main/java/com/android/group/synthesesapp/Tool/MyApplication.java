package com.android.group.synthesesapp.Tool;

import android.app.Application;

/**
 * Created by Piib on 02-01-18.
 */

//pour le partage de valeurs
//source: https://stackoverflow.com/questions/8573796/keeping-a-variable-value-across-all-android-activities

public class MyApplication extends Application {
    public String nomProf="";
    public String classe="";
    public String eleve="";
}
