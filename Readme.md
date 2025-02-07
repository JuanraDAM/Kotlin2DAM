
```markdown
# Example with Catalogs

Este es un proyecto de ejemplo en **Kotlin** para Android que implementa una arquitectura basada en MVVM y utiliza Room, Kotlin Coroutines, LiveData y Navigation. El proyecto está organizado en varios módulos (application, data, domain, repository y ui) y demuestra funcionalidades como:

- **Login y Registro de Usuarios:**  
  Permite iniciar sesión y registrar nuevos usuarios, persistiendo la información en una base de datos local mediante Room y utilizando SharedPreferences para mantener la sesión activa.

- **Listado de Usuarios:**  
  Muestra los usuarios registrados en un listado utilizando un RecyclerView con CardViews. Cada tarjeta presenta información del usuario (nombre, email, teléfono, etc.) y dispone de botones para eliminar o editar el nombre del usuario.

- **Perfil de Usuario:**  
  Una vez logueado, se muestra un perfil con los datos del usuario actual.

- **Edición y Eliminación de Usuarios:**  
  En el listado de usuarios, se puede pulsar el botón de eliminar para quitar un usuario de la base de datos, o el botón de editar para modificar el nombre del usuario a través de un diálogo.

---

---

## Requisitos

- **Android Studio 4.0** o superior.
- **SDK mínimo:** Configurado en el proyecto (por ejemplo, API 21 o superior, según tu configuración).
- **Dependencias principales:**
  - Kotlin Coroutines (por ejemplo, `org.jetbrains.kotlinx:kotlinx-coroutines-android`)
  - Room Persistence Library (incluyendo `room-runtime` y `room-ktx`)
  - Lifecycle (LiveData, ViewModel)
  - Navigation Component
  - CardView y ConstraintLayout

---

## Instrucciones de Instalación y Ejecución

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/JuanraDAM/Kotlin2DAM/tree/ProyectoDebug1
   ```

2. **Abrir el proyecto en Android Studio:**
    - Selecciona *Open an Existing Project* y busca la carpeta clonada.

3. **Sincronizar Gradle:**
    - Asegúrate de que todas las dependencias se descarguen correctamente (usa *Sync Now* cuando Android Studio lo indique).

4. **Ejecutar la aplicación:**
    - Conecta un dispositivo o utiliza un emulador.
    - Ejecuta la aplicación desde Android Studio.

---

## Funcionalidades Clave

### Login y Registro
- Al iniciar la app se muestra la pantalla de login.
- Si el usuario ya está logueado (almacenado en SharedPreferences), se carga directamente la MainActivity.
- El registro de usuarios se realiza a través de un diálogo en la pantalla de login.

### Listado de Usuarios y Edición
- En la pestaña de usuarios (UsersFragment) se muestra un listado de usuarios mediante un RecyclerView.
- Cada usuario se muestra en una CardView que incluye:
    - Imagen del usuario (puedes usar un recurso por defecto).
    - Nombre, email y teléfono.
    - Un botón para eliminar el usuario.
    - **Un botón para editar el nombre**: Al pulsarlo se muestra un diálogo que permite introducir el nuevo nombre; al confirmar, se actualiza el nombre en la base de datos y se refresca la lista.

### Eliminación de Usuarios
- Al pulsar el botón de eliminar en la CardView, se invoca el método `deleteUser()` del ViewModel, el cual actualiza la base de datos y refresca el listado.

---

## Cómo Probar la Edición y Eliminación

1. **Edición del Nombre:**
    - En el listado de usuarios, pulsa el botón de editar (ícono de lápiz) en la tarjeta del usuario.
    - Se mostrará un diálogo con un campo de texto prellenado con el nombre actual.
    - Introduce el nuevo nombre y pulsa "Guardar". El cambio se reflejará en el listado y, si es el usuario logueado, en el perfil.

2. **Eliminación de Usuario:**
    - Pulsa el botón de eliminar (ícono de basurero) en la tarjeta de un usuario.
    - El usuario se eliminará de la base de datos y la lista se refrescará automáticamente.

---

## Dependencias y Configuración de Gradle

Asegúrate de tener en tu `build.gradle` (módulo app) algo similar a lo siguiente:

```gradle
android {
    compileSdkVersion 33
    defaultConfig {
        applicationId "com.example.srodenas.example_with_catalogs"
        minSdkVersion 21
        targetSdkVersion 33
        versionCode 1
        versionName "1.0"
    }
    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Lifecycle components
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.5.1'
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4'

    // Room components
    implementation 'androidx.room:room-runtime:2.4.3'
    kapt 'androidx.room:room-compiler:2.4.3'
    implementation 'androidx.room:room-ktx:2.4.3'

    // Navigation Component
    implementation 'androidx.navigation:navigation-fragment-ktx:2.5.2'
    implementation 'androidx.navigation:navigation-ui-ktx:2.5.2'

    // RecyclerView y CardView
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'androidx.cardview:cardview:1.0.0'
}
```

---

## Conclusión

Este proyecto sirve como ejemplo para aprender y practicar la implementación de una aplicación Android utilizando una arquitectura modular, con funciones de login, registro, listado, edición y eliminación de usuarios. Puedes ampliar la funcionalidad según tus necesidades, por ejemplo, agregando validación de datos, gestión de imágenes, notificaciones, etc.

---