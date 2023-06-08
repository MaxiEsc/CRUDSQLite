package com.maxescobar.tarea.util;

public class Constantes {

    public final static String ELIMINAR_TABLA = "DROP TABLE IF EXISTS Usuarios; ";
    public final static String CREAR_TABLA_USUARIOS = "CREATE TABLE Usuarios(codigo int PRIMARY KEY, nombre text)";
    public final static String LISTAR_USUARIOS = "SELECT * FROM Usuarios";

}
