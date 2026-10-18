### Encuentra tu tribu
 
App Android para descubrir eventos e ir acompañado: eliges un evento y la app te arma un grupo con gente afín para ir juntos. **Nadie llega solo.**
 
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android%207.0+-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Material 3](https://img.shields.io/badge/Material%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white)
 
`MVP académico` · `DSY1105 Desarrollo de Aplicaciones Móviles` · `Duoc UC`
 
</div>
---
 
## 📱 Capturas
 
<p align="center">
  <img src="docs/login.png" width="240" alt="Pantalla de inicio" />
  &nbsp;&nbsp;
  <img src="docs/explorar.png" width="240" alt="Explorar eventos" />
</p>
> 💡 Toma las capturas desde el emulador de Android Studio y guárdalas en una carpeta `docs/` con esos nombres.
 
---
 
## ✨ Funcionalidades
 
| | Funcionalidad | Estado |
|---|---|---|
| 🔐 | **Inicio** con usuarios de prueba (modo demo, perfiles ficticios) | ✅ Listo |
| 🧭 | **Explorar eventos** con saludo personalizado | ✅ Listo |
| 🏷️ | **Filtro por categoría**: Todos, Música, Cine, Juegos | ✅ Listo |
| 🖼️ | **Tarjetas de evento** con foto, fecha, organizador, hora y asistentes | ✅ Listo |
| 👥 | **Tribus**: cuántos grupos se están formando por evento y si ya tienes uno | ✅ Listo |
| 🧑‍💼 | **Modo organizador** para publicar eventos | 🔜 Próximamente |
| 🗂️ | Pestañas de **Tribus** y **Perfil** | 🔜 Próximamente |
 
---
 
## 🛠️ Tecnologías
 
- **Kotlin** como lenguaje principal
- **Jetpack Compose** + **Material 3** para toda la interfaz
- **Edge-to-edge** con tema oscuro propio y barras del sistema transparentes
- Tipografías personalizadas: **Nunito** e **Instrument Serif Italic**
- Paleta de colores propia definida en `ui/theme`
- Datos de prueba locales (sin backend por ahora)
---
 
## 📂 Estructura
 
```
app/src/main/java/com/example/mygos/
├── MainActivity.kt      # Punto de entrada: decide entre Login y Explorar
├── LoginScreen.kt       # Pantalla de inicio "Encuentra tu tribu"
├── ExplorarScreen.kt    # Listado de eventos, filtros y navegación inferior
├── Componentes.kt       # Componentes reutilizables (avatares, decoraciones)
├── Datos.kt             # Modelos (Usuario, Evento) y datos de prueba
└── ui/theme/            # Colores, tipografía y tema
```
 
---
 
## 🚀 Cómo ejecutarlo
 
1. Clona el repositorio
```bash
   git clone https://github.com/tomihm/Mygos.git
```
2. Ábrelo en **Android Studio** y espera a que sincronice Gradle.
3. Ejecuta en un emulador o dispositivo con **Android 7.0 (API 24)** o superior.
4. En la pantalla de inicio elige un **usuario de prueba** y entra como asistente.
---
 
## 🗺️ Próximos pasos
 
- [ ] Detalle de evento y unión a una tribu
- [ ] Pantallas de Tribus y Perfil
- [ ] Modo organizador
- [ ] Persistencia local de datos
---
 
## 📸 Créditos
 
Las fotos de ejemplo de los eventos vienen de Wikimedia Commons. El detalle de autores y licencias está en [`CREDITOS_IMAGENES.md`](CREDITOS_IMAGENES.md).
 
---
 
<div align="center">
Hecho por **[Tomás Hernández](https://github.com/tomihm)** · Estudiante de Analista Programador en Duoc UC
 
</div>
 
Claude terminó la respuesta
