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

        listaDeNotas.add(Nota(1, "Nota 1", "Descripcion de la nota"))
        listaDeNotas.add(Nota(2, "Nota 2", "Descripcion de la nota"))
        listaDeNotas.add(Nota(3, "Nota 3", "Descripcion de la nota"))

        adapter = NotasAdapter(this, listaDeNotas)
        lista.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val buscar = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val manejador = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        buscar.setSearchableInfo(manejador.getSearchableInfo(componentName))
        buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(texto: String?): Boolean {
                Toast.makeText(applicationContext, texto, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(texto: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
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

    class NotasAdapter(contexto: Context, var listaDeNotas: ArrayList<Nota>): BaseAdapter() {

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
            val nota = listaDeNotas[index]
            val inflater = contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val miVista = inflater.inflate(R.layout.molde_notas, null)
            miVista.textViewTitulo.text = nota.titulo
            miVista.textViewContenido.text = nota.descripcion
            return miVista
        }
    }
}
