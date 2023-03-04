package com.example.pruebabbdd;

import android.provider.BaseColumns;

public final class EmpleadoContract {

    private EmpleadoContract() {}

    public static class EmpleadoEntry implements BaseColumns {
        public static final String TABLE_NAME = "empleados";
        public static final String COLUMN_NOMBRE = "nombre";
        public static final String COLUMN_APELLIDO = "apellido";
        public static final String COLUMN_PUESTO = "puesto";
    }
}
