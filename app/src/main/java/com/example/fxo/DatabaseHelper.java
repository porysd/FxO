package com.example.fxo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    public static final String db_name = "Users_DB";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
    }

    // Create tables when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "onCreate called");

        // User login table
        db.execSQL("CREATE TABLE Users_tbl(usersID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, firstname TEXT, lastname TEXT, birthdate TEXT, contactno TEXT)");

        // Folders table
        db.execSQL("CREATE TABLE Folders_tbl(foldersID INTEGER PRIMARY KEY AUTOINCREMENT, folder TEXT, usersID INTEGER, FOREIGN KEY(usersID) REFERENCES Users_tbl(usersID))");

        // Flashcard folders table
        db.execSQL("CREATE TABLE FlashcardFolders_tbl(flashcardfolderID INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subject TEXT, datecreated DATETIME DEFAULT CURRENT_TIMESTAMP, foldersID INTEGER, FOREIGN KEY(foldersID) REFERENCES Folders_tbl(foldersID))");

        // Flashcard question and answers table
        db.execSQL("CREATE TABLE Flashcards_tbl(flashcardID INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT, datecreated DATETIME DEFAULT CURRENT_TIMESTAMP, flashcardfolderID INTEGER, FOREIGN KEY(flashcardfolderID) REFERENCES FlashcardFolders_tbl(flashcardfolderID))");
    }

    // Drop tables and recreate them if the database version is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users_tbl");
        db.execSQL("DROP TABLE IF EXISTS Folders_tbl");
        db.execSQL("DROP TABLE IF EXISTS FlashcardFolders_tbl");
        db.execSQL("DROP TABLE IF EXISTS Flashcards_tbl");
        onCreate(db);
    }

    // Method to check if a username already exists
    public Boolean usernameAlreadyExisting(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users_tbl WHERE username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    // Method to check if the username and password match to login
    public Integer getUserIDIfMatch(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT usersID FROM Users_tbl WHERE username = ? AND password = ?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0); // Using 0 as the index for the first column
            cursor.close();
            return userId;
        }
        cursor.close();
        return null;
    }

    //
    public Integer getUserID(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT usersID FROM Users_tbl WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            int userID = cursor.getInt(0); // Using 0 as the index for the first column
            cursor.close();
            return userID;
        }
        cursor.close();
        return null;
    }
    public void insertFolderData(String folder, int userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("folder", folder);
        contentValues.put("usersID", userID);
        db.insert("Folders_tbl", null, contentValues);
    }

    // Method to get all folders
    public Cursor getFolders() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Folders_tbl", null);
    }

    // Method to get all flashcard folders
    public Cursor getFlashcardFolders() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM FlashcardFolders_tbl", null);
    }

    // Method to get all flashcards
    public Cursor getFlashcards() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Flashcards_tbl", null);
    }

    // Method to insert data into the flashcards table
    public Boolean insertFlashcardData(String question, String answer, int flashcardfolderID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", question);
        contentValues.put("answer", answer);
        contentValues.put("flashcardfolderID", flashcardfolderID);
        long result = db.insert("Flashcards_tbl", null, contentValues);
        return result != -1;
    }

    // Method to insert data into the flashcard folder table
    public Boolean insertFlashcardFolderData(String title, String subject, int foldersID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("subject", subject);
        contentValues.put("foldersID", foldersID);
        long result = db.insert("FlashcardFolders_tbl", null, contentValues);
        return result != -1;
    }

    // Method to insert user data
    public Boolean insertUserData(String username, String password, String firstname, String lastname, String birthdate, String contactno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("birthdate", birthdate);
        contentValues.put("contactno", contactno);
        long result = db.insert("Users_tbl", null, contentValues);
        return result != -1;
    }
    //Method to delete flashcardID
    public boolean deleteData(String flashcardID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("FlashcardFolders_tbl", "flashcardfolderID = ?", new String[]{flashcardID});
        return result > 0;
    }

    // Method to get folder ID by name
    public int getFolderIDByName(String folderName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT flashcardfolderID FROM FlashcardFolders_tbl WHERE title = ?", new String[]{folderName});
        int folderID = -1;
        if (cursor != null && cursor.moveToFirst()) {
            folderID = cursor.getInt(0);
            cursor.close();
        }
        return folderID;
    }
    public void resetAutoIncrement() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM FlashcardFolders_tbl", null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            if (count == 0) {
                db.execSQL("DELETE FROM sqlite_sequence WHERE name='FlashcardFolders_tbl'");
            } else {
                //table not empty, do not reset.
            }
        }
    }
}
