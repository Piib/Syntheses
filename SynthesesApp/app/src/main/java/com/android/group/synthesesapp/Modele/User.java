package com.android.group.synthesesapp.Modele;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by geoffrey on 16/11/17.
 */

public class User implements Parcelable {

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

    protected User(Parcel in) {
        sNom = in.readString();
        sPrenom = in.readString();
        iIdu = in.readInt();
        sPwd = in.readString();
        sTatut = in.readString();
        bmPhoto = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sNom);
        dest.writeString(sPrenom);
        dest.writeInt(iIdu);
        dest.writeString(sPwd);
        dest.writeString(sTatut);
        dest.writeParcelable(bmPhoto, flags);
    }
}
