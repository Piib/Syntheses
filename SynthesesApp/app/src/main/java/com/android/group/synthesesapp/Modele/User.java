package com.android.group.synthesesapp.Modele;

/**
 * Created by geoffrey on 16/11/17.
 */

public class User {

    protected String sNom;

    public User(String sNom) {
        this.sNom=sNom;
    }

    public String getsNom() {
        return sNom;
    }

    public void setsNom(String sNom) {
        this.sNom = sNom;
    }
}
