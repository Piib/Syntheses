package com.android.group.synthesesapp.Modele;

/**
 * Created by geoffrey on 26/12/17.
 */

public class Entry {
    private int iId;
    private String sType;
    private String sContenu;

    public Entry(int iId, String sType, String sContenu) {
        this.iId = iId;
        this.sType = sType;
        this.sContenu = sContenu;
    }

    public int getiId() {
        return iId;
    }

    public void setiId(int iId) {
        this.iId = iId;
    }

    public String getsType() {
        return sType;
    }

    public void setsType(String sType) {
        this.sType = sType;
    }

    public String getsContenu() {
        return sContenu;
    }

    public void setsContenu(String sContenu) {
        this.sContenu = sContenu;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "iId=" + iId +
                ", sType='" + sType + '\'' +
                ", sContenu='" + sContenu + '\'' +
                '}';
    }
}
