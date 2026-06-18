# Proyecto Callahan / Scottie 🕵️‍♂️🎬
### Sistema Avanzado de Filtrado e Interacción Cinematográfica

---

## 1. El Problema Principal

En la era del streaming, los usuarios se enfrentan a la **paradoja de la elección**. Plataformas como Netflix o HBO tienen catálogos tan inmensos que el usuario medio sufre de "parálisis por análisis", pasando más tiempo navegando por menús infinitos que disfrutando de una película. 

Los sistemas de recomendación actuales suelen estar sesgados por los intereses comerciales de las propias plataformas, atrapando al usuario en bucles de contenido repetitivo.

### ¿Cómo lo resuelve esta aplicación?
**Callahan/Scottie** actúa como un "detective privado" del cine. En lugar de abrumar al usuario, la aplicación:
* Interroga al usuario sobre sus gustos específicos (filtros limpios y directos).
* Rastrea el catálogo global en tiempo real.
* Permite "fichar" películas (guardar interacciones como "Me gusta", "Ver más tarde" o "Descartar") para afinar futuras búsquedas, separando radicalmente el almacenamiento de la sesión de la descarga de datos masivos.

---

## 2. Arquitectura de Datos: Base de Datos vs. API Externa

Para mantener la aplicación ligera, rápida y profesional, se ha optado por una **arquitectura híbrida**. No duplicamos el catálogo de internet en nuestro ordenador; trabajamos "al vuelo".


[ Base de Datos Local ]                 [ API Externa ]
(MySQL / PostgreSQL)                     (TMDB API)
│                                     │
┌───────┴───────┐                             │
│  Usuario (1)  │                             │
└───────┬───────┘                             │
│ (1:N)                               │
┌───────▼───────┐                             │
│Interacción (N)├───[ Long peliculaID ]───────┘
└───────────────┘   (Vínculo lógico / No JPA)

### Relaciones en la Base de Datos Local (JPA)
Nuestra base de datos local solo almacena lo que es estrictamente propiedad de nuestra aplicación: **los usuarios y sus acciones**.

* **Usuario (1) a Intermediario (N):** Un `Usuario` puede realizar muchas interacciones en la app. Desde el punto de vista de la entidad `Interaccion`, la relación es `@ManyToOne` (Muchas interacciones pertenecen a un único usuario).

### La Relación con la API (El Vínculo Ciego)
* **¿Por qué NO hay una entidad `Pelicula` en nuestra BBDD?** Las relaciones de JPA (`@ManyToMany`, `@JoinColumn`, etc.) exigen que ambas tablas existan físicamente en el mismo motor de base de datos. Como las películas viven en los servidores externos de TMDB, intentar mapearlas como entidades locales obligaría a Spring a buscar una tabla inexistente, haciendo colapsar la aplicación al arrancar.
* **La Solución Arquitectónica:** La entidad `Interaccion` guarda un atributo simple: `private Long peliculaID`. Nuestra base de datos es "ciega" respecto a los detalles de la película (título, póster, sinopsis). Solo almacena este número. Cuando el usuario requiera ver su historial, nuestro `@Service` cogerá ese ID y le preguntará a la API los detalles actualizados.

---

## 3. ¿Por qué se ha decidido usar TMDB?

The Movie Database (TMDB) es el estándar de la industria para el desarrollo de software cinematográfico por tres razones clave:

1.  **Consistencia de Datos:** Ofrece un catálogo multimillonario, actualizado al minuto y con soporte multiidioma nativo.
2.  **Rendimiento y Gratuidad:** Su API es extremadamente rápida, no requiere costes de infraestructura para desarrollo y ofrece un sistema de autenticación robusto basado en API Keys.
3.  **Madurez Educativa:** Sus respuestas JSON utilizan estructuras complejas (listas anidadas, nombres en `snake_case`). Esto obliga al desarrollador a implementar patrones de diseño profesionales en Java, como el uso de **DTOs** (*Data Transfer Objects*) y herramientas de deserialización como la librería **Jackson** (`@JsonProperty`), elevando la calidad técnica del proyecto.