const pantallaInicio = document.getElementById("pantalla-inicio");
const pantallaForm = document.getElementById("pantalla-interrogatorio");
const trigger = document.getElementById("btn-interrogatorio");

trigger.addEventListener('click', () => {
    pantallaInicio.classList.add('oculto');
    pantallaForm.classList.remove('oculto');
});

const emociones = document.querySelectorAll('.checkbox-emocion');
emociones.forEach(emocion => {
    emocion.addEventListener('change', () => {
        const cantidadMarcados = document.querySelectorAll('.checkbox-emocion:checked').length;
        if (cantidadMarcados === 2) {
            emociones.forEach(caja => {
                if (!caja.checked) caja.disabled = true;
            });
        } else {
            emociones.forEach(caja => {
                caja.disabled = false;
            });
        }
    });
});

const btnVolver = document.getElementById("btn-volver");
btnVolver.addEventListener('click', (event) => {
    const pantallaResultados = document.getElementById("pantalla-resultados");
    const pantallaForm = document.getElementById("pantalla-interrogatorio");

    pantallaResultados.classList.add('oculto');
    pantallaForm.classList.remove('oculto');

    const emociones = document.querySelectorAll('.checkbox-emocion');
    emociones.forEach(caja => {
        caja.checked = false;
        caja.disabled = false;
    });
    document.getElementById("zona-peliculas").innerHTML = '';
});

const formularioEmociones = document.getElementById('formulario-emociones');
formularioEmociones.addEventListener('submit', (event) => {
    event.preventDefault();
    const casillasMarcadas = document.querySelectorAll('input[name="emocion"]:checked');
    const emocionesSeleccionadas = [];
    const anime = document.getElementById("checkbox-anime").checked;

    casillasMarcadas.forEach(caja => {
        emocionesSeleccionadas.push(caja.value);
    });

    const paqueteDatos = {
        emociones: emocionesSeleccionadas,
        idUsuario: parseInt(localStorage.getItem('idUsuario')),
        anime: anime
    };

    fetch('http://localhost:8080/api/callahan/recomendar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paqueteDatos)
    })
        .then(respuesta => respuesta.json())
        .then(datos => {
            const pantallaForm = document.getElementById("pantalla-interrogatorio");
            const pantallaResultados = document.getElementById("pantalla-resultados");
            const zonaPeliculas = document.getElementById("zona-peliculas");

            pantallaForm.classList.add('oculto');
            pantallaResultados.classList.remove('oculto');
            zonaPeliculas.innerHTML = '';

            datos.forEach(pelicula => {
                const rutaPoster = pelicula.poster_path
                    ? `https://image.tmdb.org/t/p/w500${pelicula.poster_path}`
                    : 'https://via.placeholder.com/500x750/111111/F5F2EB?text=Sin+Pruebas';

                zonaPeliculas.innerHTML += `
                <article class="tarjeta-pelicula">
                    <img class="poster-pelicula" src="${rutaPoster}" alt="Póster de ${pelicula.title}">
                    <h3 class="titulo-pelicula">${pelicula.title}</h3>
                    <div class="nota-pelicula">⭐ ${pelicula.vote_average.toFixed(1)} / 10</div>
                    <p class="sinopsis-pelicula">${pelicula.overview || "El expediente de esta película está clasificado (Sin sinopsis)."}</p>
                </article>
            `;
            });
        })
        .catch(error => console.error("Error en el envío:", error));
});

// ==========================================
// PROTOCOLO DE ABANDONO (Logout)
// ==========================================
const btnCerrarSesion = document.getElementById("btn-cerrar-sesion");
if (btnCerrarSesion) {
    btnCerrarSesion.addEventListener("click", () => {
        fetch('http://localhost:8080/api/detectives/cerrarSesion', {
            method: 'POST'
        })
            .then(respuesta => {
                if (respuesta.ok) {
                    localStorage.removeItem("idUsuario");
                    localStorage.removeItem("plataformasUsuario");
                    window.location.href = "index.html";
                } else {
                    console.error("El servidor se negó a cerrar la sesión.");
                }
            })
            .catch(error => {
                console.error("Error de comunicación al intentar desconectar:", error);
            });
    });
}