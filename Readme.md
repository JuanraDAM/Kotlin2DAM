# PescaPro

PescaPro es una aplicación móvil diseñada para los apasionados de la pesca. Su objetivo es facilitar el registro y gestión de las capturas realizadas, permitiendo almacenar detalles como el nombre o título, descripción, peso y una imagen opcional de cada pieza capturada. Además, la app ofrece útiles consejos para mejorar la experiencia de pesca, convirtiéndose en una herramienta integral para cualquier pescador.

## Características

- **Registro de Capturas:**  
  Permite añadir, editar y eliminar registros de capturas. Cada registro (o "tarjeta") incluye:
    - Título (nombre de la captura)
    - Descripción
    - Peso (en kg)
    - Imagen (opcional, se puede capturar con la cámara o seleccionar de la galería)

- **Autenticación de Usuarios:**  
  Utiliza Firebase Authentication para gestionar el inicio de sesión y registro de usuarios, incluyendo la verificación de correo electrónico.

- **Consejos de Pesca:**  
  Incorpora un fragmento que muestra consejos prácticos y recomendaciones para mejorar las técnicas de pesca.

- **Gestión de Perfil:**  
  Los usuarios pueden acceder a un perfil donde se muestra su información básica (por ejemplo, el correo electrónico con el que iniciaron sesión).

- **Interfaz Intuitiva:**  
  Uso de un Navigation Drawer y navegación inferior para acceder fácilmente a las diferentes secciones de la aplicación.

- **Selección y Captura de Imágenes:**  
  Integración con la cámara y la galería del dispositivo, permitiendo seleccionar o capturar imágenes que se asocian a cada registro de captura.

## Tecnologías y Arquitectura

- **MVVM (Model-View-ViewModel):**  
  La aplicación sigue el patrón de diseño MVVM, separando la lógica de negocio de la interfaz de usuario para una mejor mantenibilidad y escalabilidad.

- **Hilt (Dependency Injection):**  
  Se utiliza Hilt para la inyección de dependencias, simplificando la gestión de componentes como ViewModels y Repositories.

- **Firebase Authentication:**  
  Gestiona el registro, inicio de sesión y verificación de usuarios.

- **SharedPreferences y Gson:**  
  Se utiliza SharedPreferences para el almacenamiento local de los registros (tarjetas) en formato JSON, facilitando una persistencia sencilla de los datos.

- **Kotlin y AndroidX:**  
  Desarrollada en Kotlin y aprovechando las últimas bibliotecas de AndroidX (RecyclerView, Navigation, etc.).

## Requisitos

- **Android Studio:**  
  Se recomienda utilizar la versión más reciente de Android Studio.

- **SDK Mínimo:**  
  Android API nivel 21 o superior.

- **Gradle:**  
  Se debe utilizar la versión de Gradle especificada en los archivos `build.gradle` del proyecto.

## Instalación

1. **Clonar el Repositorio:**  
   Abre una terminal y ejecuta el siguiente comando:
   ```bash
   git clone https://github.com/JuanraDAM/Kotlin2DAM/tree/mvvmYhit
Aquí tienes un ejemplo actualizado del README para PescaPro, que incluye la descripción, características, tecnologías, instrucciones de instalación y uso, además de una explicación de la estructura del proyecto. Puedes copiar y ajustar este contenido según necesites:

---

```markdown
# PescaPro

PescaPro es una aplicación móvil diseñada para los apasionados de la pesca. Su objetivo es facilitar el registro y gestión de las capturas realizadas, permitiendo almacenar detalles como el nombre o título, descripción, peso y una imagen opcional de cada pieza capturada. Además, la app ofrece útiles consejos para mejorar la experiencia de pesca, convirtiéndose en una herramienta integral para cualquier pescador.

## Características

- **Registro de Capturas:**  
  Permite añadir, editar y eliminar registros de capturas. Cada registro (o "tarjeta") incluye:
  - Título (nombre de la captura)
  - Descripción
  - Peso (en kg)
  - Imagen (opcional, se puede capturar con la cámara o seleccionar de la galería)

- **Autenticación de Usuarios:**  
  Utiliza Firebase Authentication para gestionar el inicio de sesión y registro de usuarios, incluyendo la verificación de correo electrónico.

- **Consejos de Pesca:**  
  Incorpora un fragmento que muestra consejos prácticos y recomendaciones para mejorar las técnicas de pesca.

- **Gestión de Perfil:**  
  Los usuarios pueden acceder a un perfil donde se muestra su información básica (por ejemplo, el correo electrónico con el que iniciaron sesión).

- **Interfaz Intuitiva:**  
  Uso de un Navigation Drawer y navegación inferior para acceder fácilmente a las diferentes secciones de la aplicación.

- **Selección y Captura de Imágenes:**  
  Integración con la cámara y la galería del dispositivo, permitiendo seleccionar o capturar imágenes que se asocian a cada registro de captura.

## Tecnologías y Arquitectura

- **MVVM (Model-View-ViewModel):**  
  La aplicación sigue el patrón de diseño MVVM, separando la lógica de negocio de la interfaz de usuario para una mejor mantenibilidad y escalabilidad.

- **Hilt (Dependency Injection):**  
  Se utiliza Hilt para la inyección de dependencias, simplificando la gestión de componentes como ViewModels y Repositories.

- **Firebase Authentication:**  
  Gestiona el registro, inicio de sesión y verificación de usuarios.

- **SharedPreferences y Gson:**  
  Se utiliza SharedPreferences para el almacenamiento local de los registros (tarjetas) en formato JSON, facilitando una persistencia sencilla de los datos.

- **Kotlin y AndroidX:**  
  Desarrollada en Kotlin y aprovechando las últimas bibliotecas de AndroidX (RecyclerView, Navigation, etc.).

## Requisitos

- **Android Studio:**  
  Se recomienda utilizar la versión más reciente de Android Studio.

- **SDK Mínimo:**  
  Android API nivel 21 o superior.

- **Gradle:**  
  Se debe utilizar la versión de Gradle especificada en los archivos `build.gradle` del proyecto.

## Instalación

1. **Clonar el Repositorio:**  
   Abre una terminal y ejecuta el siguiente comando:
   ```bash
   git clone https://github.com/JuanraDAM/Kotlin2DAM/tree/mvvmYhit
_(Reemplaza la URL por la de tu repositorio real cuando esté disponible.)_

2. **Abrir en Android Studio:**  
   Importa el proyecto clonado en Android Studio.

3. **Configurar Firebase:**
  - Crea un proyecto en Firebase.
  - Descarga el archivo `google-services.json` y colócalo en la carpeta `app` del proyecto.
  - Sincroniza el proyecto con Gradle para integrar correctamente las dependencias de Firebase.

4. **Ejecutar la Aplicación:**  
   Compila y ejecuta la aplicación en un dispositivo físico o en un emulador Android.

## Uso

1. **Inicio de Sesión / Registro:**  
   Al abrir la aplicación, el usuario deberá registrarse o iniciar sesión utilizando su correo electrónico y contraseña. El proceso de registro incluye el envío de un correo de verificación.

2. **Registro de Capturas:**  
   Una vez autenticado, el usuario puede:
  - Añadir nuevas capturas pulsando el botón flotante.
  - Completar el formulario para ingresar título, descripción, peso y seleccionar o capturar una imagen.
  - Editar o eliminar las capturas registradas a través de las opciones disponibles en cada tarjeta.

3. **Consejos de Pesca y Perfil:**  
   A través del menú lateral o inferior, el usuario podrá:
  - Acceder a una sección con consejos prácticos para la pesca.
  - Visualizar su perfil con información básica, como su correo electrónico.

## Estructura del Proyecto

El proyecto se organiza siguiendo los principios de Clean Architecture y MVVM para separar las responsabilidades de la aplicación:

```plaintext
PescaPro/
├── application
│   └── MyApplication.kt
├── data
│   └── cards
│       ├── datasource
│       │   └── SharedPrefsDataSource.kt
│       └── repository
│           └── CardRepositoryImpl.kt
├── di
│   └── AppModule.kt
├── domain
│   └── cards
│       ├── models
│       │   └── Card.kt
│       ├── repository
│       │   └── CardRepository.kt
│       └── usecase
│           ├── UseCaseLoadCards.kt
│           ├── UseCaseSaveCards.kt
│           └── UseCaseDeleteCard.kt
└── ui
    ├── viewmodel
    │   └── cards
    │       └── ListViewModel.kt
    └── views
        ├── activities
        │   ├── LoginActivity.kt
        │   └── ListActivity.kt
        ├── adapters
        │   ├── MyAdapter.kt
        │   └── StaticTipsAdapter.kt
        └── fragments
            ├── CardDialogFragment.kt
            ├── FishingTipsFragment.kt
            └── UserFragment.kt
```

### Descripción de Directorios

- **application:**  
  Contiene la clase `MyApplication.kt`, que extiende `Application` y se utiliza para inicializaciones globales. Además, se anota con `@HiltAndroidApp` para habilitar Hilt.

- **data:**  
  Implementa el acceso a datos y las fuentes de datos reales.
  - **datasource:** Contiene clases para acceder a datos locales (ejemplo: `SharedPrefsDataSource.kt` para gestionar SharedPreferences).
  - **repository:** Implementa la interfaz de repositorio definida en la capa de dominio (`CardRepositoryImpl.kt`).

- **di:**  
  Configuración de la inyección de dependencias con Hilt. En `AppModule.kt` se proveen las instancias necesarias (SharedPreferences, Gson, data sources, repositorios y casos de uso).

- **domain:**  
  Contiene el núcleo de la lógica de negocio.
  - **models:** Define las entidades de dominio (por ejemplo, `Card.kt`).
  - **repository:** Declara las interfaces de los repositorios (por ejemplo, `CardRepository.kt`).
  - **usecase:** Encapsula los casos de uso que representan acciones de negocio (cargar, guardar o eliminar tarjetas).

- **ui:**  
  Se encarga de la capa de presentación.
  - **viewmodel:** Contiene los ViewModels que gestionan la lógica de la UI (por ejemplo, `ListViewModel.kt`).
  - **views:** Divide la UI en actividades, fragmentos y adaptadores:
    - **activities:** Pantallas principales (por ejemplo, `LoginActivity.kt` y `ListActivity.kt`).
    - **adapters:** Adaptadores para listas y RecyclerViews (por ejemplo, `MyAdapter.kt` y `StaticTipsAdapter.kt`).
    - **fragments:** Fragmentos que componen la UI (por ejemplo, `CardDialogFragment.kt`, `FishingTipsFragment.kt` y `UserFragment.kt`).

## Notas Adicionales

- **Firebase:** Asegúrate de tener correctamente configurado Firebase (archivo `google-services.json` en la carpeta `app`).
- **Dependencias:** Verifica que en tu archivo `build.gradle` estén incluidas las dependencias de Hilt, Firebase, Gson y AndroidX.
- **Clean Architecture:** La separación en capas (application, data, domain y ui) permite que el proyecto sea más escalable y fácil de mantener, además de facilitar la realización de pruebas unitarias.

---
