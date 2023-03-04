package com.example.deberesa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int velocidad = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Creamos los botones de selección de dificultad
        Button facil = findViewById(R.id.facil);
        Button normal = findViewById(R.id.normal);
        Button dificil = findViewById(R.id.dificil);
        TextView dificultad = findViewById(R.id.dificultad);

        //todo, programamos la dificultad según el botón
        facil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                velocidad = 20;
                dificultad.setText("Dificuldad: Fácil");
            }
        });
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                velocidad = 10;
                dificultad.setText("Dificuldad: Normal");
            }
        });
        dificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                velocidad = 5;
                dificultad.setText("Dificuldad: Difícil");
            }
        });

    }

    public void empiezaElJuego(View view){
        //Esta clase nos servirá para abrir el activity del juego
        //Hemos puesto el Onclick listener seleccionando el botón desde el apartado diseño y escogiendo este metodo

        Intent intent = new Intent(this, JuegoActivity.class);
        intent.putExtra("dificultad",velocidad); //Con putExtra enviamos la velocidad
                                                        //a la cual queremos que vaya nuestro juego,
                                                        // a mayor o menor dificultad
        startActivity(intent); //Esto hace un start de la actividad que hemos marcado arriba en el intent
    }

}