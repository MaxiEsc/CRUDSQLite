package com.maxescobar.tarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Metodo para ir a los videos
    public void btnVideo(View vista){
        Intent videos = new Intent(MainActivity.this, MainVideo.class);

        startActivity(videos);
    }

    public void btnBaseDatos(View vista){
        Intent basedatos = new Intent(MainActivity.this, BaseDatosActivity.class);

        startActivity(basedatos);
    }
}