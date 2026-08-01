//Esta parte del codigo esta muy comentada con IA para ayudarme a terminar de aclarar 
// los conceptos 

// =====================================================================
// ZONA 1: ÁMBITO GLOBAL (Variables de Estado)
// ---------------------------------------------------------------------
// Estas variables nacen fuera de las funciones. Esto significa que tienen
// "Scope Global": cualquier función del archivo puede leerlas y modificarlas.
// =====================================================================

const bolsasPeliculas = {
    accion: [658, 9461, 1571, 280, 98, 76341, 361743],
    romance: [289, 138, 88, 597, 11036, 313369, 1072790],
    terror: [539, 348, 694, 4232, 176, 138843, 882598],
    comedia: [239, 762, 105, 771, 18785, 293660, 346698],
    cienciaFiccion: [62, 11, 78, 603, 19995, 157336, 438631],
    crimen: [389, 238, 111, 680, 1422, 475557, 661374],
    familiar: [630, 252, 601, 862, 12, 150540, 502356]
};

let barajaFinal = [];       // Guardará los 21 IDs aleatorios (Números)
let barajaPeliculas = [];   // Guardará los 21 expedientes completos (Objetos JSON de la API)

let copiaBolsaPeliculas = [];

let directoresGustados = [];
let directoresOdiados = [];

let aniosGustados = [];
let aniosDescartados = [];

let peliculasGustadas = []; //Guarda las peliculas que le gustan al usuario 
let peliculasNoGustadas = [];

let generosGustados = [];
let generosNoGustados = [];

let duracionPeliculasGustadas = [];

//Indica qué película está viendo el usuario en este momento.
let indiceActual = 0;


// =====================================================================
// ZONA 2: FUNCIONES Y LÓGICA (Los engranajes de la aplicación)
// =====================================================================

/**
 * Recorre el diccionario de géneros y extrae 3 IDs aleatorios de cada uno (Total: 21).
 */
function recorrerBolsas(bolsasPeliculas) {
    copiaBolsaPeliculas = bolsasPeliculas;

    for (const bolsa in bolsasPeliculas) {
        // Medida de seguridad estándar al iterar objetos en JavaScript
        if (!Object.hasOwn(bolsasPeliculas, bolsa)) continue;

        // Sacamos 3 películas por género para que el backend tenga margen de puntuación
        for (let i = 0; i < 3; i++) {
            const rnd = Math.floor(Math.random() * bolsasPeliculas[bolsa].length);
            const idPelicula = bolsasPeliculas[bolsa][rnd];

            barajaFinal.push(idPelicula);

            //Borra la pelicula para que no vuelva a salir 
            bolsasPeliculas[bolsa].splice(rnd, 1);
        }
    }
}

function evaluarPelicula(decision) {
    // EL ESCUDO: Si hacen clic rápido pero la descarga simultánea aún no ha terminado
    if (barajaPeliculas.length === 0 || !barajaPeliculas[indiceActual]) {
        console.warn("Calma, detective. El expediente aún se está descargando...");
        return;
    }

    const pelicula = barajaPeliculas[indiceActual];

    // =====================================
    //                 ACEPTAR
    // =====================================
    if (decision === "aceptar") {
        const crew = pelicula.credits.crew;
        const director = crew.find(persona => persona.job === "Director");

        if (director !== undefined) {
            const directorID = director.id;
            directoresGustados.push(directorID);
            console.log("Director capturado con ID: " + directorID);
        } else {
            console.log("No se encontraron datos del director para esta película.");
        }

        const anioLanzamiento = pelicula.release_date;
        if (anioLanzamiento != undefined) {
            aniosGustados.push(anioLanzamiento.substring(0, 4));
            console.log("Año de lanzamiento: " + anioLanzamiento.substring(0, 4));
        } else {
            console.log("No se encontro el año de lanzamiento");
        }

        const duracionPelicula = pelicula.runtime;
        if (duracionPelicula != undefined) {
            duracionPeliculasGustadas.push(duracionPelicula);
            console.log("duracion de la pelicula: " + duracionPelicula);
        }

        peliculasGustadas.push(pelicula.id);

        pelicula.genres.forEach(genero => {

            generosGustados.push(genero.id);

        });


        console.log("Peliculas gustadas: ", peliculasGustadas);
        console.log("Generos gustados :" + generosGustados);

        // =====================================
        //                 DESCARTAR
        // =====================================
    } else if (decision === "rechazar") {
        const crew = pelicula.credits.crew;
        const director = crew.find(persona => persona.job === "Director");

        if (director !== undefined) {
            const directorID = director.id;
            directoresOdiados.push(directorID); // Lo mandamos a la lista de odiados
            console.log("Director rechazado con ID: " + directorID);
        } else {
            console.log("No se encontraron datos del director para esta película.");
        }

        const anioLanzamiento = pelicula.release_date;
        if (anioLanzamiento != null) {
            aniosDescartados.push(anioLanzamiento.substring(0, 4));
            console.log("Año de lanzamiento descartado: " + anioLanzamiento.substring(0, 4));
        } else {
            console.log("No se encontro el año de lanzamiento");
        }

        peliculasNoGustadas.push(pelicula.id);

        pelicula.genres.forEach(genero => {
            generosNoGustados.push(genero.id);
        });

        console.log("Peliculas no gustadas :", peliculasNoGustadas);
        console.log("Generos no gustados: " + generosNoGustados);
    }

    indiceActual++;
    pintarTarjeta();
}


/**
 * Función asíncrona que hace peticiones HTTP a nuestro propio servidor Java.
 * Descarga los 21 pósteres simultáneamente para evitar errores de red.
 */
async function descargarPosters() {
    try {
        // Preparamos las 21 peticiones a la vez
        const promesas = barajaFinal.map(id =>
            fetch(`http://localhost:8080/api/callahan/expedientes/${id}`)
                .then(res => res.json())
        );

        // Promise.all lanza todas y "congela" el código hasta que todas acaban
        barajaPeliculas = await Promise.all(promesas);
        console.log("Expedientes descargados correctamente de golpe. Baraja lista:", barajaPeliculas);

        // Empezamos a pintar cuando estamos 100% seguros de que todo bajó
        pintarTarjeta();

    } catch (error) {
        console.error("Error crítico al solicitar los expedientes:", error);
    }
}

/**
 * Lee los datos de la película actual y los inyecta en el HTML (DOM).
 * Además, al terminar las 21, empaqueta y envía los datos al backend.
 */
function pintarTarjeta() {
    // 1. Control de flujo: Si llegamos a la 21, cerramos el juego y enviamos a Java
    if (indiceActual === barajaFinal.length) {

        console.log("Procesando datos finales. ID rescatado: ", localStorage.getItem("idUsuario"));

        const paqueDatos = {
            id: parseInt(localStorage.getItem("idUsuario")),
            generosGustados: generosGustados,
            generosNoGustados: generosNoGustados,
            directoresOdiados: directoresOdiados,
            directoresFav: directoresGustados,
            aniosDescartes: aniosDescartados,
            aniosGustados: aniosGustados,
            duracionPeliculasgustadas: duracionPeliculasGustadas,
            plataformasContratadas: JSON.parse(localStorage.getItem("plataformasUsuario"))
        };

        // Enviamos el paquete a Java
        fetch('http://localhost:8080/preferencias/procesamientoDatos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(paqueDatos)
        })
            .then(respuesta => {
                if (respuesta.ok) {
                    console.log("¡Perfil de detective calibrado y guardado con éxito!");
                    window.location.href = "mainPage.html";
                } else {
                    throw new Error("El servidor rechazó el paquete de preferencias.");
                }
            })
            .catch(error => {
                console.error("Fallo de comunicación con la base de datos:", error);
                alert("Ha ocurrido un error al guardar tu perfil. Revisa la consola.");
            });

        // Este return evita intentar pintar una tarjeta que no existe
        return;
    }

    // --- Si aún quedan películas por evaluar, pintamos la siguiente tarjeta ---
    const pelicula = barajaPeliculas[indiceActual];

    const tituloDOM = document.getElementById("titulo-pelicula");
    const textoDOM = document.getElementById("director-pelicula");
    const posterDOM = document.getElementById("poster-pelicula");

    tituloDOM.textContent = pelicula.title;

    // Validamos la fecha para no intentar cortar algo indefinido
    const fecha = pelicula.release_date || "Desconocido";
    textoDOM.textContent = "Año de estreno: " + fecha.substring(0, 4);

    const urlBaseTMDb = "https://image.tmdb.org/t/p/w500";
    // Si no tiene poster, ponemos uno por defecto para que no se rompa la vista
    const rutaImagen = pelicula.poster_path ? urlBaseTMDb + pelicula.poster_path : 'https://via.placeholder.com/500x750/111111/F5F2EB?text=Sin+Pruebas';
    posterDOM.src = rutaImagen;

    posterDOM.classList.add("animacion-entrada");

    setTimeout(() => {
        posterDOM.classList.remove("animacion-entrada");
    }, 500);
}


// =====================================================================
// ZONA 3: EJECUCIÓN (El motor de arranque)
// ---------------------------------------------------------------------
// Aquí es donde realmente arranca el programa cuando el navegador lee 
// el archivo de arriba a abajo.
// =====================================================================

recorrerBolsas(bolsasPeliculas);
descargarPosters();


// =====================================================================
// ZONA 4: BOTONES Y EVENTOS
// =====================================================================

const btnMeGusta = document.getElementById("btn-gustar");
const btnDescartar = document.getElementById("btn-rechazar");
const btnIgnorar = document.getElementById("btn-ignorar");

btnMeGusta.addEventListener("click", () => {
    evaluarPelicula("aceptar");
});

btnIgnorar.addEventListener("click", () => {
    evaluarPelicula("ignorar");
});

btnDescartar.addEventListener("click", () => {
    evaluarPelicula("rechazar");
});