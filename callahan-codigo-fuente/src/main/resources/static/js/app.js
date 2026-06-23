

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


const formularioEmociones = document.getElementById('formulario-emociones');


formularioEmociones.addEventListener('submit', (event) => {

    //Frena la recarga del navegador
    event.preventDefault();

    const casillasMarcadas = document.querySelectorAll('.checkbox-emocion:checked');

    const emocionesSeleccionadas = [];

    casillasMarcadas.forEach(caja => {
        emocionesSeleccionadas.push(caja.value);
    });

    console.log("El usuario quiere ver una película con estas emociones:", emocionesSeleccionadas);

    const paqueteDatos = { emociones: emocionesSeleccionadas };

    console.log(paqueteDatos);

    fetch('http://localhost:8080/api/callahan/recomendar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(paqueteDatos)
    })
        .then(respuesta => respuesta.json())
        .then(datos => console.log("Respuesta de Java:", datos))
        .catch(error => console.error("Error en el envío:", error));

})



