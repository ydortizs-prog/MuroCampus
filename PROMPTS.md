# PROMPTS · Fase 2 (rama `mejora-ia`)

Mejoras hechas con ayuda de un asistente de IA sin romper la arquitectura **MVVM + Repository**.
La IA propone; yo reviso, pruebo en el emulador y corrijo lo que rompa las reglas
(nada de Retrofit ni HttpException en el ViewModel, y la pantalla nunca llama a la API).

## Mejoras elegidas

| # | Mejora | Capas que cambian | Commit |
|---|--------|-------------------|--------|
| 1 | Encabezado con interceptor (`Accept: application/json`) | RetrofitClient | `feat(red): agrega interceptor que envia Accept: application/json` |
| 2 | Borrar publicación con `@DELETE` y `Response<Unit>` | API + Repository + ViewModel + View | `feat(borrar): elimina una publicacion con DELETE y revisa Response<Unit>` |
| 3 | Búsqueda por título en la lista descargada | ViewModel + View | `feat(busqueda): busca publicaciones por titulo en la lista descargada` |
| 4 | Nuevo diseño: colores, degradado y tarjetas | View (ui/ y theme/) | `feat(ui): nuevo tema de colores, encabezado con degradado y tarjetas mas llamativas` |

## Prompts

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|--------------------|---------------------|-----------------------|---------|
| «Agrega un Interceptor de OkHttp que ponga el encabezado `Accept: application/json` en todas las peticiones de MuroCampus.» | Un `Interceptor { chain -> ... }` que copia la petición con `.header("Accept", ...)` y lo agrega al `OkHttpClient`. | Lo puse **antes** del `HttpLoggingInterceptor`. | Así el log de Logcat muestra la petición ya con el encabezado y puedo comprobar que se envía. Solo cambia `RetrofitClient`; ninguna otra capa se entera. |
| «Agrega un botón Eliminar en el detalle que use `@DELETE("posts/{id}")` y revise el código de respuesta con `Response<Unit>`.» | `borrar()` en `PostApi`, `borrar()` en el Repository, un estado `borrado` en el ViewModel y un botón en `PantallaDetalle`. | El Repository convierte un código no exitoso en `HttpException` dentro de `llamar { }`, así el ViewModel solo recibe `Result`. Agregué un `AlertDialog` de confirmación y, al borrar bien, quito la publicación de la lista local y vuelvo a la lista. | `Response<Unit>` no lanza excepción con un 404, hay que revisar `isSuccessful`. JSONPlaceholder no borra de verdad, por eso se actualiza la lista local. HttpException no debe llegar al ViewModel. |
| «Agrega un campo de búsqueda que filtre por título las publicaciones ya descargadas, sin volver a llamar a la API.» | Filtrar la lista directamente en el Composable con `remember`. | **Descarté** filtrar en la pantalla: lo moví al ViewModel con `combine(_lista, _busqueda)` y `stateIn`, expuesto como `listaVisible`. Agregué el mensaje «Sin resultados para ...». | La pantalla solo debe dibujar el estado. En el ViewModel la búsqueda sobrevive a la rotación y funciona junto con el filtro por usuario. |
| «Haz la interfaz de MuroCampus más llamativa, con colores que combinen, sin cambiar la arquitectura.» | Paleta índigo/violeta con acentos turquesa y coral, encabezado con degradado, avatares de color por usuario, chips de colores y botón flotante extendido. | Quité el color dinámico de Android 12 para que la paleta se vea igual en todos los teléfonos. Usé emojis en lugar de agregar la librería de íconos. Mantuve `TarjetaPost`, `Cargando` y `ErrorConReintento` con las mismas firmas. | Un diseño consistente sin dependencias extra. Al conservar las firmas de los componentes, las pantallas y el ViewModel no tuvieron que cambiar su lógica. |

## Verificación

- El proyecto compila y se instala en el emulador (Pixel 7).
- Lista, detalle con **Eliminar**, búsqueda y crear funcionan con el nuevo diseño.
- `viewmodel/PostsViewModel.kt` no importa Retrofit, PostApi ni HttpException.
