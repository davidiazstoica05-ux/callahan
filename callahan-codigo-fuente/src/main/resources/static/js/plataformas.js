// --- plataformas.js ---

// --- plataformas.js ---
const formularioPlataformas = document.getElementById('formulario-plataformas');

formularioPlataformas.addEventListener('submit', (event) => {
    event.preventDefault();
    const casillasMarcadas = document.querySelectorAll('input[name="plataforma"]:checked');
    const plataformasSeleccionadas = [];

    casillasMarcadas.forEach(caja => {
        plataformasSeleccionadas.push(caja.value);
    });

    localStorage.setItem("plataformasUsuario", JSON.stringify(plataformasSeleccionadas));
    window.location.href = "preferencias.html";
});