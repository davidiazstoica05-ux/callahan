// =====================================================================
// ZONA 1: ÁMBITO GLOBAL (Variables de Estado)
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

let barajaFinal = [];
let barajaPeliculas = [];
let copiaBolsaPeliculas = [];
let directoresGustados = [];
let directoresOdiados = [];
let aniosGustados = [];
let aniosDescartados = [];
let peliculasGustadas = [];
let peliculasNoGustadas = [];
let generosGustados = [];
let generosNoGustados = [];
let duracionPeliculasGustadas = [];
let indiceActual = 0;

// =====================================================================
// ZONA 2: FUNCIONES Y LÓGICA 
// =====================================================================
function recorrerBolsas(bolsasPeliculas) {
    copiaBolsaPeliculas = bolsasPeliculas;
    for (const bolsa in bolsasPeliculas) {
        if (!Object.hasOwn(bolsasPeliculas, bolsa)) continue;
        for (let i = 0; i < 3; i++) {
            const rnd = Math.floor(Math.random() * bolsasPeliculas[bolsa].length);
            const idPelicula = bolsasPeliculas[bolsa][rnd];
            barajaFinal.push(idPelicula);
            bolsasPeliculas[bolsa].splice(rnd, 1);
        }
    }
}

function evaluarPelicula(decision) {
    if (barajaPeliculas.length === 0 || !barajaPeliculas[indiceActual]) {
        console.warn("Calma, detective. El expediente aún se está descargando...");
        return;
    }

    const pelicula = barajaPeliculas[indiceActual];

    if (decision === "aceptar") {
        const crew = pelicula.credits.crew;
        const director = crew.find(persona => persona.job === "Director");

        if (director !== undefined) directoresGustados.push(director.id);
        if (pelicula.release_date != undefined) aniosGustados.push(pelicula.release_date.substring(0, 4));
        if (pelicula.runtime != undefined) duracionPeliculasGustadas.push(pelicula.runtime);

        peliculasGustadas.push(pelicula.id);
        pelicula.genres.forEach(genero => generosGustados.push(genero.id));

    } else if (decision === "rechazar") {
        const crew = pelicula.credits.crew;
        const director = crew.find(persona => persona.job === "Director");

        if (director !== undefined) directoresOdiados.push(director.id);
        if (pelicula.release_date != null) aniosDescartados.push(pelicula.release_date.substring(0, 4));

        peliculasNoGustadas.push(pelicula.id);
        pelicula.genres.forEach(genero => generosNoGustados.push(genero.id));
    }

    indiceActual++;
    pintarTarjeta();
}

async function descargarPosters() {
    try {
        const promesas = barajaFinal.map(id =>
            fetch(`http://localhost:8080/api/callahan/expedientes/${id}`)
                .then(res => res.json())
        );
        barajaPeliculas = await Promise.all(promesas);
        pintarTarjeta();
    } catch (error) {
        console.error("Error crítico al solicitar los expedientes:", error);
    }
}

function pintarTarjeta() {
    if (indiceActual === barajaFinal.length) {
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

        fetch('http://localhost:8080/preferencias/procesamientoDatos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(paqueDatos)
        })
            .then(respuesta => {
                if (respuesta.ok) {
                    window.location.href = "mainPage.html";
                } else {
                    throw new Error("El servidor rechazó el paquete de preferencias.");
                }
            })
            .catch(error => {
                console.error("Fallo de comunicación con la base de datos:", error);
                alert("Ha ocurrido un error al guardar tu perfil. Revisa la consola.");
            });

        return;
    }

    const pelicula = barajaPeliculas[indiceActual];
    const tituloDOM = document.getElementById("titulo-pelicula");
    const textoDOM = document.getElementById("director-pelicula");
    const posterDOM = document.getElementById("poster-pelicula");

    tituloDOM.textContent = pelicula.title;
    const fecha = pelicula.release_date || "Desconocido";
    textoDOM.textContent = "Año de estreno: " + fecha.substring(0, 4);

    const urlBaseTMDb = "https://image.tmdb.org/t/p/w500";
    const rutaImagen = pelicula.poster_path ? urlBaseTMDb + pelicula.poster_path : 'https://via.placeholder.com/500x750/111111/F5F2EB?text=Sin+Pruebas';

    posterDOM.src = rutaImagen;
    posterDOM.classList.add("animacion-entrada");
    setTimeout(() => {
        posterDOM.classList.remove("animacion-entrada");
    }, 500);
}

// =====================================================================
// ZONA 3: EJECUCIÓN (El motor de arranque)
// =====================================================================
recorrerBolsas(bolsasPeliculas);
descargarPosters();

// =====================================================================
// ZONA 4: BOTONES Y EVENTOS
// =====================================================================
const btnMeGusta = document.getElementById("btn-gustar");
const btnDescartar = document.getElementById("btn-rechazar");
const btnIgnorar = document.getElementById("btn-ignorar");

btnMeGusta.addEventListener("click", () => evaluarPelicula("aceptar"));
btnIgnorar.addEventListener("click", () => evaluarPelicula("ignorar"));
btnDescartar.addEventListener("click", () => evaluarPelicula("rechazar"));