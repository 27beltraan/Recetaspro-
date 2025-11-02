package com.example.miappbasica.data


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {

        private const val DATABASE_NAME = "mi_app.db"


        private const val DATABASE_VERSION = 1


        const val TABLE_USUARIOS = "usuarios"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
    }


    override fun onCreate(db: SQLiteDatabase) {
        // SQL: estructura de la tabla (campos y tipos de datos)
        val crearTablaUsuarios = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,   -- ID autoincremental
                $COLUMN_NOMBRE TEXT NOT NULL,                   -- Nombre del usuario
                $COLUMN_EMAIL TEXT UNIQUE NOT NULL,             -- Email único
                $COLUMN_PASSWORD TEXT NOT NULL                  -- Contraseña
            )
        """.trimIndent()

        // Ejecutamos el comando SQL que crea la tabla
        db.execSQL(crearTablaUsuarios)

        // 👥 Insertamos un usuario de prueba al crear la base
        val usuarioPrueba = ContentValues().apply {
            put(COLUMN_NOMBRE, "Usuario Prueba")
            put(COLUMN_EMAIL, "admin@duoc.cl")
            put(COLUMN_PASSWORD, "1234")
        }

        // Insertamos el usuario inicial para probar logins
        db.insert(TABLE_USUARIOS, null, usuarioPrueba)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Borramos la tabla antigua (si existía)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        // Volvemos a crear la tabla desde cero
        onCreate(db)
    }


    fun agregarUsuario(nombre: String, email: String, password: String): Long {
        // Abrimos la base en modo escritura
        val db = this.writableDatabase

        // Creamos un "formulario" con los datos a insertar
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, nombre)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
        }

        // insert() devuelve el ID del nuevo registro (o -1 si falla)
        val resultado = db.insert(TABLE_USUARIOS, null, values)

        // Cerramos la base
        db.close()

        // Retornamos el resultado
        return resultado
    }


    fun buscarUsuario(email: String, password: String): Boolean {
        val db = this.readableDatabase


        val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"

        // Ejecutamos la consulta
        val cursor = db.rawQuery(query, arrayOf(email, password))

        // Si el cursor tiene filas, el usuario existe
        val usuarioExiste = cursor.count > 0

        // Cerramos todo
        cursor.close()
        db.close()

        return usuarioExiste
    }


    fun emailExiste(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        val existe = cursor.count > 0

        cursor.close()
        db.close()

        return existe
    }
}
