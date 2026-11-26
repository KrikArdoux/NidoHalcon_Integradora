Integrantes:
20253ds049
20253ds034
20253ds051


Breve descripción de la solución
El Nido del Halcón es una aplicación Android de punto de venta para una cafetería universitaria. Está desarrollada en Kotlin con Jetpack Compose para la interfaz, utiliza Room para guardar información localmente en el dispositivo y Retrofit para comunicarse con un servidor mediante una API REST.

La aplicación permite al cajero iniciar sesión, consultar y administrar platillos, manejar variantes y stock, editar el menú del día, registrar pedidos, generar tickets y consultar el historial de ventas. Una parte importante del sistema es que puede seguir trabajando con información local aunque falle la conexión al servidor, ya que los datos principales también se almacenan en Room.

El flujo general es:
Pantalla
   ↓
ViewModel
   ↓
Repository
   ↓
Room / DAO ───── Retrofit
   ↓                ↓
Base local        API REST

Por ejemplo, al confirmar una venta, PedidoScreen solicita la operación a PedidoViewModel, este llama a PedidoRepository, y el repositorio registra el ticket y descuenta el stock en la base de datos local. Después intenta enviar esa venta al servidor.
Instrucciones básicas de ejecución
Para ejecutar el proyecto:
1. Abrir la carpeta ElNidoDelHalcon en Android Studio.
2. Esperar a que Android Studio termine la sincronización de Gradle.
3. Tener instalado un JDK 17, ya que el proyecto está configurado para Java 17.
4. Seleccionar un emulador Android o un dispositivo físico.
5. Ejecutar la aplicación con el botón Run ▶.
El proyecto utiliza:
compileSdk = 35
minSdk = 24
targetSdk = 35
JVM / Java = 17

Si solamente quieres probar las funciones locales, la aplicación puede utilizar los datos almacenados con Room.
Ejecución con backend
La configuración actual del servidor se encuentra en:
data/remote/RetrofitClient.kt

Actualmente utiliza:
const val BASE_URL = "http://10.0.2.2:8080/"

10.0.2.2 es una dirección especial utilizada por el emulador de Android para acceder al localhost de la computadora.

Por lo tanto, para usar la API debes tener el backend ejecutándose en la computadora en:
http://localhost:8080

y después iniciar la aplicación desde el emulador.

Si utilizas un celular físico, 10.0.2.2 no funciona. En ese caso debes cambiar BASE_URL por la IP local de la computadora, por ejemplo:
const val BASE_URL = "http://192.168.1.50:8080/"

El celular y la computadora deben encontrarse normalmente en la misma red.
Endpoints utilizados por el sistema
Los endpoints están definidos en ApiService.kt.
Método	Endpoint	Función
POST	/api/auth/login	Iniciar sesión del cajero.
GET	/api/platillos	Consultar todos los platillos.
POST	/api/platillos	Crear un platillo nuevo.
PUT	/api/platillos/{id}	Actualizar un platillo existente.
PATCH	/api/platillos/{id}/desactivar	Desactivar un platillo.
GET	/api/menu-del-dia	Consultar los menús del día.
PUT	/api/menu-del-dia/{id}	Actualizar un menú del día.
POST	/api/tickets	Registrar una venta/ticket.
GET	/api/tickets/cajero/{matricula}	Consultar las ventas de un cajero.


Ejemplos
Inicio de sesión:
POST http://localhost:8080/api/auth/login

La aplicación envía un objeto LoginRequestDto con las credenciales del cajero.

Consultar productos:
GET http://localhost:8080/api/platillos

Crear un platillo:
POST http://localhost:8080/api/platillos

Modificar el platillo con ID 5:
PUT http://localhost:8080/api/platillos/5

Desactivarlo:
PATCH http://localhost:8080/api/platillos/5/desactivar

Registrar una venta:
POST http://localhost:8080/api/tickets

Consultar las ventas del cajero con matrícula 2023001:
GET http://localhost:8080/api/tickets/cajero/2023001
