package com.example.pruebabbdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MiDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "empleados.db";

    public MiDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_EMPLEADOS_TABLE = "CREATE TABLE " + EmpleadoContract.EmpleadoEntry.TABLE_NAME + " ("
                + EmpleadoContract.EmpleadoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE + " TEXT NOT NULL, "
                + EmpleadoContract.EmpleadoEntry.COLUMN_APELLIDO + " TEXT NOT NULL, "
                + EmpleadoContract.EmpleadoEntry.COLUMN_PUESTO + " TEXT NOT NULL, "
                + EmpleadoContract.EmpleadoEntry.COLUMN_DNI + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_EMPLEADOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmpleadoContract.EmpleadoEntry.TABLE_NAME);
        onCreate(db);
    }
}
