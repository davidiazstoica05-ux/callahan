# Callahan 
> *"Sé lo que estás pensando: ¿ha filtrado seis géneros o solo cinco?"*

Callahan es un motor inteligente de recomendación cinematográfica diseñado para huir del *scroll infinito*. A diferencia de las plataformas tradicionales, Callahan somete al usuario a un interrogatorio de estado anímico y cruza los resultados con un expediente estricto de filias y fobias personales, entregando únicamente las películas exactas para el momento preciso. Todo envuelto en una estética brutalista inspirada en el diseño gráfico de Saul Bass.

## Stack Tecnológico

**Back-End:**
* **Java 17 & Spring Boot 3:** Núcleo de la API REST.
* **Spring Security:** Gestión de autenticación por sesiones (Stateful), encriptación de contraseñas (BCrypt) y protección de rutas.
* **Spring Data JPA & Hibernate:** ORM para el modelado relacional.
* **Jakarta Validation:** Validación estricta de datos en el servidor (`@Valid`, `@AssertTrue`).

**Base de Datos:**
* **H2 Database:** Base de datos relacional en memoria para el entorno de desarrollo y persistencia local (`DB_CLOSE_ON_EXIT=FALSE`).

**Front-End:**
* **HTML5 & CSS3:** Arquitectura de estilos vanilla con variables globales CSS, diseño responsivo y animaciones CSS nativas.
* **Vanilla JavaScript:** Consumo de API mediante Fetch, manejo del DOM, almacenamiento en `localStorage` y validación nativa (API `setCustomValidity`).

**Integraciones Externas:**
* **TMDb API (The Movie Database):** Extracción de metadatos, pósteres, directores y géneros en tiempo real mediante `RestTemplate`.

##  Características Principales (v0.1)

* **Calibración del Algoritmo (Estilo Tinder):** Durante el primer inicio de sesión, el usuario evalúa una baraja de 21 películas (Aceptar, Rechazar o Ignorar) para construir su expediente base de directores odiados/amados, géneros afines, época favorita y tolerancia a la duración.
* **El Interrogatorio (Filtro Emocional):** Sistema de recomendación basado en intenciones diarias (ej. *Desconectar y reír*, *Tensión y misterio*, *Llorar a mares*).
* **Motor de Puntuación:** Algoritmo propio que asigna puntuaciones a cada película basándose en coincidencias de época (+10 pts), director (+15 pts), límite de duración y presencia de géneros favoritos, aplicando "guillotinas" directas si aparece un director odiado.
* **El Doble Escudo de Validación:**
  * **Capa Front-End:** Bloqueo visual e interceptación de formularios antes del envío mediante atributos HTML5 y JS.
  * **Capa Back-End:** Interceptador `GlobalExceptionHandler` que captura excepciones de Spring Validation y devuelve un JSON estructurado y amigable.
* **Seguridad y Control de Acceso:** Redirección automática de intrusos a una página personalizada de *Acceso Denegado (403)* y cierre de sesión seguro con destrucción de *cookies* e invalidación de caché local.

##  Decisiones de Arquitectura

1. **Desacoplamiento Front/Back:** Aunque es un proyecto monolítico de Spring Boot, el frontend es completamente estático (HTML/JS/CSS puro) y se comunica con Spring exclusivamente mediante endpoints REST y JSON. 
2. **Cierre de Sesión Transparente:** La aplicación utiliza el *Back/Forward Cache* del navegador a su favor; un script centinela evalúa el `localStorage` en la pantalla de inicio y fuerza la destrucción silenciosa de la sesión HTTP en Spring si detecta anomalías.
3. **DataSeed Automático:** El proyecto inyecta perfiles de usuario preconfigurados al arrancar (desde cinéfilos clásicos hasta fanáticos del anime y adictos a la adrenalina) para testear rápidamente las fisuras matemáticas del algoritmo.

##  Instalación y Despliegue

1. Clona el repositorio.
2. Obtén una API Key gratuita en [The Movie Database (TMDb)](https://www.themoviedb.org/).
3. Configura tu clave en el archivo `application.properties`:
   ```properties
   tmdb.api.key=TU_API_KEY_AQUI
   ```
4. Ejecuta el proyecto mediante Maven:
   ```
   ./mvnw spring-boot:run
   ```
5. Accede a la interfaz web en http://localhost:8080.
   
##  RoadMap(Proximamente en V1.0)
* Precisión Temática Estricta: Reemplazo del método permisivo .anyMatch() por una regla de coincidencia porcentual o de género principal para evitar que dramas con ligeros toques cómicos se filtren en la categoría de comedias puras.
* Bloqueo por Contaminación de Géneros: Penalización matemática severa para películas que, aunque encajen con la búsqueda, contengan etiquetas secundarias cruzadas indeseables (ej. buscar comedia y recibir thrillers de humor negro).
* Calibración Cero Fricciones: Evolucionar el onboarding actual (evaluación obligatoria de 21 expedientes) hacia un sistema de perfilado más ágil y directo. El objetivo es mantener la misma base matemática en el perfil del usuario, pero reduciendo drásticamente la fatiga inicial de los nuevos detectives y consiguiendo mejores datos de los usuarios.
