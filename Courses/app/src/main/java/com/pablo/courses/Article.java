package com.pablo.courses;

/**
 * Created by Nicolas Pablo on 20-10-17.
 * Source: https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */

public class Article {
    int _id;
    String _magasin;
    String _article;
    int _nbArt;

    public Article(){

    }

    public Article(int id, String magasin, String article, int nbArt) {
        this._id = id;
        this._magasin = magasin;
        this._article = article;
        this._nbArt = nbArt;
    }

    public Article(String magasin, String article, int nbArt) {
        this._magasin = magasin;
        this._article = article;
        this._nbArt = nbArt;
    }

    public int getID() {
        return _id;
    }

    public void setID(int id) {
        this._id = id;
    }

    public String getMagasin() {
        return _magasin;
    }

    public void setMagasin(String magasin) {
        this._magasin = magasin;
    }

    public String getArticle() {
        return _article;
    }

    public void setArticle(String article) {
        this._article = article;
    }

    public int getNbArt() {
        return _nbArt;
    }

    public void setNbArt(int nbArt) {
        this._nbArt = nbArt;
    }
}
