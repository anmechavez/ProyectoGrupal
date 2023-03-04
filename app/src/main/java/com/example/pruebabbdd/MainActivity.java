package com.example.pruebabbdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText apellidoEditText;
    private EditText puestoEditText;
    private TextView empleadosTextView;
    private Button agregarEmpleadoButton;
    private Button actualizarEmpleadoButton;
    private Button eliminarEmpleadoButton;

    private SQLiteDatabase db;
    private MiDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreEditText = findViewById(R.id.nombreEditText);
        apellidoEditText = findViewById(R.id.apellidoEditText);
        puestoEditText = findViewById(R.id.puestoEditText);
        empleadosTextView = findViewById(R.id.empleadosTextView);
        agregarEmpleadoButton = findViewById(R.id.agregarEmpleadoButton);
        actualizarEmpleadoButton = findViewById(R.id.actualizarEmpleadoButton);
        eliminarEmpleadoButton = findViewById(R.id.eliminarEmpleadoButton);

        dbHelper = new MiDBHelper(this);
        db = dbHelper.getWritableDatabase();

        agregarEmpleadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarEmpleado();
            }
        });

        actualizarEmpleadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEmpleado();
            }
        });

        eliminarEmpleadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarEmpleado();
            }
        });

        mostrarEmpleados();
    }

    private void agregarEmpleado() {
        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String puesto = puestoEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE, nombre);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_APELLIDO, apellido);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_PUESTO, puesto);

        long newRowId = db.insert(EmpleadoContract.EmpleadoEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error al agregar empleado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Empleado agregado con ID " + newRowId, Toast.LENGTH_SHORT).show();
            mostrarEmpleados();
        }
    }

    private void actualizarEmpleado() {
        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String puesto = puestoEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE, nombre);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_APELLIDO, apellido);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_PUESTO, puesto);

        String selection = EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE + " LIKE ?";
        String[] selectionArgs = { nombre };

        int count = db.update(
                EmpleadoContract.EmpleadoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count == 0) {
            Toast.makeText(this, "Error al actualizar empleado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Empleado actualizado", Toast.LENGTH_SHORT).show();
            mostrarEmpleados();
        }
    }

    private void eliminarEmpleado() {
        String nombre = nombreEditText.getText().toString();

        String selection = EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE + " LIKE ?";
        String[] selectionArgs = { nombre };

        int deletedRows = db.delete(EmpleadoContract.EmpleadoEntry.TABLE_NAME, selection, selectionArgs);

        if (deletedRows == 0) {
            Toast.makeText(this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
            mostrarEmpleados();
        }
    }

    private void mostrarEmpleados() {
        Cursor cursor = db.query(
                EmpleadoContract.EmpleadoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_APELLIDO));
            String puesto = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_PUESTO));
            builder.append(nombre).append(" ").append(apellido).append(", ").append(puesto).append("\n");
        }

        empleadosTextView.setText(builder.toString());
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
