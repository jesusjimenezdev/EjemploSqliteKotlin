package jesus.net.ejemplosqlitekotlin

class Nota {

    var notaID: Int? = null
    var titulo: String? = null
    var descripcion: String? = null

    constructor(notaID: Int, titulo: String, descripcion: String) {
        this.notaID = notaID
        this.titulo = titulo
        this.descripcion = descripcion
    }
}
