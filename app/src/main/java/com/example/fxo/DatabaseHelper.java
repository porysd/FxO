package com.example.fxo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    public static final String db_name = "Users_DB";

    private Context context;

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
        this.context = context;
    }



    // Create tables when the database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        // User login table
        db.execSQL("CREATE TABLE Users_tbl(usersID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, firstname TEXT, lastname TEXT, birthdate TEXT, contactno TEXT)");

        // Folders table
        db.execSQL("CREATE TABLE Folders_tbl(foldersID INTEGER PRIMARY KEY AUTOINCREMENT, folder TEXT, usersID INTEGER, FOREIGN KEY(usersID) REFERENCES Users_tbl(usersID))");

        // Flashcard folders table
        db.execSQL("CREATE TABLE FlashcardFolders_tbl(flashcardfolderID INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, subject TEXT, datecreated DATETIME DEFAULT CURRENT_TIMESTAMP, foldersID INTEGER, FOREIGN KEY(foldersID) REFERENCES Folders_tbl(foldersID))");

        // Flashcard question and answers table
        db.execSQL("CREATE TABLE Flashcards_tbl(flashcardID INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, answer TEXT, datecreated DATETIME DEFAULT CURRENT_TIMESTAMP, flashcardfolderID INTEGER, FOREIGN KEY(flashcardfolderID) REFERENCES FlashcardFolders_tbl(flashcardfolderID))");

        // Event Table
        db.execSQL("CREATE TABLE Event_tbl(eventID INTEGER PRIMARY KEY AUTOINCREMENT, eventName TEXT, eventDate TEXT, eventReminder TEXT, usersID INTEGER, FOREIGN KEY(usersID) REFERENCES Users_tbl(usersID))");
    }

    // Drop tables and recreate them if the database version is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users_tbl");
        db.execSQL("DROP TABLE IF EXISTS Folders_tbl");
        db.execSQL("DROP TABLE IF EXISTS FlashcardFolders_tbl");
        db.execSQL("DROP TABLE IF EXISTS Flashcards_tbl");
        db.execSQL("DROP TABLE IF EXISTS Event_tbl");
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

    // Method to get recently created flashcard folders
    public Cursor getRecentFlashcardFolders(int userID, int limit) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT ff.flashcardfolderID, ff.title, ff.subject, ff.datecreated " +
                "FROM FlashcardFolders_tbl ff " +
                "INNER JOIN Folders_tbl fo ON ff.foldersID = fo.foldersID " +
                "WHERE fo.usersID = ? " +
                "ORDER BY ff.datecreated DESC " +
                "LIMIT ?", new String[]{String.valueOf(userID), String.valueOf(limit)});
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

    //Method to retrieve the user data in Profile page

    public void getUserDetailsByID(int usersID){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT username, firstname, lastname, birthdate, contactno FROM Users_tbl WHERE usersID = ? ";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(usersID)});
        if (cursor != null && cursor.moveToFirst()) {
            User.getInstance().setUserName(cursor.getString(0));
            User.getInstance().setFirstName(cursor.getString(1));
            User.getInstance().setLastName(cursor.getString(2));
            User.getInstance().setBirthDate(cursor.getString(3));
            User.getInstance().setContactNo(cursor.getString(4));
            cursor.close();
        }
    }
    //Method to delete flashcardID
    public boolean deleteData(String flashcardFolderID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Flashcards_tbl", "flashcardfolderID = ?", new String[]{flashcardFolderID});
        int result = db.delete("FlashcardFolders_tbl", "flashcardfolderID = ?", new String[]{flashcardFolderID});
        return result > 0;
    }

    public boolean deleteFlashcardData(String flashcardID) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Flashcards_tbl", "flashcardID = ?", new String[]{flashcardID});
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

    public void updateFlashcard(String flashcardID, String question, String answer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", question);
        contentValues.put("answer", answer);
        long result = db.update("Flashcards_tbl", contentValues, "flashcardID=?", new String[]{flashcardID});
        if(result == -1){
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    public int getFolderIDByFlashcardFolderID(int flashcardFolderID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT foldersID FROM FlashcardFolders_tbl WHERE flashcardFolderID = ?", new String[]{String.valueOf(flashcardFolderID)});
        int folderID = -1;
        if (cursor != null && cursor.moveToFirst()) {
            folderID = cursor.getInt(0);
            cursor.close();
        }
        return folderID;
    }
    public String getFolderNameByFolderID(int folderID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT folder FROM Folders_tbl WHERE foldersID = ?", new String[]{String.valueOf(folderID)});
        String folderName = null;
        if (cursor != null && cursor.moveToFirst()) {
            folderName = cursor.getString(0);
            cursor.close();
        }
        return folderName;
    }
    public Boolean insertEventData (String eventName, String eventDate, String eventReminder, int usersID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eventName", eventName);
        contentValues.put("eventDate", eventDate);
        contentValues.put("eventReminder", eventReminder);
        contentValues.put("usersID", usersID);

        long result = db.insert("Event_tbl", null, contentValues);
        return result != -1;
    }

    public Cursor getUpcomingEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM Event_tbl ORDER BY eventDate ASC", null);
    }

    public boolean updateEvent(int eventId, String eventName, String eventDate, String eventReminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eventName", eventName);
        values.put("eventDate", eventDate);
        values.put("eventReminder", eventReminder);

        // Updating event record
        int rowsAffected = db.update("Event_tbl", values, "eventID = ?", new String[]{String.valueOf(eventId)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean deleteEvent(int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRows = db.delete("Event_tbl", "eventID=?", new String[]{String.valueOf(eventId)});
        return affectedRows > 0;
    }

    // Method to get all flashcards
    public Cursor getAllFlashcards() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT flashcardfolderID, title FROM FlashcardFolders_tbl", null);
    }
}

