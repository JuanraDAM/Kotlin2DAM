# PescaPro

## Descripción

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
  La aplicación sigue el patrón de diseño MVVM, lo que facilita la separación entre la lógica de negocio y la interfaz de usuario.

- **Hilt (Dependency Injection):**  
  Se utiliza Hilt para la inyección de dependencias, simplificando la gestión de componentes como ViewModels y Repositories.

- **Firebase Authentication:**  
  Gestiona el registro, inicio de sesión y verificación de usuarios.

- **SharedPreferences y Gson:**  
  Almacenamiento local de los registros (tarjetas) en formato JSON, permitiendo una persistencia sencilla de los datos.

- **Kotlin y AndroidX:**  
  Desarrollada completamente en Kotlin, aprovechando las últimas bibliotecas de AndroidX, como RecyclerView para la visualización de listas.

## Requisitos

- **Android Studio:**  
  Se recomienda utilizar la versión más reciente.

- **SDK Mínimo:**  
  Android API nivel 21 o superior.

- **Gradle:**  
  Se debe utilizar la versión indicada en los archivos `build.gradle` del proyecto.

## Instalación

1. **Clonar el Repositorio:**  
   Abre una terminal y ejecuta el siguiente comando:
   ```bash
   git clone https://github.com/JuanraDAM/Kotlin2DAM/tree/mvvmYhit
   ```
   _(Reemplaza la URL por la de tu repositorio real cuando lo tengas disponible.)_

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
   Al abrir la aplicación, el usuario deberá registrarse o iniciar sesión utilizando su correo electrónico y contraseña. El registro incluye el envío de un correo de verificación.

2. **Registro de Capturas:**  
   Una vez autenticado, el usuario puede:
    - Añadir nuevas capturas pulsando el botón flotante.
    - Completar el formulario que permite ingresar el título, descripción, peso y seleccionar o capturar una imagen.
    - Editar o eliminar las capturas registradas a través de opciones disponibles en cada tarjeta.

3. **Consejos de Pesca y Perfil:**  
   A través del menú lateral o inferior, el usuario podrá:
    - Acceder a una sección con consejos prácticos para la pesca.
    - Visualizar su perfil en el que se muestra, por ejemplo, su correo electrónico.

## Estructura del Proyecto

```plaintext
PescaPro/
├── AppModule.kt
├── CardDialogFragment.kt
├── Card.kt
├── CardRepository.kt
├── FishingTipsFragment.kt
├── ListActivity.kt
├── ListViewModel.kt
├── LoginActivity.kt
├── MyAdapter.kt
├── MyApplication.kt
├── StaticTipsAdapter.kt
└── UserFragment.kt
```

> **Nota:**  
> La lista de ficheros proporcionada parece completa. Si en algún momento se añade o se requiere algún nuevo componente, se actualizará este documento.
