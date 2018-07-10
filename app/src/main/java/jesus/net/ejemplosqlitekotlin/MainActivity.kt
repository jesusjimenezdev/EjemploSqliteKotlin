package jesus.net.ejemplosqlitekotlin

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.molde_notas.view.*

class MainActivity(var adapter: NotasAdapter? = null) : AppCompatActivity() {

    var listaDeNotas = ArrayList<Nota>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ponemos el % para que cargue todas las notas
        cargarQuery("%")

        /*listaDeNotas.add(Nota(1, "Nota 1", "Descripcion de la nota"))
        listaDeNotas.add(Nota(2, "Nota 2", "Descripcion de la nota"))
        listaDeNotas.add(Nota(3, "Nota 3", "Descripcion de la nota"))*/

        /*adapter = NotasAdapter(this, listaDeNotas)
        lista.adapter = adapter*/

        fab.setOnClickListener {
            var intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    // onResume() se ejecuta cuando pasamos de una actividad a otra
    override fun onResume() {
        super.onResume()
        cargarQuery("%")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val buscar = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        buscar.setSearchableInfo(manejador.getSearchableInfo(componentName))

        buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(texto: String?): Boolean {
                Toast.makeText(applicationContext, texto, Toast.LENGTH_SHORT).show()
                cargarQuery("%" + texto + "%")
                return false
            }

            override fun onQueryTextChange(texto: String?): Boolean {
                cargarQuery("%" + texto + "%")
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    // SELECT ID, TITULO, DESCRIPCION FROM NOTAS WHERE (TITULO LIKE ?) ORDER BY TITULO
    fun cargarQuery(titulo: String) {
        var baseDatos = DBManager(this)
        val columnas = arrayOf("ID", "Titulo", "Descripcion")
        val selectionArgs = arrayOf(titulo)
        val cursor = baseDatos.query(columnas, "Titulo LIKE ?", selectionArgs, "Titulo")
        listaDeNotas.clear()
        if(cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val titulo = cursor.getString(cursor.getColumnIndex("Titulo"))
                val descripcion = cursor.getString(cursor.getColumnIndex("Descripcion"))
                listaDeNotas.add(Nota(ID, titulo, descripcion))
            }while (cursor.moveToNext())
        }

        adapter = NotasAdapter(this, listaDeNotas)
        lista.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.menuAgregar -> {
                var intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class NotasAdapter(contexto: Context, var listaDeNotas: ArrayList<Nota>): BaseAdapter() {

        var contexto: Context = contexto

        override fun getItem(index: Int): Any {
            return listaDeNotas[index]
        }

        override fun getItemId(index: Int): Long {
            return index.toLong()
        }

        override fun getCount(): Int {
            return listaDeNotas.count()
        }

        override fun getView(index: Int, view: View?, viewGroup: ViewGroup?): View {
            var convertView: View? = view
            if(convertView == null) {
                convertView = View.inflate(contexto, R.layout.molde_notas, null)
            }

            val nota = listaDeNotas[index]
            //val inflater = contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val miVista = convertView!! //inflater.inflate(R.layout.molde_notas, null)
            miVista.textViewTitulo.text = nota.titulo
            miVista.textViewContenido.text = nota.descripcion

            miVista.imageViewBorrar.setOnClickListener {
                val dbManager = DBManager(this.contexto!!)
                val selectionArgs = arrayOf(nota.notaID.toString())
                dbManager.borrar("ID=?", selectionArgs)
                cargarQuery("%")
            }

            miVista.imageViewEditar.setOnClickListener {
                val intent = Intent(this@MainActivity, AddActivity::class.java)
                intent.putExtra("ID", nota.notaID)
                intent.putExtra("titulo", nota.titulo)
                intent.putExtra("descripcion", nota.descripcion)
                intent.putExtra("control", 0)
                startActivity(intent)
            }

            return miVista
        }
    }
}
