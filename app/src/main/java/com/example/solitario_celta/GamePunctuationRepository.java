package com.example.solitario_celta;

import static com.example.solitario_celta.GamePunctuationContract.GamePunctuationEntry;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class GamePunctuationRepository extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = GamePunctuationEntry.TABLE_NAME;


    public GamePunctuationRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        this.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GamePunctuationEntry.TABLE_NAME ;
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insert(GamePunctuation gamePunctuation) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insert = "INSERT INTO " + DATABASE_NAME + " (" +
                GamePunctuationEntry.COLUMN_NAME_ID + "," +
                GamePunctuationEntry.COLUMN_NAME_PLAYERNAME + "," +
                GamePunctuationEntry.COLUMN_NAME_DATETIME + "," +
                GamePunctuationEntry.COLUMN_NAME_MISSINGPIECES + ") "+
                "VALUES (" +
                null + ", " +
                "'" + gamePunctuation.getPlayerName() + "'" + ", " +
                "'" + gamePunctuation.getDateTime() + "'" + ", " +
                gamePunctuation.getPieces() + ")";
        db.execSQL(insert);
        db.close();
    }



    public ArrayList<GamePunctuation> getAll() {
        String consultaSQL = "SELECT * FROM " + GamePunctuationEntry.TABLE_NAME +
                " ORDER BY " + GamePunctuationEntry.COLUMN_NAME_MISSINGPIECES;
        ArrayList<GamePunctuation> listaPuntuaciones = new ArrayList<>();

        // Accedo a la DB en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(consultaSQL, null);

            if (cursor.moveToFirst()) {

                    while(cursor.moveToNext()) {
                        GamePunctuation puntuacion = new GamePunctuation(
                                cursor.getString(cursor.getColumnIndexOrThrow(GamePunctuationEntry.COLUMN_NAME_PLAYERNAME)),
                                cursor.getString(cursor.getColumnIndexOrThrow(GamePunctuationEntry.COLUMN_NAME_DATETIME)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(GamePunctuationEntry.COLUMN_NAME_MISSINGPIECES)),
                                (int) cursor.getLong(cursor.getColumnIndexOrThrow(GamePunctuationEntry.COLUMN_NAME_ID)));
                                listaPuntuaciones.add(puntuacion);
                    }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return listaPuntuaciones;
    }

    public void deleteInformation (){
        SQLiteDatabase db = this.getWritableDatabase();
        final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + GamePunctuationEntry.TABLE_NAME;
        db.execSQL(SQL_DELETE_ENTRIES);
        this.createTable(db);
    }

    public void createTable (SQLiteDatabase db){
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + GamePunctuationEntry.TABLE_NAME + " (" +
                        GamePunctuationEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        GamePunctuationEntry.COLUMN_NAME_PLAYERNAME + " TEXT," +
                        GamePunctuationEntry.COLUMN_NAME_DATETIME + " TEXT," +
                        GamePunctuationEntry.COLUMN_NAME_MISSINGPIECES + " INTEGER)";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

}