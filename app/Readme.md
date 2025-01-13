# Proyecto “PescaPro”

Esta aplicación está desarrollada en Android (Kotlin) y permite:

1. **Gestionar usuarios** (registro, login, logout) mediante Firebase Authentication.
2. **Crear, editar y eliminar** tarjetas personalizadas (un CRUD básico) almacenadas en `SharedPreferences` de forma local.
3. **Navegar** a través de un **Navigation Drawer**, con opciones como logout y vista de usuario.
4. **Presentar** la información en una **lista (RecyclerView)** y utilizar un **DialogFragment** para añadir o editar tarjetas (con soporte de cámara y galería para fotos).

## Estructura de la aplicación

1. **LoginActivity**
    - Permite **iniciar sesión** o **registrar** un nuevo usuario.
    - Verifica si el usuario ya está logueado o si el correo fue verificado.
    - Incluye **recuperación de contraseña** a través de Firebase.
    - Usa `FirebaseAuth` para la autenticación.

2. **ListActivity**
    - Es la pantalla principal tras el login.
    - Contiene un **Navigation Drawer** con opciones (CRUD, Lista Genérica, Logout...).
    - Muestra en un **RecyclerView** las tarjetas (`Card`) guardadas para cada usuario.
    - Permite **añadir** (FAB), **editar** y **eliminar** tarjetas.
    - Gestiona las tarjetas mediante `SharedPreferences` (cada usuario tiene su propia clave).

3. **MyAdapter**
    - Adaptador para el `RecyclerView`.
    - Maneja la visualización de cada `Card`, con **botones para editar** (muestra un diálogo de edición) y **eliminar** (muestra alerta de confirmación).
    - Soporta mostrar la foto de la tarjeta (o un logo por defecto), y ampliarla en un `AlertDialog`.

4. **CardDialogFragment**
    - Se muestra como **diálogo** para **crear o editar** una tarjeta.
    - Permite ingresar título, descripción, peso y foto.
    - Opción de **abrir cámara** o **galería** para seleccionar la foto.
    - Guarda la foto en almacenamiento interno y la convierte a `Uri`.

5. **UserFragment** (opcional, si implementaste la vista de usuario)
    - Se muestra dentro del `ListActivity` (reemplazando un contenedor) para ver info del usuario, etc.

6. **Card (data class)**
    - Modelo de datos con `username`, `password`, `weight`, `photoUri`.

7. **FishingTipsFragment**
   - Es un `Fragment` que muestra una lista de consejos sobre pesca.
   - Utiliza un `RecyclerView` para presentar las tarjetas de consejos (`Card`) de forma estática.
   - Cada tarjeta incluye un título, descripción y una imagen opcional.
   - No permite editar ni eliminar tarjetas, ya que su propósito es solo mostrar información estática.
   - Se integra en el contenedor de `ListActivity` al seleccionar el botón de "Info" o la opción correspondiente en el menú.

8. **StaticTipsAdapter**
   - Adaptador específico para el `RecyclerView` de `FishingTipsFragment`.
   - Renderiza las tarjetas (`Card`) con un diseño simplificado (sin botones de edición ni eliminación).
   - Soporta mostrar imágenes asociadas a cada consejo o un icono por defecto si no hay imagen disponible.
   - Es una clase independiente para separar la lógica de las tarjetas estáticas de las tarjetas interactivas manejadas por `MyAdapter`.

## Dependencias principales

- **Firebase Auth** (`com.google.firebase:firebase-auth`) para el sistema de login/registro.
- **Material Components** (`com.google.android.material:material`) para vistas y estilos modernos (NavigationView, FAB, etc.).
- **Gson** (`com.google.code.gson:gson`) para serializar/deserializar las tarjetas en `SharedPreferences`.

## Flujo de uso

1. **Login/Register**: El usuario ingresa su email/contraseña en `LoginActivity`. Si es nuevo, se registra; si ya existe, hace login.
2. **Verificación de correo**: Al registrarse, se envía un correo de verificación. Se requiere verificarlo antes de iniciar sesión.
3. **Pantalla principal (ListActivity)**:
    - Aparece la **lista de tarjetas** del usuario (vaciada si no tiene ninguna).
    - El **Navigation Drawer** ofrece acciones (CRUD personalizado, Lista Genérica - que vuelve a esta misma pantalla, Logout, etc.).
    - El **FAB** crea una nueva tarjeta (abre `CardDialogFragment`).
    - Cada tarjeta tiene **botones** para editar o eliminar.
    - Al pulsar en la **imagen**, se muestra en grande en un `AlertDialog`.
4. **Al cerrar sesión** (`Logout`), se vuelve a `LoginActivity`.

## Cómo compilar y ejecutar

1. Clona o descarga el repositorio.
2. Abre el proyecto en **Android Studio** (versión compatible con Kotlin y las librerías de Material).
3. Asegúrate de tener configuradas las dependencias en `build.gradle` (módulo `app`) y de contar con un archivo `google-services.json` (si tu proyecto usa Firebase).
4. Conecta un dispositivo o usa un emulador con minSdkVersion >= 19 (o la que hayas definido).
5. Compila y ejecuta.

