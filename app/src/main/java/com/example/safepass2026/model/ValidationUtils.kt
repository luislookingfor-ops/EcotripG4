package com.example.safepass2026.model

// Aplicamos una Funsion de extension, que ayudara a  Blindar la app manejando
// enteros nulos mediante la utilización del operador elvis.
fun Int?.esMayorDeEdad(): Boolean = (this ?: 0) >= 18

// Acontinuacion, aplicamos un Higher-Order Function, osea que creamos una funcion que
// recibe a otra, pero como parametro y asi realizar una validación externa
fun procesarRegistro(usuario: String, edad: Int, validacionExtra: (Int) -> Boolean) {
    if (edad.esMayorDeEdad() && validacionExtra(edad)) {
        println("Registro exitoso para $usuario")
    } else {
        println("Registro rechazado: No cumple con los criterios de validación")
    }
}
// para el ultimo paso, hacemos el uso de "aply", que en generar sirve para instanciar
// al asistente de forma limpia en la logica empleada. Como vimos en clase
// nos ahorramos repetir "nuevoAsistente" varias veces.
fun crearAsistenteEjemplo() {
    // Instanciamos respetando los 'val' asignados en la primera parte
    val nuevoAsistente = Asistente(
        nombre = "Asistente SafePass",
        edad = 25,
        tipoEntrada = "VIP"
    ).apply {
        // podemos realizar acciones extras que no busquen alterar la inmutabilidad
        println("Asistente $nombre instanciado correctamente para el sistema")
    }
}
