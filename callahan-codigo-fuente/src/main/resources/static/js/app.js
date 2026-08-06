

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

        // Le preguntamos al documento entero cuántos están marcados y lo guardamos
        const cantidadMarcados = document.querySelectorAll('.checkbox-emocion:checked').length;


        if (cantidadMarcados === 2) {
            emociones.forEach(caja => {
                if (!caja.checked) {
                    caja.disabled = true;
                }
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
})

const formularioEmociones = document.getElementById('formulario-emociones');


formularioEmociones.addEventListener('submit', (event) => {

    // Frena la recarga del navegador
    event.preventDefault();

    const casillasMarcadas = document.querySelectorAll('input[name="emocion"]:checked');
    const emocionesSeleccionadas = [];
    const anime = document.getElementById("checkbox-anime").checked;


    casillasMarcadas.forEach(caja => {
        emocionesSeleccionadas.push(caja.value);
    });

    console.log("El usuario quiere ver una película con estas emociones:", emocionesSeleccionadas);

    const paqueteDatos = {

        emociones: emocionesSeleccionadas,
        idUsuario: parseInt(localStorage.getItem('idUsuario')),
        anime: anime


    }

    fetch('http://localhost:8080/api/callahan/recomendar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paqueteDatos)
    })
        .then(respuesta => respuesta.json())
        .then(datos => {
            // 1. Capturamos los elementos de la pantalla
            const pantallaForm = document.getElementById("pantalla-interrogatorio");
            const pantallaResultados = document.getElementById("pantalla-resultados");
            const zonaPeliculas = document.getElementById("zona-peliculas");

            // 2. Transición: Ocultamos el formulario y mostramos los resultados
            pantallaForm.classList.add('oculto');
            pantallaResultados.classList.remove('oculto');

            // 3. Limpiamos el contenedor por si había búsquedas anteriores
            zonaPeliculas.innerHTML = '';

            // 4. Recorremos las películas y pintamos el HTML
            datos.forEach(pelicula => {

                // Si la película no tiene póster, ponemos una imagen negra por defecto
                const rutaPoster = pelicula.poster_path
                    ? `https://image.tmdb.org/t/p/w500${pelicula.poster_path}`
                    : 'https://via.placeholder.com/500x750/111111/F5F2EB?text=Sin+Pruebas';

                // Usamos las comillas invertidas (backticks) para inyectar variables en el HTML
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





