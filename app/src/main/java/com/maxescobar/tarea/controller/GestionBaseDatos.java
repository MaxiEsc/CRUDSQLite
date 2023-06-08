package com.maxescobar.tarea.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import com.maxescobar.tarea.util.Constantes;


public class GestionBaseDatos extends SQLiteOpenHelper {

    public GestionBaseDatos(@Nullable Context context,
                        @Nullable String name,
                        @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase basedeDatos) {
        basedeDatos.execSQL(Constantes.CREAR_TABLA_USUARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Constantes.ELIMINAR_TABLA);

        db.execSQL(Constantes.CREAR_TABLA_USUARIOS);
    }
}
