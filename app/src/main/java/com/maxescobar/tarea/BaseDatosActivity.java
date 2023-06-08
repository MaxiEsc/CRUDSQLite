package com.maxescobar.tarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.maxescobar.tarea.controller.GestionBaseDatos;
import com.maxescobar.tarea.model.Usuario;
import com.maxescobar.tarea.util.Constantes;

import java.util.ArrayList;

public class BaseDatosActivity extends AppCompatActivity {

    //Nuestra lista de Usuarios
    private ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
    //Con esto tipo de dato se adaptaria a la clase creada de parte de BaseAdapter
    ArrayAdapter<Usuario> arrayAdapter;
    //Nuestro adaptador creado
    ListView listaViewUsuarios;
    Usuario itemSeleccionado;
    private EditText etNombre, etCodigo;

    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_datos);

        etCodigo = (EditText) findViewById(R.id.etCodigo);
        etNombre = (EditText) findViewById(R.id.etNombre);

        btnVolver = (Button) findViewById(R.id.btnVolver2);

        listaViewUsuarios = (ListView) findViewById(R.id.listViewSalida);

        try {
            listaViewUsuarios.setOnItemClickListener((parent, view, position, id) -> {
                itemSeleccionado = (Usuario) parent.getItemAtPosition(position);
                etCodigo.setText(itemSeleccionado.getCodigo());
                etNombre.setText(itemSeleccionado.getNombre());

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        cargarUsuarios();
    }

    private void cargarUsuarios() {


        //Con es ta linea utilizamos la base de datos SQLite mediante la clase que creamos y que extendimos de SQLiteOpenHelper
        GestionBaseDatos admin = new GestionBaseDatos(this, "gestion", null, 1);
        //Crea un objeto de tipoSQLiteDatabase para que este se escribible
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

        Cursor item = BaseDeDatos.rawQuery(Constantes.LISTAR_USUARIOS,null);

        try {
            while (item.moveToNext()){
                listaUsuarios.add(new Usuario(item.getString(1),item.getString(0)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        arrayAdapter = new ArrayAdapter<Usuario>(
                BaseDatosActivity.this,
                android.R.layout.simple_list_item_1
                , listaUsuarios);

        listaViewUsuarios.setAdapter(arrayAdapter);
    }

    public void BtnListar(View vista){

        listaUsuarios.clear();
        listaViewUsuarios.clearTextFilter();
        cargarUsuarios();
        Toast.makeText(this, "Actulizando Lista", Toast.LENGTH_SHORT).show();


    }


    //Metodo para Ingresar un usuario
    public void BtnInsertar(View vista) {
        //Con es ta linea utilizamos la base de datos SQLite mediante la clase que creamos y que extendimos de SQLiteOpenHelper
        GestionBaseDatos admin = new GestionBaseDatos(this, "gestion", null, 1);
        //Crea un objeto de tipoSQLiteDatabase para que este se escribible
        SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();
        //Lo de siempre
        String codigo = etCodigo.getText().toString();
        String nombre = etNombre.getText().toString();

        if (!codigo.isEmpty() && !nombre.isEmpty()) {
            //Permite crear una variable que permita la carga de datos en la DB
            ContentValues registro = new ContentValues();
            //Registramos el codigo, descripcion y precio
            registro.put("codigo", codigo);
            registro.put("nombre", nombre);

            //Guardamos los regiistros en la tabla articulos
            BaseDeDatos.insert("Usuarios", null, registro);

            //Cerrar la base de datos
            BaseDeDatos.close();
            //Limpiamos los campos
            etNombre.setText("");
            etCodigo.setText("");

            //Avisar al usuario de que todo se realizo correctamente
            Toast.makeText(this, "Datos registrados correctamente", Toast.LENGTH_SHORT).show();
        } else {
            //Avisar al usuario de este caso
            Toast.makeText(this, "Debes completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para Actualizar el Usuario
    public void BtnActualizar(View vista) {
        //Como arriba creamos un objeto de tipo AdminiSQLite que hereda de SQLiteOpenHelper este metodo lo usamos previamente darle caracteristica a SQLiteDatabase
        GestionBaseDatos admin = new GestionBaseDatos(this, "gestion", null, 1);
        //Idem como en las vez pasada, no mas podemos agregar que nos ortorgara la caractirsticas para abrir la base de datos como lectura o escritura
        SQLiteDatabase basededatos = admin.getWritableDatabase();
        //Lo de siempre x2... porque los 3 pues para que pueda editarlos si lo desea.
        String codigo = etCodigo.getText().toString();
        String nombre = etNombre.getText().toString();
        //Condicion para que se edite todo el campo
        if (!codigo.isEmpty() && !nombre.isEmpty()) {
            //Como en el insert nos respaldamos del objeto ContentValues para que el usuario cree una sentencia de basededatos
            ContentValues registro = new ContentValues();
            //Registramos el codigo, descripcion y precio una vez mas
            registro.put("nombre", nombre);
            registro.put("codigo", codigo);

            try {

                //Valor devolucion de las cantidad de registros modificados
                int cantidad = basededatos.update("Usuarios", registro, "codigo=" + codigo, null);

                //condicional para controla si el articulo se modifica o no
                if (cantidad == 1) {
                    //Pues le avisamos al cliente que se ha modificado el Usuario o elemento
                    Toast.makeText(this, "El Usuario se ha modificado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    //Si el Usuario no se encuetra o se a tipeado un codigo erroneo
                    Toast.makeText(this, "El Usuario a modififcar no existe", Toast.LENGTH_SHORT).show();
                }

            }catch(Exception e){
                e.printStackTrace();
                Toast.makeText(this, "No se puede editar el Codigo, SOLO NOMBRE!!", Toast.LENGTH_SHORT).show();
            }

        } else {
            //si no esta completo los campos... a llenar!!!
            Toast.makeText(this, "Debes completar los campos para modificar", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo Para Borrar el Usuario
    public void BtnBorrar(View vista) {
        //Como arriba creamos un objeto de tipo AdminiSQLite que hereda de SQLiteOpenHelper que implementa metodos de base dedatos
        GestionBaseDatos admin = new GestionBaseDatos(BaseDatosActivity.this, "gestion", null, 1);
        //Aqui implementamos un objeto BasedeDatos(Objeto que actua como una base de datos simple) con el que implementaremos los metodos de la sqlite
        SQLiteDatabase basededatos = admin.getWritableDatabase();

        //Extraemos el codigo que necesitamos
        String codigo = etCodigo.getText().toString();
        //estructura condicional para controlar si el usuario ingresa codigo o no
        if (!codigo.isEmpty()) {

            try {

                //En este caso este metodo devuelve un entero representando la cantidad de elementos borrados como JDBC.
                int cantidad = basededatos.delete("Usuarios", "codigo=" + codigo, null);
                //Cerramos la base de datos
                basededatos.close();
                //limpiamos os campos
                etNombre.setText("");
                etCodigo.setText("");
                //Estructura condicional por si se ha elimnado el articulo o no
                if (cantidad == 1) {
                    //Mensaje para el cliente por si el Usuario se elimino como corresponde
                    Toast.makeText(this, "El Usuario ha sido eliminado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    //Mensaje para el cliente por si el Usuario no se ha eliminado por alguna razon
                    Toast.makeText(this, "El Usuario no ha podido ser eliminado por que no existe", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            //si no esta completo el codigo... pues que lo coloque
            Toast.makeText(this, "Debes escribir un codigo que desee eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para Volver al menu
    public void BtnVolver(View vista){
        Intent volver = new Intent(BaseDatosActivity.this, MainActivity.class);

        startActivity(volver);
    }
}