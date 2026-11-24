<div align="center">

# mygos

### Encuentra tu tribu
 
App Android para descubrir eventos e ir acompañado: eliges un evento y la app te arma un grupo con gente afín para ir juntos. **Nadie llega solo.**
 
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android%207.0+-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
 
`MVP académico` · `DSY1105 Desarrollo de Aplicaciones Móviles` · `Duoc UC`
 
</div>

## ✨ Funcionalidades
 
| | Funcionalidad | Estado |
|---|---|---|
| 🔐 | **Crear cuenta e iniciar sesión** con validación por campo e íconos | ✅ Listo |
| 🧭 | **Explorar eventos** con filtro por categoría (Todos, Música, Cine, Juegos) | ✅ Listo |
| 👥 | **Tribus**: carrusel animado para elegir grupo (la tarjeta sale volando al unirte o saltar), detalle, unirse (una tribu por evento) y salir | ✅ Listo |
| 🎉 | **¡Ya tienes tribu!** con animación de celebración | ✅ Listo |
| 💬 | **Chat de la tribu** con mensajes en memoria | ✅ Listo |
| 📷 | **Muro de la tribu**: subir fotos con la **cámara** y dar insignias | ✅ Listo |
| 📍 | **Modo encuentro**: distancia al lugar con **GPS**, "Ya llegué" y cómo llegar | ✅ Listo |
| 🧑‍💼 | **Organizador**: publicar evento (con fecha y hora) y ver mis eventos | ✅ Listo |
| 👤 | **Perfil** con foto, resumen y cerrar sesión | ✅ Listo |
| 🖼️ | **Fotos de perfil** ficticias para los usuarios de prueba | ✅ Listo |
| 🌐 | **Foro de la comunidad** desde una **API externa**: ver, abrir con autor y comentarios, publicar, editar y borrar | ✅ Listo |
 
---
 
## 🛠️ Tecnologías
 
- **Kotlin** + **Jetpack Compose** + **Material 3**
- **MVVM**: cada pantalla tiene un `ViewModel` con su estado en `StateFlow`
- **Navigation Compose** con transiciones animadas entre pantallas
- **Room** (con KSP) para guardar las cuentas en el teléfono
- **Cámara**: permiso `CAMERA` en tiempo de ejecución + `FileProvider` + `ActivityResultContracts.TakePicture`
- **GPS**: permisos de ubicación + `FusedLocationProviderClient` (Google Play Services Location)
- **Retrofit** + **Gson** para consumir la API REST del foro, con corrutinas (`suspend`)
- **Pruebas unitarias** con JUnit en `app/src/test`
- Animaciones de Compose: `AnimatedVisibility`, `animateColorAsState`, `Animatable`, `spring`, `rememberInfiniteTransition`, `animateItem`
- Modales: `AlertDialog` (salir de tribu, cerrar sesión), `DatePickerDialog` y selector de hora
- Tipografías **Nunito** e **Instrument Serif Italic**, paleta propia en `ui/theme`
 
---
 
## 📂 Estructura
 
```
app/src/main/java/com/example/mygos/
├── MainActivity.kt, MygosApp.kt   # Entrada de la app; MygosApp crea la base de datos
├── Datos.kt                       # Modelos: Usuario, Cuenta, Evento, Tribu, Publicacion, Mensaje
├── data/                          # Room (entidad, DAO, base) y datos de ejemplo
├── remote/ForoApi.kt              # Retrofit: endpoints, modelos del JSON y cliente
├── repository/                    # Fuente de datos de cada tema (cuentas, eventos, tribus, chat, muro, foro)
├── validacion/Validaciones.kt     # Todas las reglas de los formularios, sin UI
├── viewmodel/                     # Un ViewModel por pantalla, con su estado
├── navegacion/Navegacion.kt       # Rutas, NavHost y guardia de sesión
└── ui/
    ├── componentes/               # Piezas reutilizables: botones, campos, cabecera, barra inferior
    ├── cuenta/                    # Bienvenida, iniciar sesión, crear cuenta
    ├── eventos/                   # Explorar, detalle de evento, publicar, mis eventos
    ├── tribus/                    # Carrusel, detalle, ya tienes tribu, mis tribus, chat, muro, encuentro
    ├── perfil/                    # Perfil
    ├── foro/                      # Foro de la comunidad (API externa)
    └── theme/                     # Colores, tipografía y tema
```
 
Cada pantalla tiene dos funciones: `XRuta` conecta el ViewModel y `XScreen` solo dibuja (por eso tiene preview).
 
---
 
## 🧾 Rúbrica → dónde está
 
| Indicador | Dónde mirar |
|---|---|
| 2.1.1 Interfaz y navegación | `navegacion/Navegacion.kt`, `ui/` |
| 2.1.2 Formularios con validación visual | `ui/cuenta/CuentaScreen.kt`, `ui/eventos/PublicarEventoScreen.kt`, `CampoTexto` y `CampoSelector` en `ui/componentes` |
| 2.2.1 Validación desacoplada | `validacion/Validaciones.kt` + ViewModels de formularios |
| 2.2.2 Animaciones | tarjeta del carrusel en `ui/tribus/TribusScreen.kt`, celebración en `TribuScreen.kt`, transiciones en `Navegacion.kt`, sacudida en `CampoTexto`, error del login en `CuentaScreen.kt`, pulso en `EncuentroScreen.kt`, `animateItem` en chat |
| 2.3.1 Modularidad y persistencia | paquetes de arriba + Room en `data/` |
| API externa (7 endpoints) | `remote/ForoApi.kt`, `repository/ForoRepository.kt`, `viewmodel/ForoViewModel.kt`, `ui/foro/ForoScreen.kt` |
| Pruebas unitarias | `app/src/test/java/com/example/mygos/` |
| APK firmado e ícono | `signingConfigs` en `app/build.gradle.kts`, `res/drawable/ic_launcher_*.xml` |
| 2.4.1 Recursos del dispositivo | cámara en `ui/tribus/MuroScreen.kt`, GPS en `ui/tribus/EncuentroScreen.kt`, permisos en `AndroidManifest.xml` |
 
---
 
## 🌐 API externa

El foro usa [JSONPlaceholder](https://jsonplaceholder.typicode.com), una API REST pública de prueba. Responde como si guardara los cambios, pero no los guarda: lo que se publica o edita queda solo en el teléfono mientras la pantalla está abierta.

| # | Método | Endpoint | Uso en la app |
|---|---|---|---|
| 1 | GET | `/posts` | Lista del foro (primeras 20) |
| 2 | GET | `/posts/{id}` | Abrir una publicación |
| 3 | GET | `/posts/{id}/comments` | Comentarios de la publicación |
| 4 | GET | `/users/{id}` | Nombre del autor |
| 5 | POST | `/posts` | Publicar |
| 6 | PUT | `/posts/{id}` | Guardar cambios |
| 7 | DELETE | `/posts/{id}` | Borrar |

Capas: `ForoScreen` (UI) → `ForoViewModel` (estado, validación y errores de conexión) → `ForoRepository` → `ForoApi` (Retrofit).

---

## 🧪 Pruebas unitarias

```bash
./gradlew testDebugUnitTest
```

- `ValidacionesTest`: correo, clave, fecha y título de publicación.
- `ForoRepositoryTest`: usa una API falsa (sin internet) para revisar que la lista se corta en 20 y que al publicar se envía el texto sin espacios sobrantes.

---

## 📦 APK firmado

El APK release se firma con `mygos-release.jks`. El keystore y `keystore.properties` (rutas y claves) no se suben al repositorio.

```bash
./gradlew assembleRelease
# queda en app/build/outputs/apk/release/app-release.apk
```

El ícono es propio: los dos anillos de la bienvenida (amarillo y naranja) sobre el fondo oscuro de la app (`res/drawable/ic_launcher_*.xml`).

---

## 🚀 Cómo ejecutarlo
 
1. Clona el repositorio
```bash
   git clone https://github.com/tomihm/Mygos.git
```
2. Ábrelo en **Android Studio** y espera a que sincronice Gradle.
3. Ejecuta en un emulador o dispositivo con **Android 7.0 (API 24)** o superior.
4. Entra con una cuenta de prueba (`vale@mygos.cl`, `nico@mygos.cl` o `cami@mygos.cl`, clave `1234`) o crea una.
5. Para probar el modo encuentro en el emulador: **Extended controls → Location**, fija un punto cerca de Santiago centro.
 
---
 
## 🗺️ Próximos pasos
 
- [ ] Guardar eventos, tribus, mensajes y fotos en Room (hoy viven en memoria)
- [ ] Ubicación real del evento al publicarlo
- [ ] Ordenar eventos por fecha
- [ ] Mapa dentro del modo encuentro
---
 
## 📸 Créditos
 
Las fotos de ejemplo de los eventos vienen de Wikimedia Commons. El detalle de autores y licencias está en [`CREDITOS_IMAGENES.md`](CREDITOS_IMAGENES.md).
 
---
 
<div align="center">
Hecho por **[Tomás Hernández](https://github.com/tomihm)** · Estudiante de Analista Programador en Duoc UC
 
</div>
