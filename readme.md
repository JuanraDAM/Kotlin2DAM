# Dog API Clean Architecture - Caso 5: Eliminar un Perro Individual

## Objetivo de la Tarea

El objetivo de esta tarea es modificar la aplicación para que se pueda **eliminar un perro individual**. Además, si tras borrar un perro la base de datos queda vacía, se recargan los perros de forma nativa (como ocurre cuando se borra toda la base de datos).

## ¿Qué se ha hecho?

1. **Nuevo Caso de Uso (DeleteDogUseCase):**  
   Se ha creado un caso de uso llamado `DeleteDogUseCase` que se encarga de:
   - Eliminar un perro individual de la base de datos.
   - Verificar si, después de la eliminación, no quedan perros en la base de datos.  
     Si es así, se vuelven a cargar los perros que vienen por defecto (datos nativos).

2. **Modificación en la UI (Interfaz de Usuario):**
   - En el layout del item del RecyclerView (`item_dog.xml`), se ha añadido un botón "Eliminar".
   - En el ViewHolder correspondiente se ha configurado ese botón para que, mediante una lambda, llame al método de eliminación del ViewModel.
   - El adaptador (`DogAdapter`) se encarga de pasar esta lambda a cada ViewHolder.

3. **Actualización del ViewModel:**
   - Se añadió un método `deleteDog(dog: Dog)` en el ViewModel.  
     Este método se encarga de llamar al `DeleteDogUseCase` para eliminar el perro y luego actualizar la lista de perros que se muestra en la pantalla.

## Cambios y Mejoras Propuestas

Aunque la funcionalidad ya cumple con lo solicitado, se han observado algunos aspectos que se podrían mejorar:

- **Manejo del Identificador (ID):**
   - **Lo que pasaba:**  
     Al convertir el objeto de dominio (`Dog`) a la entidad de base de datos (`DogEntity`), el ID se perdía (se usaba un valor por defecto).
   - **Cambio realizado:**  
     Se ha modificado el mapeo para que se preserve el ID, lo que permite que Room sepa exactamente qué registro eliminar.

- **Uso de Variables Globales:**
   - **Situación actual:**  
     La lista de perros se guarda en un objeto global (`Repository.dogs`), lo que puede complicar la actualización y el mantenimiento.
   - **Propuesta de mejora:**  
     Manejar el estado de la lista directamente en el ViewModel (por ejemplo, usando LiveData o Kotlin Flow) para que la actualización de la UI sea más directa y menos dependiente de variables globales.

- **Actualización de la Lista:**
   - **Situación actual:**  
     Tras borrar un perro, se vuelve a recargar toda la lista desde la base de datos.
   - **Propuesta de mejora:**  
     Actualizar únicamente el elemento eliminado en la lista, lo que haría que la aplicación sea más rápida y eficiente.
