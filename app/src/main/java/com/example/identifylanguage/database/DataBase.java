package com.example.identifylanguage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.identifylanguage.model.Note;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        // конструктор суперкласса
        super(context, "myDataBase", null, 1);
    }

    public static ArrayList<Note> loadNotes(Context context) {
        ArrayList<Note> notes = new ArrayList<>();
        DataBase dataBase = new DataBase(context);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.query("notes", null, null, null, null, null, null);
        String text;
        String resultOfIdentification;
        if (c.moveToFirst()) {
            int textColIndex = c.getColumnIndex("text");
            int resultOfIdentificationColIndex = c.getColumnIndex("resultOfIdentification");
            do {
                text = c.getString(textColIndex);
                resultOfIdentification = c.getString(resultOfIdentificationColIndex);
                notes.add(new Note(text, resultOfIdentification));
            } while (c.moveToNext());
        } else {
            c.close();
        }
        return notes;
    }

    public static void makeNote(Context context, String text, String resultOfIdentification) {
        ContentValues cv = new ContentValues();
        cv.put("text", text);
        cv.put("resultOfIdentification", resultOfIdentification);

        DataBase dataBase = new DataBase(context);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        db.insert("notes", null, cv);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table notes ("
                + "id integer primary key autoincrement,"
                + "text text,"
                + "resultOfIdentification text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
