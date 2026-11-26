package mx.edu.utez.cafeteria.navigation

object AppRoutes {
    const val LOGIN = "login"
    const val MENU = "menu"
    const val PEDIDO = "pedido"
    const val HISTORIAL = "historial"

    const val ARG_ID_PLATILLO = "idPlatillo"
    const val PLATILLO_FORM = "platillo_form"
    const val PLATILLO_FORM_ROUTE = "$PLATILLO_FORM?$ARG_ID_PLATILLO={$ARG_ID_PLATILLO}"

    const val ARG_NUMERO = "numero"
    const val ARG_NUEVO = "nuevo"
    const val DETALLE = "detalle"
    const val DETALLE_ROUTE = "$DETALLE/{$ARG_NUMERO}?$ARG_NUEVO={$ARG_NUEVO}"

    fun platilloForm(id: Int = -1) = "$PLATILLO_FORM?$ARG_ID_PLATILLO=$id"
    fun detalle(numero: Int, nuevo: Boolean = false) = "$DETALLE/$numero?$ARG_NUEVO=$nuevo"
}
