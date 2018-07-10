package jesus.net.ejemplosqlitekotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder

class DBManager {

    // Atributos de la BD
    val dbNombre = "MisNotas"
    val dbTabla = "Notas"
    val columnaID = "ID"
    val columnaTitulo = "Titulo"
    val columnaDescripcion = "Descripcion"
    val dbVersion = 1

    // CREATE TABLE IF NOT EXISTS + Notas + (ID INTEGER PRIMARY KEY AUTOINCREMENT, Titulo TEXT NOT NULL, Descripcion TEXT NOT NULL)
    val sqlCrearTabla = "CREATE TABLE IF NOT EXISTS " + dbTabla + " (" + columnaID + " INTEGER PRIMARY KEY AUTOINCREMENT," + columnaTitulo + " TEXT NOT NULL," + columnaDescripcion + " TEXT NOT NULL)"
    var sqlDB: SQLiteDatabase? = null

    constructor(contexto: Context) {
        val db = DBHelperNotas(contexto)
        sqlDB = db.writableDatabase
    }

    inner class DBHelperNotas(contexto: Context): SQLiteOpenHelper(contexto, dbNombre, null, dbVersion) {

        var contexto: Context? = null

        override fun onCreate(sqliteDatabase: SQLiteDatabase?) {
            sqliteDatabase!!.execSQL(sqlCrearTabla)
            print("BASE DE DATOS CREADA")
        }

        override fun onUpgrade(sqliteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
            sqliteDatabase!!.execSQL("DROP TABLE IF EXISTS" + dbTabla)
        }

    }

    fun insert(values: ContentValues) : Long {
        val ID = sqlDB!!.insert(dbTabla, "", values)
        return ID
    }

    // Query retorna un objeto de tipo cursor
    // Projection: Son las columnas
    // Selection: Es el cuerpo de la sentencia WHERE con las columnas que condicionamos
    // SelectionArgs: Es una lista de los valores que se usaran para reemplezar selection
    // OrderBy: Como queremos que se ordenen
    fun query(projection: Array<String>, selection: String, selectionArgs: Array<String>, orderBy: String): Cursor {
        val consulta = SQLiteQueryBuilder()
        consulta.tables = dbTabla
        val cursor = consulta.query(sqlDB, projection, selection, selectionArgs, null, null, orderBy)
        return cursor
    }

    fun borrar(selection: String, selectionArgs: Array<String>): Int {
        val contador = sqlDB!!.delete(dbTabla, selection, selectionArgs)
        return contador
    }

    fun actualizar(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        val contador = sqlDB!!.update(dbTabla, values, selection, selectionArgs)
        return contador
    }
}