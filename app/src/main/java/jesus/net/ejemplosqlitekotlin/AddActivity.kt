package jesus.net.ejemplosqlitekotlin

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        try {
            val bundle: Bundle = intent.extras
            id = bundle.getInt("ID", 0)
            val control = bundle.getInt("control")
            if(id != 0 && control == 0) {
                editTextTitulo.setText(bundle.getString("titulo"))
                editTextContenido.setText(bundle.getString("descripcion"))
                btnAgregar.setText("ACTUALIZAR")
            }
        }catch (e: Exception){}


        btnAgregar.setOnClickListener {
            val baseDatos = DBManager(this)
            val values = ContentValues()
            values.put("Titulo", editTextTitulo.text.toString())
            values.put("Descripcion", editTextContenido.text.toString())
            if(id == 0) {
                val ID = baseDatos.insert(values)
                if (ID > 0) {
                    Toast.makeText(this, "Nota agregada correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Nota no agregada correctamente", Toast.LENGTH_LONG).show()
                }
            } else {
                val selectionArgs = arrayOf(id.toString())
                val ID = baseDatos.actualizar(values, "ID=?", selectionArgs)
                if (ID > 0) {
                    Toast.makeText(this, "Nota agregada correctamente", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Nota no agregada correctamente", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
