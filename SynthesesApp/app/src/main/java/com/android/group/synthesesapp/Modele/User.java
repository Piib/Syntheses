package com.android.group.synthesesapp.Modele;

import android.graphics.Bitmap;

/**
 * Created by geoffrey on 16/11/17.
 */

public class User {

    protected String sNom;
    protected String sPrenom;
    protected int iIdu;
    protected String sPwd;
    protected String sTatut;
    protected Bitmap bmPhoto;

    public User(String sNom, String sPrenom, int iIdu, String sPwd, String sTatut) {
        this.sNom = sNom;
        this.sPrenom = sPrenom;
        this.iIdu = iIdu;
        this.sPwd = sPwd;
        this.sTatut = sTatut;
    }

    public String getsNom() {
        return sNom;
    }

    public void setsNom(String sNom) {
        this.sNom = sNom;
    }

    public String getsPrenom() {
        return sPrenom;
    }

    public void setsPrenom(String sPrenom) {
        this.sPrenom = sPrenom;
    }

    public int getiIdu() {
        return iIdu;
    }

    public void setiIdu(int iIdu) {
        this.iIdu = iIdu;
    }

    public String getsPwd() {
        return sPwd;
    }

    public void setsPwd(String sPwd) {
        this.sPwd = sPwd;
    }

    public String getsTatut() {
        return sTatut;
    }

    public void setsTatut(String sTatut) {
        this.sTatut = sTatut;
    }

    public Bitmap getBmPhoto() {
        return bmPhoto;
    }

    public void setBmPhoto(Bitmap bmPhoto) {
        this.bmPhoto = bmPhoto;
    }
}
