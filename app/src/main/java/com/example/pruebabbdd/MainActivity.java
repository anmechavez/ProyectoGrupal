package com.example.pruebabbdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//todo, LAS CREDENCIALES DE ACCESO SON:
//Usuario: admin
//Password: root
public class MainActivity extends AppCompatActivity {
    private Button btToSecondActivity;
    private ImageView imglogo;
    private EditText etuser, etpass;
    private String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = "admin";
        pass = "root";
        etuser = findViewById(R.id.etuser);
        etpass = findViewById(R.id.etpass);
        imglogo = findViewById(R.id.imageView);
        imglogo.setImageResource(R.drawable.logoelephant);

        //todo, Botón que envia a la siguiente activity
        btToSecondActivity = findViewById(R.id.button);
        btToSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo, HAY ALGO EN ESTE IF QUE NO ESTA BIEN
                //todo, AUNQUE INTRODUZCAS BIEN EL USUARIO DA ERROR
                //todo, REVISAR EL ERROR.
                if(etuser.getText().toString().equals(user) && etpass.getText().toString().equals(pass)){
                    Intent intent = new Intent(MainActivity.this,Interfaz.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                }else if(etuser.getText().toString().equals("")||etpass.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "El campo usuario y contraseña no pueden estar incompletos", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
