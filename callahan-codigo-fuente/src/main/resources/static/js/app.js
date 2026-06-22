

const pantallaInicio = document.getElementById("pantalla-inicio");

const pantallaForm = document.getElementById("pantalla-interrogatorio"); 

const trigger = document.getElementById("btn-interrogatorio");


trigger.addEventListener('click', () => {
    pantallaInicio.classList.add('oculto');
    pantallaForm.classList.remove('oculto');
});


const emociones = document.querySelectorAll('.checkbox-emocion'); 

emociones.forEach( emocion => {
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
