package com.example.fxo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String db_name = "userDB";
    public DatabaseHelper(Context context){
        super(context, db_name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Users_tbl(usersID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, firstname TEXT, lastname TEXT, birthdate TEXT, contactno TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Users_tbl");
    }

    public Boolean insertData(String username, String password, String firstname, String lastname, String birthdate, String contactno){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("birthdate", birthdate);
        contentValues.put("contactno", contactno);
        long result = db.insert("Users_tbl", null, contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Boolean checkusername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users_tbl WHERE username = ?", new String[]{username});
        if (cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users_tbl WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }
}
