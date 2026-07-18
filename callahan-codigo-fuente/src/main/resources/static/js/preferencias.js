
//Esta parte del codigo esta muy comentada con IA para ayudarme a terminar de aclarar 
// los conceptos 

// =====================================================================
// ZONA 1: ÁMBITO GLOBAL (Variables de Estado)
// ---------------------------------------------------------------------
// Estas variables nacen fuera de las funciones. Esto significa que tienen
// "Scope Global": cualquier función del archivo puede leerlas y modificarlas.
// =====================================================================

const bolsasPeliculas = {
    accion: [155, 1566, 98, 76341, 245891],
    romance: [597, 11036, 313369, 114, 4584],
    terror: [348, 694, 539, 11324, 346364],
    comedia: [18785, 854, 771, 4247, 105],
    cienciaFiccion: [603, 157336, 27205, 11, 808],
    crimen: [238, 278, 680, 807, 550],
    familiar: [862, 858, 12, 14160, 129]
};

let barajaFinal = [];      // Guardará los 7 IDs aleatorios (Números)
let barajaPeliculas = [];  // Guardará los 7 expedientes completos (Objetos JSON de la API)


let copiaBolsaPeliculas = [];

let directoresGustados = [];
let directoresOdiados = [];

let aniosGustados = []; 
let aniosDescartados = []

let peliculasGustadas = []; //Guarda las peliculas que le gustán al usuario 
let peliculasNoGUstadas = [];

//Indica qué película está viendo el usuario en este momento.
let indiceActual = 0; 


// =====================================================================
// ZONA 2: FUNCIONES Y LÓGICA (Los engranajes de la aplicación)
// =====================================================================

/**
 * Recorre el diccionario de géneros y extrae un ID aleatorio de cada uno.
 */
function recorrerBolsas(bolsasPeliculas) {

    copiaBolsaPeliculas = bolsasPeliculas;

    for (const bolsa in bolsasPeliculas) {
        

        // Medida de seguridad estándar al iterar objetos en JavaScript
        if (!Object.hasOwn(bolsasPeliculas, bolsa)) continue;

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

    const pelicula = barajaPeliculas[indiceActual]; 


    //Aceptar
    if (decision === "aceptar") {
    
    const crew = pelicula.credits.crew;
    
    const director = crew.find(persona => persona.job === "Director"); 

    if (director !== undefined) {

        const directorID = director.id;

        directoresGustados.push(directorID);

        console.log("Director capturado con ID: " + directorID);

    }else {

        console.log("No se encontraron datos del director para esta película.");

    }

    const anioLanzamiento = pelicula.release_date; 

    if (anioLanzamiento != undefined) {
        

        aniosGustados.push(anioLanzamiento.substring(0, 4)); 
        console.log("Año de lanzamiento: " + anioLanzamiento.substring(0,4)); 
        

    } else {

        console.log( "No se encontro el año de lanzamiento");


    }

    peliculasGustadas.push(pelicula.id);

    console.log("Peliculas gustadas: ", peliculasGustadas);

    //======================================
    //                 DESCARTAR
    // =====================================
    
    } else if ( decision === "rechazar"){

    const crew = pelicula.credits.crew;
    
    const director = crew.find(persona => persona.job === "Director"); 

    if (director !== undefined) {

        const directorID = director.id;

        directoresGustados.push(directorID);

        console.log("Director capturado con ID: " + directorID);

    }else {

        console.log("No se encontraron datos del director para esta película.");

    }

    const anioLanzamiento = pelicula.release_date;

    if(anioLanzamiento != null ){


        aniosDescartados.push(anioLanzamiento.substring(0,4));

      console.log("Año de lanzamiento: " + anioLanzamiento.substring(0,4)); 
        

    } else {

        console.log( "No se encontro el año de lanzamiento");


    }
    peliculasNoGUstadas.push(pelicula.id);
    
    console.log("no gustadas :", peliculasNoGUstadas);
  

    } 

    indiceActual ++; 
    pintarTarjeta();

}


/**
 * Función asíncrona que hace peticiones HTTP a nuestro propio servidor Java.
 * Usamos async/await para obligar a JS a esperar a que lleguen los datos.
 */
async function descargarPosters() {
    
    // Iteramos sobre los 7 números que acabamos de generar
    for (const id of barajaFinal) {
    
        // Conectamos con la pasarela segura de nuestro controlador en Spring Boot
        const urlCompleta = `http://localhost:8080/api/callahan/expedientes/${id}`;
        
        // Bloque try...catch: Evita que la aplicación colapse si un enlace falla
        try {
            // await: Detiene el bucle aquí hasta que Java nos devuelva el paquete
            const response = await fetch(urlCompleta);
            
            // Convertimos el paquete de texto (JSON) a un Objeto real de JavaScript
            const datos = await response.json();
            
            // Metemos el objeto completo en nuestra baraja global
            barajaPeliculas.push(datos);
            
        } catch (error) {
            console.error(`Error crítico al solicitar el expediente ${id}:`, error);
        }
    } 

    console.log("Expedientes descargados correctamente. Baraja lista:", barajaPeliculas);
    
    // CRÍTICO: Llamamos a pintarTarjeta() AQUÍ DENTRO.
    // Solo así garantizamos que las tarjetas se pinten DESPUÉS de que las 
    // películas hayan terminado de descargarse en el array.
    pintarTarjeta();
}

/**
 * Lee los datos de la película actual y los inyecta en el HTML (DOM).
 */
function pintarTarjeta() {
   
    // 1. Control de flujo (Condición de salida)
    // Si el índice alcanza el tamaño del array, ya no hay más películas.
    if (indiceActual === barajaPeliculas.length) {
        window.location.href = "mainPage.html";
        return; // Detiene la ejecución de esta función inmediatamente
    }

    // Extraemos la película actual basándonos en el contador global
    const pelicula = barajaPeliculas[indiceActual];
    
    // 2. Capturamos los elementos visuales del HTML (El DOM)
    const tituloDOM = document.getElementById("titulo-pelicula"); 
    const textoDOM = document.getElementById("director-pelicula");
    const posterDOM = document.getElementById("poster-pelicula");

    // 3. Inyectamos la información en los elementos capturados
    tituloDOM.textContent = pelicula.title;
    
    // Extraemos solo los primeros 4 caracteres (YYYY) de la fecha "YYYY-MM-DD"
    textoDOM.textContent = "Año de estreno: " + pelicula.release_date.substring(0, 4);

    // Concatenamos la URL base oficial de TMDb con la ruta específica de esta imagen
    const urlBaseTMDb = "https://image.tmdb.org/t/p/w500";
    posterDOM.src = urlBaseTMDb + pelicula.poster_path;

    // 4. Manipulación de CSS mediante clases de JavaScript
    posterDOM.classList.add("animacion-entrada");
    
    // setTimeout: Un temporizador que elimina la clase de animación tras 500ms
    // Esto deja la imagen "limpia" y preparada para cuando toque animar la siguiente
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


//zona 4 

const btnMeGusta = document.getElementById("btn-gustar"); 
const btnDescartar = document.getElementById("btn-rechazar");
const btnIgnorar = document.getElementById("btn-ignorar");

btnMeGusta.addEventListener("click", () => {
    evaluarPelicula("aceptar");
});

btnIgnorar.addEventListener("click", () => {

evaluarPelicula("ignorar");




})


btnDescartar.addEventListener("click", () => {
    evaluarPelicula("rechazar");
});

//Procesamiento de datos 
//Envio de los datos 

//Creamos el paquete de datos: 

const paqueDatos = {
    id : localStorage.getItem("idUsuario"),
    peliculasGustadas : peliculasGustadas, 
    peliculasNoGustadas : peliculasNoGUstadas,
    directoresOdiados : directoresOdiados,
    directoresFav : directoresGustados,      
    aniosDescartes : aniosDescartados, 
    aniosGustados : aniosGustados
}

//Lo mandamos al controlador 

fetch('http://localhost:8080/preferencias/procesamientoDatos',{

    method: 'POST',
    headers:{
        

        'Content Type': 'application/JSON'
    }, 
    body: JSON.stringify(paqueDatos)

})


