package com.example.deberesa;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.Random;





public class Juego extends View {
    //todo, FONDO DE ESPACIO EN FORMATO GIF
    /*
    La clase Movie en Android se utiliza para reproducir animaciones en formato GIF.
    Un objeto de la clase Movie contiene información sobre un archivo GIF, incluyendo su
    tamaño, duración y una serie de imágenes que se reproducen en un bucle.
     */
    public Movie movie;
    /*
    La variable de tipo long se utiliza para almacenar números enteros de 64 bits en Java.
    Una variable de tipo long puede almacenar números más grandes que una variable de tipo int,
    y se utiliza a menudo para almacenar valores de tiempo (como milisegundos) o para almacenar
    valores de gran precisión que requieren más de 32 bits.
     */
    public Long  movieStart;


    public int ancho, alto;
    public boolean frutaSeleccionada=false;
    public int posX, posY, radio, posMonedaX, posMonedaY, numeroAleatorio, numeroSeleccionado,posMoneda2X,posMoneda2Y;
    //La clase GestureDetector se utiliza para detectar gestos en la pantalla, como toques y arrastres.
    private GestureDetector gestos;
    //La clase RectF se utiliza para representar un rectángulo con coordenadas de punto flotante.
    private RectF rectCesta;
    private RectF rectMoneda,rectMoneda2;
    private RectF rectFondo;

    //todo, gameOver
    private RectF rectFGameOver;
    private Integer puntuacion = 0;
    //todo, indicamos que el jugador comienze con 3 vidas
    public Integer numeroDeVidas = 3;
    private Random random = new Random();
    private Random random2 = new Random();


    //todo, ----------------------
    //todo, IMAGENES PARA EL JUEGO:
    //todo, Creamos la variable bitmapcesta y le asociamos la img_cesta que es una imagen png de una cesta
    Bitmap bitmapcesta = BitmapFactory.decodeResource(getResources(),R.drawable.nave);

    Bitmap asteroide = BitmapFactory.decodeResource(getResources(),R.drawable.asteroide);
    Bitmap bitmapfresa = BitmapFactory.decodeResource(getResources(),R.drawable.fresa);
    Bitmap bitmapkiwi = BitmapFactory.decodeResource(getResources(),R.drawable.kiwi);

    Bitmap bitmapcaramelo = BitmapFactory.decodeResource(getResources(),R.drawable.caramelo);
    Bitmap bitmapbomba = BitmapFactory.decodeResource(getResources(),R.drawable.bomba);
    Bitmap bitmapGameOver = BitmapFactory.decodeResource(getResources(),R.drawable.gameover2);
    Bitmap bitmapFondoImagen = BitmapFactory.decodeResource(getResources(),R.drawable.fondo);

    //todo, MUSICA
    MediaPlayer bandaSonora;
    MediaPlayer sonidoFrutaRecogida;
    MediaPlayer sonidoDeBomba;
    MediaPlayer sonidoError;

//La clase Juego tiene tres constructores diferentes, que se utilizan para inicializar la vista de diferentes maneras.

    // La clase Juego extiende (es una subclase de) la clase View
    //El método Juego es un constructor de la clase Juego
    //Un constructor es un método especial que se utiliza para crear
    // y inicializar una nueva instancia de una clase. En este caso, el constructor de la clase Juego
    // toma un argumento de tipo Context.

    /*

    Explicación del profe de para que se pasa el parametro context:

    El ejemplo que me ayuda a entenderlo es cuando lanzo procesos en paralelo y trabajo con dos actividades.
    Imagina que la actividad1 lanza un threat y este tiene que pintar algo en la actividad1. Si no le pasas el context no sabrá en qué actividad tiene que pintarlo.
    Espero que te sirva para entenderlo.
    Saludos y buenas fiestas
     */
    public Juego(Context context) {
        super(context);
        //todo, vinculamos el objeto mediaPlayer con nuestro archivo mp3
        //todo, ubicado en la carpeta raw para reproducir nuestra banda sonora
        bandaSonora = MediaPlayer.create(context,R.raw.bandasonora);
        sonidoFrutaRecogida = MediaPlayer.create(getContext(),R.raw.moneda);
        sonidoDeBomba = MediaPlayer.create(getContext(),R.raw.bomba);
        sonidoError = MediaPlayer.create(getContext(),R.raw.error);

        bandaSonora.start();
        //todo, establecemos que cuando la canción se complete se reproduzca de nuevo
        //todo, haciendo asi un loop.
        bandaSonora.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                bandaSonora.start();
            }
        });

        /*
        El método setFocusable(boolean) se utiliza para establecer si una vista es capaz de obtener
        el foco o no. El parámetro boolean especifica si la vista debe ser capaz de obtener el foco
        (si es true) o no (si es false).

        Cuando una vista tiene el foco, significa que está recibiendo eventos de entrada, como clics
        del mouse o toques en la pantalla. Esto es útil cuando se quiere que una vista participe en
        la interacción del usuario, como un botón o un campo de texto.

        Por otro lado, una vista que no tiene el foco no recibirá eventos de entrada, por lo que
         no podrá ser clickeada o tocada por el usuario. Esto es útil cuando se quiere que una vista sea solo de fondo o decorativa, como una imagen de fondo.
         */
        //setFocusable(true);
        /*
        InputStream es una clase que se utiliza para leer datos de una fuente de entrada, en este caso,
         el archivo GIF que deseas usar como fondo.
        context.getResources().openRawResource(R.drawable.yourgif); se utiliza para obtener una conexión de entrada
        a un recurso específico en tu proyecto, en este caso el archivo GIF.
         */
      //  @SuppressLint("ResourceType") InputStream is = context.getResources().openRawResource(R.drawable.espacio2);
        /*
        La línea movie = Movie.decodeStream(is); se utiliza para crear un objeto Movie a partir de
        un archivo de animación en formato GIF.

        El método decodeStream(InputStream) es un método estático de la clase Movie que recibe
        como parámetro un InputStream para el archivo GIF y devuelve un objeto Movie que contiene información sobre la animación, como su duración y tamaño, y una serie de imágenes que se reproducen en un bucle.

        En este caso, la variable 'is' es un InputStream que se obtiene a partir de un recurso de
        la aplicación con el contexto actual.

        En resumen, esta línea se utiliza para crear un objeto Movie a partir de un archivo gif y
        poder utilizarlo para reproducir la animación en una vista o actividad.
         */
     //   movie = Movie.decodeStream(is);
        /*
        La línea movieStart = Long.valueOf(0); se utiliza para inicializar una variable de tipo
        long con el valor 0.

        La clase Long es una clase wrapper de Java que proporciona una forma de trabajar con
        variables de tipo long. El método valueOf(long) es un método estático que recibe como
        parámetro un valor de tipo long y devuelve un objeto Long que envuelve ese valor.

        En este caso, se está inicializando una variable movieStart con el valor 0 para luego
        utilizarla para registrar el momento en que se inició la reproducción del gif. Esta variable
        se usará para calcular el tiempo transcurrido desde que se inició la reproducción del gif
        y determinar en qué frame se encuentra actualmente.

        En resumen, la línea movieStart = Long.valueOf(0); se utiliza para inicializar una variable
        de tipo long con el valor 0, y se utilizará para registrar el momento en que se inició la
        reproducción del gif y para calcular el tiempo transcurrido y determinar en qué frame
        se encuentra actualmente.
         */
      //  movieStart = Long.valueOf(0);

    }


    //La clase AttributeSet representa un conjunto de atributos que se aplican a una vista o layout.
    // Los atributos son propiedades que se pueden establecer en una vista o layout para controlar
    // su apariencia o comportamiento. Por ejemplo, un atributo podría controlar el color de fondo
    // de un botón o el tamaño de letra de un texto.

    public Juego(Context context, AttributeSet attrs) {
        super(context, attrs);

//todo, lo mismo que arriba con la musica pero en el otro constructor.
        bandaSonora = MediaPlayer.create(context,R.raw.bandasonora);
        sonidoFrutaRecogida = MediaPlayer.create(getContext(),R.raw.moneda);
        sonidoDeBomba = MediaPlayer.create(getContext(),R.raw.bomba);
        sonidoError = MediaPlayer.create(getContext(),R.raw.error);

        bandaSonora.start();
        //mantiene el loop de soundtrack
        bandaSonora.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                bandaSonora.start();
            }
        });
    }

    //El método onTouchEvent() es un método que se ejecuta cuando se produce un evento de toque en
    // la vista. En este método se controla el movimiento de la cesta en la pantalla según la posición
    // del dedo del usuario y se redibuja la vista utilizando el método invalidate().
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // you may need the x/y location
        switch (event.getAction()) {
            //Elimino el movimiento vertical poniendo el case y haciendo un break;
            case MotionEvent.ACTION_DOWN:
                //todo, Si cuando salga por pantalla el mensaje de gameOver el usuario pulsa en algun
                //todo, lugar el programa vuelve a la interfaz principal a veces me da error, no se porque

                if(numeroDeVidas <= 0){
                    //todo, Volvemos a la activity Main activity
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    getContext().startActivity(intent);
                    bandaSonora.stop();
                    bandaSonora.release();
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                //Configuramos el movimiento tanto por X como por Y
                posY = (int) event.getY();
                posX = (int) event.getX();
                //Le damos un radio 50
                radio = 100;
                // invalidate llama al onDraw y vuelve a pintar la bola
                this.invalidate();
                break;

        }
        return true;
    }

    // El mismo constructor que el anterior pero se le pasa un entero mas que parece definir el estilo
    public Juego(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //El método onDraw() es un método que se ejecuta cuando la vista necesita redibujarse
    // y se encarga de dibujar los elementos gráficos en la pantalla En este método se crean y
    //todo,se definen los colores de varios objetos Paint de Android, que se utilizan para dibujar elementos
    // en el canvas de la vista. También se dibuja un rectángulo de fondo y se calcula la intersección entre
    // la cesta y la moneda para determinar si se ha producido una colisión y, en ese caso, se aumenta la puntuación.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Definimos los objetos a pintar
        Paint fondo = new Paint();
        Paint cesta = new Paint();
        Paint moneda = new Paint();
        Paint moneda2 = new Paint();
        Paint puntos = new Paint();
        //todo, NI
        Paint vidas = new Paint();
        Paint gameOverFondo = new Paint();
        Paint gameOverLogo = new Paint();

        //Definimos los colores de los objetos a pintar
        fondo.setColor(Color.BLACK);
        fondo.setStyle(Paint.Style.FILL_AND_STROKE);
        cesta.setColor(Color.YELLOW);
        cesta.setStyle(Paint.Style.FILL_AND_STROKE);
        //  moneda.setColor(Color.RED);
        //  moneda.setStyle(Paint.Style.FILL_AND_STROKE);

        puntos.setTextAlign(Paint.Align.RIGHT);
        puntos.setTextSize(100);
        puntos.setColor(Color.WHITE);

        //todo, Creamos un PAINT que mostrará el numero de vidas que tiene el jugador
        //todo, si llega a 0 GameOver
        vidas.setTextAlign(Paint.Align.LEFT);
        vidas.setTextSize(100);
        vidas.setColor(Color.RED);

        //todo, GAME OVER
        gameOverFondo.setColor(Color.BLACK);
        gameOverFondo.setStyle(Paint.Style.FILL_AND_STROKE);

        gameOverLogo.setColor(Color.WHITE);
        gameOverLogo.setStyle(Paint.Style.FILL_AND_STROKE);

        //todo, Cuando el numero de vidas sea menor a 0 osea
        // cuando el jugador pierda todas las vidas lanza un mensaje de gameOverFondo.
        if (numeroDeVidas <= 0) {
            //todo, pintamos el fondo del gameOver
            canvas.drawRect(new Rect(0, 0, (ancho), (alto)), gameOverFondo);
            //todo, pintamos el logo del gameOver

            //Establecemos el ancho del logo del Bitmap GameOver
            int anchoBtmpGameOver = bitmapGameOver.getWidth() - 512;
            int altoBtmpGameOver = bitmapGameOver.getHeight() - 512;

            //Obtenemos la altura y lo ancho de la pantalla de nuestro dispositivo.
            int alturaDePantalla = getResources().getDisplayMetrics().widthPixels;
            int anchoDePantalla = getResources().getDisplayMetrics().heightPixels;

            //Establecemos las coordenadas de la izquierda y la parte superior
            //para colocar nuestroRecFGameOver centrado en la pantalla
            int izquierda = (alturaDePantalla - anchoBtmpGameOver) / 2;
            int parteSuperior = (anchoDePantalla - altoBtmpGameOver) / 3;

            //Creamos un RectF que nos servirá para colocar nuestro Logo de GameOver
            rectFGameOver = new RectF(izquierda, parteSuperior, izquierda + anchoBtmpGameOver, parteSuperior + altoBtmpGameOver);
            canvas.drawBitmap(bitmapGameOver, null, rectFGameOver, gameOverLogo);


        } else {
     //todo, Este else lo que hace es que solo pinta todos los objetos del juego (las frutas, la cesta, los movimientos..)
     //todo, en caso de que el jugador no haya perdido todas sus vidas.

        //todo, Pintamos el fondo de forma personalizada.
        rectFondo = new RectF(0, 0, 0 + ancho, 0 + alto);
        canvas.drawBitmap(bitmapFondoImagen,null,rectFondo,fondo);

        // Pinto la pelota con la posición que se va estableciendo con onTouchEvent
        rectCesta = new RectF((posX - radio), (posY - radio), (posX + radio), (posY + radio));

        //todo, establecemos que el bitmapcesta sea la imagen que nosotros utilizaremos para
        //todo, representar a la cesta
        canvas.drawBitmap(bitmapcesta, null, rectCesta, cesta);



        //todo, Modificamos el if para que cuando la posición de la moneda sea menor que 0
        //todo, osea que la parte superior, la posición de esta vuelva a ponerse en la parte baja
        //todo, de la pantalla indicandole que su posición es igual a lo alto del juego.
        if (posMonedaY > alto) {
            frutaSeleccionada = false;
            posMonedaY = 0;
            posMonedaX = random.nextInt(ancho);

            //todo, quitamos una vida cuando no consigamos coger una fruta, no se aplica a bombas ni chuches
            if (numeroSeleccionado == 1 || numeroSeleccionado == 2 || numeroSeleccionado == 3) {
                numeroDeVidas -= 1;
            }
        }
        //segunda moneda
        if (posMoneda2Y > alto) {
                frutaSeleccionada = false;
                posMoneda2Y = 0;
                posMoneda2X = random2.nextInt(ancho);
            }

        //El canvas es lo que se utiliza para pintar, el cual con un . llama al metodo draw el cual
        //redibuja los objetos que creamos con paint en la posición en la cual nosotros indiquemos.

        rectMoneda = new RectF((posMonedaX - radio), (posMonedaY - radio), (posMonedaX + radio), (posMonedaY + radio));
        rectMoneda2 = new RectF((posMoneda2X - radio), (posMoneda2Y - radio), (posMoneda2X + radio), (posMoneda2Y + radio));


        //todo, si aun no se ha seleccionado una fruta, se selecciona una mediante el metodo seleccionaFrutaAleatoria
        //todo, el cual retorna un bitmap con la imagen de una fruta. Cuando se selecciona una fruta aleatoria el boolean
        //todo, frutaSeleccionada se pone en true y solo se muestra la fruta selecionada hasta que la fruta
        //todo, llegue al final de su recorrido o sea recogida por la cesta

        canvas.drawBitmap(asteroide,null,rectMoneda,moneda);
        canvas.drawBitmap(asteroide,null,rectMoneda2,moneda2);
        //ANTES PARA ESCOGER ALEATORIAMENTE:
        /*
        if (frutaSeleccionada == false) {
            //todo, generamos un numero aleatorio del 1 al 5
            numeroAleatorio = (int) Math.floor(Math.random() * 5) + 1;
            numeroSeleccionado = numeroAleatorio;
            frutaSeleccionada = true;
            canvas.drawBitmap(seleccionaFrutaAleatoria(numeroAleatorio), null, rectMoneda, moneda);
        } else {
            //antes para elegir aleatoria mente un objeto
            canvas.drawBitmap(seleccionaFrutaAleatoria(numeroSeleccionado), null, rectMoneda, moneda);
        }
         */


        // Calculo intersección
        // Simplemente lo que hace es decir que si la cesta choca con la moneda
        // entonces suma una puntuación y la moneda vuelve a pintarse en la parte de arriba.
        // ya que vuelve a establecerese su posición.
        if (RectF.intersects(rectCesta, rectMoneda)) {
            //todo, si recogemos una fruta con la cesta se selecciona otro bitmap de fruta distinto
            //todo, de manera aleatoria

            frutaSeleccionada = false;

            //todo, Segun que fruta haya escogido se hace pone una u otra puntuación
            switch (numeroSeleccionado) {
                case 1: {
                    puntuacion += 1;
                    //todo,ni
                    sonidoFrutaRecogida.start();
                    break;
                }//platano 1 punto
                case 2: {
                    puntuacion += 2;
                    sonidoFrutaRecogida.start();
                    break;
                }//fresa 2 puntos
                case 3: {
                    puntuacion += 5;
                    sonidoFrutaRecogida.start();
                    break;
                }//kiwi 5 puntos
                case 4: {
                    numeroDeVidas -= 3;
                    sonidoDeBomba.start();
                    break;
                }//bomba quita 3 vidas
                case 5: {
                    numeroDeVidas -= 1;
                    sonidoError.start();
                    break;
                }//caramelo quita 1 vida
            }
            //todo, estalecemos que cuando la moneda choque con la cesta, la moneda se ponga en la
            //todo, parte inferior de la pantalla, con el timer se programa que suba hacia arriba
            posMonedaY = alto;
            posMonedaX = random.nextInt(ancho);
        } else if (RectF.intersects(rectCesta, rectMoneda2)){
            posMoneda2Y = alto;
            posMoneda2X = random2.nextInt(ancho);
        }
        //todo, pintamos por pantalla la puntuación y el numero de vidas del jugador
            canvas.drawText(puntuacion.toString(), 150, 150, puntos);
            canvas.drawText(numeroDeVidas.toString(), 900, 150, vidas);

    }
    }

    public Bitmap seleccionaFrutaAleatoria(int numeroAleatorio){
            switch (numeroAleatorio){
                case 1:{return asteroide;} //1 Punto
                case 2:{return bitmapfresa;} //2 Puntos
                case 3:{return bitmapkiwi;} //5 puntos
                case 4:{return bitmapbomba;} //Quita 3 vidas
                case 5:{return bitmapcaramelo;} // Quita 1 vida
            }
        return null;
    }
}
