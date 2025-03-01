
# PescaPro

**PescaPro** es una aplicación móvil diseñada para los apasionados de la pesca. Su objetivo es facilitar el registro y la gestión de las capturas realizadas, permitiendo almacenar detalles como el nombre o título, descripción, peso, imagen opcional y, ahora, la ubicación (latitud y longitud) donde se tomó la foto. Además, la app ofrece útiles consejos para mejorar la experiencia de pesca, convirtiéndose en una herramienta integral para cualquier pescador.

---

## Características

- **Registro de Capturas:**  
  Permite añadir, editar y eliminar registros de capturas. Cada registro (o "tarjeta") incluye:
  - **Título:** Representa el nombre o título de la captura.
  - **Descripción:** Se utiliza para detallar la captura.
  - **Peso:** Se registra en kilogramos.
  - **Imagen:** Opcional, se puede capturar con la cámara o seleccionar de la galería. La imagen se guarda en formato Base64.
  - **Ubicación:** Si la imagen contiene metadatos EXIF, se extraen la latitud y longitud. Estos datos permiten visualizar la ubicación exacta de la captura en un mapa.

- **Autenticación de Usuarios:**  
  La aplicación gestiona el inicio de sesión, registro y recuperación de contraseña utilizando Firebase Authentication. Esto incluye el envío de correos de verificación durante el registro.

- **Consejos de Pesca:**  
  Incorpora un fragmento con consejos prácticos y recomendaciones para mejorar las técnicas de pesca.

- **Gestión de Perfil:**  
  Los usuarios pueden acceder a un perfil donde se muestra su información básica (por ejemplo, el correo electrónico con el que iniciaron sesión).

- **Interfaz Intuitiva:**  
  Utiliza un Navigation Drawer y una barra de navegación inferior para acceder fácilmente a las diferentes secciones de la aplicación.

- **Captura y Selección de Imágenes con Ubicación:**  
  La app integra la cámara y la galería del dispositivo para seleccionar o capturar imágenes. Durante este proceso:
  - Se extraen los datos EXIF de la imagen (latitud y longitud) si están disponibles.
  - Estos datos se utilizan para abrir Google Maps y mostrar la ubicación exacta de la captura.

- **Splash Screen con Video de Bienvenida (Opcional):**  
  En algunas versiones se implementó un Splash Screen que reproduce un video de YouTube embebido mediante un WebView (con opción "Omitir") para mejorar la experiencia de inicio.

---

## Tecnologías y Arquitectura

- **MVVM (Model-View-ViewModel):**  
  La aplicación sigue el patrón de diseño MVVM, separando la lógica de negocio de la interfaz de usuario para lograr una mejor mantenibilidad y escalabilidad.

- **Hilt (Dependency Injection):**  
  Se utiliza Hilt para la inyección de dependencias, simplificando la gestión de componentes como ViewModels, Repositories y casos de uso.

- **Firebase Authentication:**  
  Gestiona el registro, inicio de sesión, recuperación y verificación de usuarios.

- **Retrofit y Kotlin Serialization:**  
  Se utiliza Retrofit para la comunicación con la API y Kotlin Serialization para la conversión de objetos a JSON y viceversa.

- **FileProvider:**  
  Configurado para permitir el acceso seguro a los archivos (por ejemplo, imágenes capturadas) y evitar errores al compartirlos con otras aplicaciones.

- **Kotlin y AndroidX:**  
  Desarrollada en Kotlin y utilizando las últimas bibliotecas de AndroidX (RecyclerView, Navigation, etc.) para una experiencia moderna y eficiente.

---

## Requisitos

- **Android Studio:**  
  Se recomienda utilizar la versión más reciente de Android Studio.

- **SDK Mínimo:**  
  Android API nivel 30 o superior.

- **Gradle:**  
  Utiliza la versión de Gradle especificada en los archivos `build.gradle` del proyecto.

---

## Instalación

1. **Clonar el Repositorio:**  
   Abre una terminal y ejecuta:
   ```bash
   git clone https://github.com/JuanraDAM/Kotlin2DAM.git
   ```
   (Asegúrate de seleccionar la rama correspondiente, por ejemplo, `Version3.1Y3.2`).

2. **Abrir en Android Studio:**  
   Importa el proyecto clonado en Android Studio.

3. **Configurar Firebase:**
  - Crea un proyecto en Firebase.
  - Descarga el archivo `google-services.json` y colócalo en la carpeta `app`.
  - Sincroniza el proyecto con Gradle para integrar las dependencias de Firebase.

4. **Verificar la Configuración del FileProvider:**
  - Asegúrate de que el archivo `file_paths.xml` se encuentre en `app/src/main/res/xml/` con la siguiente configuración:
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <paths xmlns:android="http://schemas.android.com/apk/res/android">
        <external-files-path name="external_files" path="." />
    </paths>
    ```

5. **Ejecutar la Aplicación:**  
   Compila y ejecuta la aplicación en un dispositivo físico o un emulador Android.

---

## Uso

1. **Inicio de Sesión / Registro:**
  - Al abrir la aplicación, el usuario debe iniciar sesión o registrarse mediante su correo electrónico y contraseña.
  - El proceso de registro incluye la verificación de correo electrónico.

2. **Registro y Gestión de Capturas:**
  - Una vez autenticado, el usuario puede:
    - Añadir nuevas capturas pulsando el botón flotante.
    - Completar un formulario donde se ingresa el título, descripción, peso, y se selecciona o captura una imagen.
    - Durante la selección o captura de imagen, se extraen los datos EXIF (latitud y longitud) y, si están disponibles, se almacenan junto a la captura.
    - Editar o eliminar capturas desde cada tarjeta.
    - Abrir Google Maps para ver la ubicación donde se tomó la imagen. Esto se logra pulsando el botón de mapas en la tarjeta. Si la imagen no contiene EXIF, se utilizarán las coordenadas almacenadas en la tarjeta.

3. **Consejos de Pesca y Perfil:**
  - A través del menú lateral o inferior, el usuario podrá acceder a:
    - Consejos prácticos para la pesca.
    - Su perfil, donde se muestra información básica como el correo electrónico.

4. **Splash Screen (Opcional):**
  - En el primer lanzamiento, se muestra un Splash Screen que reproduce un video de YouTube embebido mediante un WebView, con opción para omitir el video y continuar a la pantalla de login.

---

## Estructura del Proyecto

El proyecto se organiza siguiendo los principios de Clean Architecture y MVVM:

```plaintext
PescaPro/
├── MyApplication.kt
├── data
│   ├── auth
│   │   ├── AuthRepository.kt
│   │   └── UserRepository.kt
│   ├── cards
│   │   ├── datasource
│   │   │   └── SharedPrefsDataSource.kt
│   │   └── repository
│   │       └── CardRepositoryImpl.kt
│   └── remote
│       ├── ApiService.kt
│       ├── AuthRemoteDataSource.kt
│       └── UserRemoteDataSource.kt
├── di
│   ├── AppModule.kt
│   ├── AuthInterceptor.kt
│   ├── CardRepositoryModule.kt
│   ├── NetworkModule.kt
│   └── TokenManager.kt
├── domain
│   ├── auth
│   │   ├── AuthResponse.kt
│   │   ├── LoginRequest.kt
│   │   ├── RecoverPasswordRequest.kt
│   │   ├── RegisterRequest.kt
│   │   └── UpdateUserRequest.kt
│   └── cards
│       ├── models
│       │   └── Card.kt         // Incluye latitude y longitude
│       ├── repository
│       │   └── CardRepository.kt
│       ├── requests
│       │   ├── CreateItemRequest.kt  // Incluye latitude y longitude
│       │   ├── ItemsResponse.kt
│       │   └── UpdateItemRequest.kt  // Incluye latitude y longitude
│       └── usecase
│           ├── UseCaseLoadCards.kt
│           ├── UseCaseSaveCards.kt
│           └── UseCaseDeleteCard.kt
└── ui
    ├── viewmodel
    │   └── cards
    │       └── ListViewModel.kt
    └── views
        ├── activities
        │   ├── LoginActivity.kt
        │   └── ListActivity.kt
        ├── adapters
        │   ├── MyAdapter.kt
        │   └── StaticTipsAdapter.kt
        └── fragments
            ├── CardDialogFragment.kt  // Maneja imágenes Base64 y coordenadas
            ├── FishingTipsFragment.kt
            └── UserFragment.kt
```

### Descripción de Directorios

- **data:**  
  Contiene el acceso a datos, tanto locales (SharedPreferences) como remotos (API).

- **di:**  
  Configuración de la inyección de dependencias con Hilt (por ejemplo, AppModule.kt y NetworkModule.kt).

- **domain:**  
  Define el núcleo de la lógica de negocio: modelos, repositorios, requests y casos de uso.  
  Los modelos y requests se han actualizado para incluir campos de ubicación (latitude y longitude).

- **ui:**  
  Se encarga de la interfaz de usuario, dividida en:
  - **activities:** Pantallas principales (LoginActivity, ListActivity).
  - **fragments:** Componentes UI como CardDialogFragment (para agregar/editar tarjetas, gestionando imágenes y ubicación), FishingTipsFragment y UserFragment.
  - **adapters:** Adaptadores para listas (MyAdapter, StaticTipsAdapter).

---

## Configuración Adicional

### FileProvider

Para trabajar correctamente con imágenes capturadas (usadas en CardDialogFragment) se ha configurado FileProvider.  
**En el AndroidManifest.xml:**

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

**Archivo file_paths.xml (en app/src/main/res/xml/):**

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path name="external_files" path="." />
</paths>
```

### Permisos y Configuración del Manifest

Se han incluido todos los permisos necesarios, como acceso a la cámara, almacenamiento y conexión a Internet. Además, se ha configurado el Splash Screen (si se utiliza) y las actividades principales.

---

## Instalación

1. **Clonar el Repositorio:**
   ```bash
   git clone https://github.com/JuanraDAM/Kotlin2DAM.git
   ```
   (Selecciona la rama correspondiente, por ejemplo, `Version3.1Y3.2`).

2. **Abrir en Android Studio:**  
   Importa el proyecto clonado.

3. **Configurar Firebase:**
  - Crea un proyecto en Firebase y añade tu aplicación.
  - Descarga y coloca el archivo `google-services.json` en la carpeta `app`.
  - Sincroniza el proyecto con Gradle.

4. **Verificar la Configuración del FileProvider:**  
   Asegúrate de que el archivo `file_paths.xml` esté en la carpeta correcta.

5. **Ejecutar la Aplicación:**  
   Compila y ejecuta la aplicación en un dispositivo físico o un emulador.

---

## Uso

1. **Inicio de Sesión / Registro:**
  - Los usuarios deben iniciar sesión o registrarse utilizando su correo electrónico y contraseña.
  - El registro incluye la verificación de correo (Firebase Authentication).

2. **Registro y Gestión de Capturas:**
  - Una vez autenticado, el usuario puede añadir nuevas capturas pulsando el botón flotante.
  - El formulario permite ingresar título, descripción, peso y seleccionar o capturar una imagen.
  - Al seleccionar o capturar la imagen, se extraen los datos EXIF (latitud y longitud) y se guardan en la tarjeta.
  - El botón de mapas en cada tarjeta abre Google Maps con las coordenadas extraídas o almacenadas.
  - También es posible editar y eliminar las tarjetas.

3. **Consejos de Pesca y Perfil:**
  - A través del menú lateral o inferior, el usuario puede acceder a consejos de pesca y a su perfil.

4. **Splash Screen (Opcional):**
  - En el primer lanzamiento, se puede mostrar un video de bienvenida embebido mediante WebView (con opción "Omitir") antes de pasar a la pantalla de login.

---