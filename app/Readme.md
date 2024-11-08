# Proyecto Android: PescaPro

Actualmente la aplicación **PescaPro**, no esta completa, simplemente es un simple login de momento
## Tabla de Contenidos

- [Proyecto Android: PescaPro](#proyecto-android-pescapro)
  - [Tabla de Contenidos](#tabla-de-contenidos)
  - [Vista Previa](#vista-previa)
  - [Características](#características)
  - [Estructura del Proyecto](#estructura-del-proyecto)
    - [XML Layouts](#xml-layouts)
    - [Activities](#activities)

## Vista Previa

Capturas de pantalla del inicio de sesión y lista de elementos se pueden agregar aquí para ilustrar la funcionalidad de la aplicación.

## Características

- **Inicio de Sesión**: Verificación básica de usuario y contraseña en la `LoginActivity`.
- **Lista de Elementos**: Visualización de datos en un `RecyclerView` con un `CardView` personalizado para cada elemento.
- **Navegación Simple**: Barra superior e inferior para facilitar la navegación dentro de la aplicación.

## Estructura del Proyecto

### XML Layouts

1. **`activity_login.xml`** - Layout para la pantalla de inicio de sesión.
    - Incluye un `Toolbar` personalizado con un ícono de menú y un título.
    - Campos de entrada de usuario y contraseña (`EditText`).
    - Botones de **Register** y **Login** personalizados con fondos selectores.

2. **`activity_list.xml`** - Layout para la pantalla de lista de elementos.
    - Contiene un `RecyclerView` para mostrar datos en tarjetas (`CardView`).
    - Incluye una barra superior (`topBar`) y una barra de navegación inferior (`bottomNavigation`) con botones de navegación.
    - Un `FloatingActionButton` para agregar elementos.

3. **`item_card.xml`** - Layout para cada tarjeta individual en el `RecyclerView`.
    - Contiene un `ImageView` para mostrar un ícono de elemento, y varios `TextView` para mostrar el título, descripción y peso.

### Activities

1. **`LoginActivity`**: Actividad principal para la autenticación del usuario.
    - Campos y botón de inicio de sesión (`loginButton`) con lógica para verificar las credenciales:
        - Usuario: `"usuario"`
        - Contraseña: `"1234"`
    - Al inicio de sesión exitoso, se redirige a `ListActivity` pasando el nombre de usuario y la contraseña como `Intent` extras.

   ```kotlin
   val intent = Intent(this@LoginActivity, ListActivity::class.java)
   intent.putExtra("USERNAME", enteredUser)
   intent.putExtra("PASSWORD", enteredPass)
   startActivity(intent)
   finish()

2. **`ListActivity`**: Actividad que muestra una lista de elementos usando un RecyclerView
       - Recibe el nombre de usuario y la contraseña desde `LoginActivity` a través de Intent extras.
       - Configura el RecyclerView para mostrar la información del usuario autenticado en una lista de elementos utilizando el adaptador personalizado `MyAdapter`.
       - Contiene una barra superior con un título (`PescaPro`) y un ícono de menú.
       - Incluye una barra de navegación inferior con tres íconos (`nav_profile`, `nav_home`, `nav_info`):
          - `nav_profile`: Al seleccionarlo, regresa al `LoginActivity`.



    ```kotlin
    // Obtener datos del Intent y configurar RecyclerView
    val user = intent.getStringExtra("USERNAME")
    val password = intent.getStringExtra("PASSWORD")

    private fun setUpRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter(user!!, password!!)
        recyclerView?.adapter = adapter
    }

    // Configurar acción para el botón de perfil
    profileButton.setOnClickListener {
        val loginIntent = Intent(this@ListActivity, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

3. **`MyAdapter`**: Adaptador personalizado para el RecyclerView en `ListActivity`
   - El adaptador recibe los valores de `user` y `password` del usuario autenticado en su constructor, y los muestra en cada tarjeta de la lista.
   - Usa un diseño de tarjeta (`CardView`) para mostrar los datos de cada usuario, incluyendo campos básicos como el título, descripción, y peso (personalizable).
   - Cada tarjeta también incluye botones para editar y eliminar el elemento.

    #### **Estructura de la tarjeta:**
    - **Imagen de usuario** (`item_image`): Icono representativo.
    - **Título** (`item_title`): Texto que indica que es "Información del usuario".
    - **Descripción** (`item_description`): Muestra el nombre de usuario y contraseña del usuario autenticado.
    - **Peso** (`item_weight`): Campo de texto que muestra el peso, que podría ser un campo personalizable.
    - **Botones**:
        - `edit_button`: Botón para editar la información del elemento (sin funcionalidad específica en este ejemplo).
        - `delete_button`: Botón para eliminar el elemento (sin funcionalidad específica en este ejemplo).

    #### **Código en Kotlin para `MyAdapter`:**

    ```kotlin
    package com.example.proyectoevaluable

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView

    class MyAdapter(private val user: String, private val password: String) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.titleTextView.text = "Información del usuario"
            holder.descriptionTextView.text = "Usuario: $user\nContraseña: $password"
            holder.weightTextView.text = "Peso: --" // Este campo es opcional y personalizable
        }

        override fun getItemCount(): Int {
            return 1 // Número de tarjetas mostradas, aquí se limita a un solo elemento
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.item_title)
            val descriptionTextView: TextView = itemView.findViewById(R.id.item_description)
            val weightTextView: TextView = itemView.findViewById(R.id.item_weight)
        }
    }

