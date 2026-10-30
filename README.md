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
 
---
 
## 🛠️ Tecnologías
 
- **Kotlin** + **Jetpack Compose** + **Material 3**
- **MVVM**: cada pantalla tiene un `ViewModel` con su estado en `StateFlow`
- **Navigation Compose** con transiciones animadas entre pantallas
- **Room** (con KSP) para guardar las cuentas en el teléfono
- **Cámara**: permiso `CAMERA` en tiempo de ejecución + `FileProvider` + `ActivityResultContracts.TakePicture`
- **GPS**: permisos de ubicación + `FusedLocationProviderClient` (Google Play Services Location)
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
├── repository/                    # Fuente de datos de cada tema (cuentas, eventos, tribus, chat, muro)
├── validacion/Validaciones.kt     # Todas las reglas de los formularios, sin UI
├── viewmodel/                     # Un ViewModel por pantalla, con su estado
├── navegacion/Navegacion.kt       # Rutas, NavHost y guardia de sesión
└── ui/
    ├── componentes/               # Piezas reutilizables: botones, campos, cabecera, barra inferior
    ├── cuenta/                    # Bienvenida, iniciar sesión, crear cuenta
    ├── eventos/                   # Explorar, detalle de evento, publicar, mis eventos
    ├── tribus/                    # Carrusel, detalle, ya tienes tribu, mis tribus, chat, muro, encuentro
    ├── perfil/                    # Perfil
    └── theme/                     # Colores, tipografía y tema
```
 
Cada pantalla tiene dos funciones: `XRuta` conecta el ViewModel y `XScreen` solo dibuja (por eso tiene preview).
 
---
 
## 🧾 Rúbrica EP2 → dónde está
 
| Indicador | Dónde mirar |
|---|---|
| 2.1.1 Interfaz y navegación | `navegacion/Navegacion.kt`, `ui/` |
| 2.1.2 Formularios con validación visual | `ui/cuenta/CuentaScreen.kt`, `ui/eventos/PublicarEventoScreen.kt`, `CampoTexto` y `CampoSelector` en `ui/componentes` |
| 2.2.1 Validación desacoplada | `validacion/Validaciones.kt` + ViewModels de formularios |
| 2.2.2 Animaciones | tarjeta del carrusel en `ui/tribus/TribusScreen.kt`, celebración en `TribuScreen.kt`, transiciones en `Navegacion.kt`, sacudida en `CampoTexto`, error del login en `CuentaScreen.kt`, pulso en `EncuentroScreen.kt`, `animateItem` en chat |
| 2.3.1 Modularidad y persistencia | paquetes de arriba + Room en `data/` |
| 2.4.1 Recursos del dispositivo | cámara en `ui/tribus/MuroScreen.kt`, GPS en `ui/tribus/EncuentroScreen.kt`, permisos en `AndroidManifest.xml` |
 
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
