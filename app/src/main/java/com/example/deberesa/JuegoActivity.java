package com.example.deberesa;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import java.util.Timer;
import java.util.TimerTask;


public class JuegoActivity extends AppCompatActivity {
    //Crea una instancia de la clase juego
    public Juego juego;
    //Mientras menor sea el numero de velocidad
    //mas rapido subira la fruta
    //Por defecto esta en velocidad 20 que es la dificultad Facil
    int velocidad = 20;

    //Crea un objeto de tipo handler  La clase Handler se utiliza para
    //programar tareas para ejecutarse en el hilo principal de la interfaz de usuario,
    //que es el hilo que se encarga de actualizar la interfaz de usuario.

    private Handler handler = new Handler();

    //El método onCreate() es un método que se ejecuta cuando se crea la actividad por primera vez.
    //En este método se establece la vista que se mostrará en la actividad utilizando el método
    //setContentView()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        velocidad = recibirDificultad();

        // Se asigna la vista personalizada "Juego" a la variable "juego"
        juego = (Juego) findViewById(R.id.Pantalla);

        //Se establece un listener de layout global para la vista "Juego".
        //El listener de layout global se ejecutará cuando se haya completado el dibujo del layout de la actividad.
        /*Se utiliza para calcular el ancho y alto de la vista "Juego" y para establecer algunas variables de instancia
        en la vista "Juego". Estas variables se utilizarán más adelante para dibujar y mover elementos en la vista.
        */
        ViewTreeObserver obs = juego.getViewTreeObserver();
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Sólo se puede averiguar el ancho y alto una vez ya se ha pintado el layout. Por eso se calcula en este listener
                juego.ancho = juego.getWidth();
                juego.alto = juego.getHeight();
                //todo, aqui se establece donde estará la posición de la cesta
                juego.posX = juego.ancho / 2;
                //todo, indicamos que nuestra cesta se establezca en la posición 50 osea en la
                //todo, parte superior de la pantalla
                juego.posY = juego.alto - 50;
                juego.radio = 100;
                //todo, Antes era (50), aqui se establece el punto en el que se pinta la moneda que iba cayendo ahora va hacia arriba
                //todo, para hacer que el recorrido vaya de abajo hacia arriba, establecemos que se pinte en lo alto del juego
                //todo, osea desde abajo del todo.
                juego.posMonedaY = 0;
                juego.posMoneda2Y = -100;
            }
        });

        /*
        Después del listener de layout global, se crea una instancia de la clase Timer y se programa una tarea para ejecutarse cada 20 milisegundos.
        La tarea consiste en actualizar la posición de una moneda en la vista "Juego" y volver a dibujar la vista.
        Esto se hace utilizando el método invalidate() de la vista "Juego", que forzará a la vista a redibujarse y a llamar a su método draw().
         */

//(*) El timer cambia la posición de la moneda cada 20 milisegundos, y con juego.invalidate() refresca la pantalla para llamar al metodo draw
//    donde se pinta la moneda.

        //Ejecutamos cada 20 milisegundos

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        //Cada 20 milisegundos segundos movemos la moneda 10 posiciones mas abajo
                        //todo, antes tenia += 10 que era lo que hacia que bajara, ahora le ponemos -= 10 que hará que suba hacia arriba
                        juego.posMonedaY += 10;
                        //La linea de abajo lo pongo para probar, antes no habia esa
                        //linea y el codigo de abajo era lo que estaba activo
                        juego.posMoneda2Y+=10;
                        //refreca la pantalla y llama al draw
                        juego.invalidate();
                    }
                });
            }
        }, 0, velocidad);

/*
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                juego.posMoneda2Y +=10;
                juego.invalidate();
            }
        },1000,velocidad);
 */

    }



    public int recibirDificultad(){
        Bundle extras = getIntent().getExtras();
        int velocidad = extras.getInt("dificultad");
        return velocidad;
    }

}
