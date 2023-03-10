package com.example.pruebabbdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Interfaz extends AppCompatActivity {

    private EditText nombreEditText;
    private EditText apellidoEditText;
    private EditText puestoEditText;
    private TableLayout empleadosTableLayout;
    private Button agregarEmpleadoButton;
    private Button actualizarEmpleadoButton;
    private Button eliminarEmpleadoButton;
    private EditText dniEditText;

    private SQLiteDatabase db;
    private MiDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interfaz);

        getSupportActionBar().setTitle("Interfaz de acciones");
        dniEditText = findViewById(R.id.dniEditText);
        nombreEditText = findViewById(R.id.nombreEditText);
        apellidoEditText = findViewById(R.id.apellidoEditText);
        puestoEditText = findViewById(R.id.puestoEditText);
        empleadosTableLayout = findViewById(R.id.tableLayout);
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
        String dni = dniEditText.getText().toString();
        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String puesto = puestoEditText.getText().toString();

        if(dni.equals("")||nombre.equals("")||apellido.equals("")||puesto.equals("")){
            Toast.makeText(this, "Todos los campos deben ser cumplimentados para poder añadir", Toast.LENGTH_LONG).show();
            return;
        }else if(!dni.matches("\\d{8}[A-Z]")){
            Toast.makeText(this, "El formato del DNI es incorrecto", Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_DNI, dni);
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

        resetData();
    }

    private void actualizarEmpleado() {
        String dni = dniEditText.getText().toString();
        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String puesto = puestoEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_DNI, dni);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE, nombre);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_APELLIDO, apellido);
        values.put(EmpleadoContract.EmpleadoEntry.COLUMN_PUESTO, puesto);

        String selection = EmpleadoContract.EmpleadoEntry.COLUMN_DNI + " LIKE ?";
        String[] selectionArgs = { dni };

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

        resetData();
    }

    private void eliminarEmpleado() {
        String dni = dniEditText.getText().toString();

        String selection = EmpleadoContract.EmpleadoEntry.COLUMN_DNI + " LIKE ?";
        String[] selectionArgs = { dni };

        int deletedRows = db.delete(EmpleadoContract.EmpleadoEntry.TABLE_NAME, selection, selectionArgs);

        if (deletedRows == 0) {
            Toast.makeText(this, "Error al eliminar empleado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Empleado eliminado", Toast.LENGTH_SHORT).show();
            mostrarEmpleados();
        }

        resetData();
    }

    private void mostrarEmpleados() { // Obtener los datos de los empleados de la base de datos
        Cursor cursor = db.query(
                EmpleadoContract.EmpleadoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Obtener el TableLayout del diseño
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Eliminar todas las filas existentes en la tabla
        tableLayout.removeAllViews();

        // Crear una nueva fila de tabla para cada empleado
        while (cursor.moveToNext()) {
            String dni = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_DNI));
            String nombre = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_NOMBRE));
            String apellido = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_APELLIDO));
            String puesto = cursor.getString(cursor.getColumnIndex(EmpleadoContract.EmpleadoEntry.COLUMN_PUESTO));

            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            // Agregar las celdas correspondientes a la fila
            TextView dniTextView = new TextView(this);
            dniTextView.setText(dni);
            dniTextView.setPadding(5, 5, 5, 5);
            tableRow.addView(dniTextView);

            TextView nombreTextView = new TextView(this);
            nombreTextView.setText(nombre);
            nombreTextView.setPadding(5, 5, 5, 5);
            tableRow.addView(nombreTextView);

            TextView apellidoTextView = new TextView(this);
            apellidoTextView.setText(apellido);
            apellidoTextView.setPadding(5, 5, 5, 5);
            tableRow.addView(apellidoTextView);

            TextView puestoTextView = new TextView(this);
            puestoTextView.setText(puesto);
            puestoTextView.setPadding(5, 5, 5, 5);
            tableRow.addView(puestoTextView);

            // Agregar la fila a la tabla
            tableLayout.addView(tableRow);
        }

        cursor.close();
    }

    private void resetData() {
        dniEditText.setText("");
        nombreEditText.setText("");
        apellidoEditText.setText("");
        puestoEditText.setText("");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}