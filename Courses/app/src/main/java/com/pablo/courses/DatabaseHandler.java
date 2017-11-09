package com.pablo.courses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Pablo on 20-10-17
 * Source: https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DB_articles";

    private static final String TABLE_ARTICLES = "articles";

    private static final String KEY_ID = "ID";
    private static final String KEY_MAGASIN = "magasin";
    private static final String KEY_ARTICLE = "article";
    private static final String KEY_NBART = "nbArt";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //création de la table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ARTICLES_TABLE = "CREATE TABLE " + TABLE_ARTICLES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MAGASIN + " TEXT,"
                + KEY_ARTICLE + " TEXT," + KEY_NBART + " INTEGER" + ")";
        db.execSQL(CREATE_ARTICLES_TABLE);
    }

    //pour l'upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        onCreate(db);
    }

    //ajout d'un article
    public void addArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MAGASIN, article.getMagasin());
        values.put(KEY_ARTICLE, article.getArticle());
        values.put(KEY_NBART, article.getNbArt());

        db.insert(TABLE_ARTICLES, null, values);
        db.close();
    }

    //modifier un article (nom et/ou quantité)
    public void updateArticle(int articleID, String newArticle, int newNbArticles) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_ARTICLES+ " SET " + KEY_ARTICLE + "='" + newArticle + "', " + KEY_NBART + "=" + newNbArticles + " WHERE " + KEY_ID + "=" + articleID);
    }

    //suppression d'un article
    public void deleteArticle(int articleID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ARTICLES+ " WHERE " + KEY_ID + "=" + articleID);
        db.close();
    }

    //supprimer tous les articles d'un magasin
    public void deleteAll(String magasin){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ARTICLES + " WHERE " + KEY_MAGASIN + "='" + magasin + "'");
        db.close();
    }

    //liste des articles d'un magasin
    public List<Article> getAllArticles(String magasin) {
        List<Article> article_aList = new ArrayList<Article>();
        String selectQuery = "SELECT  * FROM " + TABLE_ARTICLES + " WHERE " + KEY_MAGASIN + "='" + magasin + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Article article = new Article();
                article.setID(Integer.parseInt(cursor.getString(0)));
                article.setMagasin(cursor.getString(1));
                article.setArticle(cursor.getString(2));
                article.setNbArt(Integer.parseInt(cursor.getString(3)));
                article_aList.add(article);
            } while (cursor.moveToNext());
        }
        return article_aList;
    }
}

