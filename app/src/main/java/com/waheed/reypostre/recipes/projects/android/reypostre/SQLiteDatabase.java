package com.waheed.reypostre.recipes.projects.android.reypostre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import static android.R.id.content;
import static android.R.id.list;

/**
 * Created by waheed on 10/8/2017.
 */

public class SQLiteDatabase extends SQLiteOpenHelper {


    public SQLiteDatabase(Context context) {
        super(context,"fav.db",null,6);
    }


    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS recipe (name TEXT , url TEXT , time TEXT , level TEXT , serving TEXT , material TEXT , steps TEXT)");
        db.execSQL("create table IF NOT EXISTS cart (userTextInput TEXT, marketName TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT)");
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe");
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }

    public long WriteData (String name, String url, String time, String serving, String level, String material, String steps){
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("url",url);
        contentValues.put("time",time);
        contentValues.put("level",level);
        contentValues.put("serving",serving);
        contentValues.put("material",material);
        contentValues.put("steps",steps);

        return db.insert("recipe",null,contentValues);
    }

    public long AddToCart (String userInput, String marketName){
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userTextInput",userInput);
        contentValues.put("marketName",marketName);

        return db.insert("cart",null,contentValues);
    }

    public boolean isExist(String url,String name) {
        android.database.sqlite.SQLiteDatabase db ;
        Cursor cur;
        db = this.getReadableDatabase();
        cur = db.rawQuery("SELECT * FROM " + "recipe" + " WHERE url = '" + url + "' AND name = '" + name + "'", null);
        boolean exist = (cur.getCount() > 0);
        cur.close();
        db.close();
        return exist;

    }

    public long removeArticle(String url) {
        //Open the database
        android.database.sqlite.SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        //database.execSQL("DELETE FROM " + NewsEntry.TABLE_NAME + " WHERE " + NewsEntry.ID + " = '" + id + "' AND " + NewsEntry.ARTICLE_TITLE + " = '" + title + "'");

        long result = database.delete("recipe","url" + " = '" + url + "'",null);

        //Close the database
        database.close();

        return result;
    }

    public long removeFromCart (String userInput) {
        //Open the database
        android.database.sqlite.SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        //database.execSQL("DELETE FROM " + NewsEntry.TABLE_NAME + " WHERE " + NewsEntry.ID + " = '" + id + "' AND " + NewsEntry.ARTICLE_TITLE + " = '" + title + "'");

        long result = database.delete("cart","userTextInput" + " = '" + userInput + "'",null);

        //Close the database
        database.close();

        return result;
    }


    public long removeAllArticle(String table) {
        //Open the database
        android.database.sqlite.SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        //database.execSQL("DELETE FROM " + NewsEntry.TABLE_NAME + " WHERE " + NewsEntry.ID + " = '" + id + "' AND " + NewsEntry.ARTICLE_TITLE + " = '" + title + "'");

        long result = database.delete(table,null,null);

        //Close the database
        database.close();

        return result;
    }


    public ArrayList<recipe> ReadData() {
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<recipe> results = new ArrayList<recipe>();

        Cursor crs = db.rawQuery("select * from recipe", null);
        while (crs.moveToNext()) {
            recipe item = new recipe();
            item.setUrl(crs.getString(crs.getColumnIndex("url")));
            item.setName(crs.getString(crs.getColumnIndex("name")));
            item.setTime(crs.getString(crs.getColumnIndex("time")));
            item.setLevel(crs.getString(crs.getColumnIndex("level")));
            item.setServing(crs.getString(crs.getColumnIndex("serving")));
            item.setMaterial(crs.getString(crs.getColumnIndex("material")));
            item.setStep(crs.getString(crs.getColumnIndex("steps")));
            results.add(item);
        }

        db.close();
        return results;
    }


    public ArrayList<cart> ReadDataFromCart() {
        android.database.sqlite.SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<cart> results = new ArrayList<cart>();

        Cursor crs = db.rawQuery("select * from cart", null);
        while (crs.moveToNext()) {
            cart item = new cart();
            item.setUserInput(crs.getString(crs.getColumnIndex("userTextInput")));
            item.setMarketName(crs.getString(crs.getColumnIndex("marketName")));
            item.setId(crs.getInt(crs.getColumnIndex("id")));
            results.add(item);
        }

        db.close();
        return results;
    }



    public void updateItemInList(ContentValues values, int id) {

        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();

        db.update("cart",values,"id" + " = '" + id + "'",null);

    }


}
