package com.android.group.synthesesapp.Modele;

/**
 * Created by geoffrey on 26/12/17.
 */

public class Entry {
    private int iId;
    private String sType;
    private String sContenu;
    private int iOrder;

    public Entry(int iId, String sType, String sContenu, int iOrder) {
        this.iId = iId;
        this.sType = sType;
        this.sContenu = sContenu;
        this.iOrder = iOrder;
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

    public int getiOrder() {
        return iOrder;
    }

    public void setiOrder(int iOrder) {
        this.iOrder = iOrder;
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
