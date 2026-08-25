
//Uso de IA para ayudarme a organizar el codigo mas limpio 

// ==========================================
// 1. GESTIÓN DE MODALES (Login y Registro)
// ==========================================


// Capturamos los elementos del Registro
const btnRegistro = document.querySelector('.btn-principal');
const modalRegistro = document.getElementById('modal-registro');
const btnCerrarRegistro = document.getElementById('cerrar-registro');

// Capturamos los elementos del Login
const btnLogin = document.getElementById('btn-iniciar-sesion');
const modalLogin = document.getElementById('modal-login');
const btnCerrarLogin = document.getElementById('cerrar-login');

// Capturamos el fondo oscuro (Común para ambos)
const overlay = document.getElementById('modal-overlay');

// --- EVENTOS DE APERTURA ---

btnRegistro.addEventListener('click', () => {
    overlay.classList.remove('oculto');
    modalRegistro.classList.remove('oculto');
});

btnLogin.addEventListener('click', () => {
    overlay.classList.remove('oculto');
    modalLogin.classList.remove('oculto');
});

// --- EVENTOS DE CIERRE ---

// Función unificada para limpiar la pantalla
function cerrarModales() {
    overlay.classList.add('oculto');
    modalRegistro.classList.add('oculto');
    modalLogin.classList.add('oculto');
}

btnCerrarRegistro.addEventListener('click', cerrarModales);
btnCerrarLogin.addEventListener('click', cerrarModales);
overlay.addEventListener('click', cerrarModales);


// ==========================================
// 2. CARGA DE RECURSOS (Lista de Países)
// ==========================================

const datalistPaises = document.getElementById('lista-paises');

const paises = [
    "Afganistán", "Albania", "Alemania", "Andorra", "Angola",
    "Antigua y Barbuda", "Arabia Saudita", "Argelia", "Argentina", "Armenia",
    "Australia", "Austria", "Azerbaiyán", "Bahamas", "Bangladés",
    "Barbados", "Baréin", "Bélgica", "Belice", "Benín",
    "Bielorrusia", "Birmania", "Bolivia", "Bosnia y Herzegovina", "Botsuana",
    "Brasil", "Brunéi", "Bulgaria", "Burkina Faso", "Burundi",
    "Bután", "Cabo Verde", "Camboya", "Camerún", "Canadá",
    "Catar", "Chad", "Chile", "China", "Chipre",
    "Ciudad del Vaticano", "Colombia", "Comoras", "Corea del Norte", "Corea del Sur",
    "Costa de Marfil", "Costa Rica", "Croacia", "Cuba", "Dinamarca",
    "Dominica", "Ecuador", "Egipto", "El Salvador", "Emiratos Árabes Unidos",
    "Eritrea", "Eslovaquia", "Eslovenia", "España", "Estados Unidos",
    "Estonia", "Etiopía", "Filipinas", "Finlandia", "Fiyi",
    "Francia", "Gabón", "Gambia", "Georgia", "Ghana",
    "Granada", "Grecia", "Guatemala", "Guinea", "Guinea Ecuatorial",
    "Guinea-Bisáu", "Guyana", "Haití", "Honduras", "Hungría",
    "India", "Indonesia", "Irak", "Irán", "Irlanda",
    "Islandia", "Islas Marshall", "Islas Salomón", "Israel", "Italia",
    "Jamaica", "Japón", "Jordania", "Kazajistán", "Kenia",
    "Kirguistán", "Kiribati", "Kuwait", "Laos", "Lesoto",
    "Letonia", "Líbano", "Liberia", "Libia", "Liechtenstein",
    "Lituania", "Luxemburgo", "Madagascar", "Malasia", "Malaui",
    "Maldivas", "Malí", "Malta", "Marruecos", "Mauricio",
    "Mauritania", "México", "Micronesia", "Moldavia", "Mónaco",
    "Mongolia", "Montenegro", "Mozambique", "Namibia", "Nauru",
    "Nepal", "Nicaragua", "Níger", "Nigeria", "Noruega",
    "Nueva Zelanda", "Omán", "Países Bajos", "Pakistán", "Palaos",
    "Palestina", "Panamá", "Papúa Nueva Guinea", "Paraguay", "Perú",
    "Polonia", "Portugal", "Reino Unido", "República Centroafricana", "República Checa",
    "República del Congo", "República Democrática del Congo", "República Dominicana", "Ruanda", "Rumania",
    "Rusia", "Samoa", "San Cristóbal y Nieves", "San Marino", "San Vicente y las Granadinas",
    "Santa Lucía", "Santo Tomé y Príncipe", "Senegal", "Serbia", "Seychelles",
    "Sierra Leona", "Singapur", "Siria", "Somalia", "Sri Lanka",
    "Suazilandia", "Sudáfrica", "Sudán", "Sudán del Sur", "Suecia",
    "Suiza", "Surinam", "Tailandia", "Tanzania", "Tayikistán",
    "Timor Oriental", "Togo", "Tonga", "Trinidad y Tobago", "Túnez",
    "Turkmenistán", "Turquía", "Tuvalu", "Ucrania", "Uganda",
    "Uruguay", "Uzbekistán", "Vanuatu", "Venezuela", "Vietnam",
    "Yemen", "Yibuti", "Zambia", "Zimbabue"
];

paises.forEach(pais => {
    const nuevaOpcion = document.createElement('option');
    nuevaOpcion.value = pais;
    nuevaOpcion.textContent = pais;
    datalistPaises.appendChild(nuevaOpcion);
});


// ==========================================
// 3. GESTIÓN DEL FORMULARIO DE REGISTRO
// ==========================================



const formRegistro = document.getElementById("form-registro");
const inputPais = document.getElementById('reg-pais');


const inputFecha = document.getElementById('reg-fecha');

// Calculamos los límites de edad (Entre 10 y 100 años)
const hoy = new Date();

const fechaMax = new Date(hoy.getFullYear() - 10, hoy.getMonth(), hoy.getDate());
inputFecha.max = fechaMax.toISOString().split("T")[0]; // Formato YYYY-MM-DD

const fechaMin = new Date(hoy.getFullYear() - 100, hoy.getMonth(), hoy.getDate());
inputFecha.min = fechaMin.toISOString().split("T")[0]; // Formato YYYY-MM-DD


inputPais.addEventListener('input', () => {
    if (!paises.includes(inputPais.value.trim())) {
        inputPais.setCustomValidity("Debes seleccionar un país válido de la lista.");
    } else {
        // Libera el formulario
        inputPais.setCustomValidity("");
    }
});

formRegistro.addEventListener('submit', (event) => {
    event.preventDefault();

    // Recolectamos los datos limpios
    const nuevoDetective = {
        nombreUsuario: document.getElementById('reg-usuario').value.trim(),
        email: document.getElementById('reg-email').value.trim(),
        passw: document.getElementById('reg-password').value,
        apellido1: document.getElementById('reg-apellido').value.trim(),
        nombreReal: document.getElementById('reg-nombre').value.trim(),
        fechaNacimiento: document.getElementById('reg-fecha').value,
        pais: inputPais.value.trim(),
    };

    console.log("Iniciando transmisión de registro al cuartel general...", nuevoDetective);

    fetch('http://localhost:8080/api/detectives/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevoDetective)
    })
        .then(respuesta => {
            if (respuesta.ok) return respuesta.json();
            throw new Error("El servidor no devolvió un 200 OK.");
        })
        .then(datos => {
            const idRescatado = datos.idUsuario || datos.id;
            localStorage.setItem("idUsuario", idRescatado);
            alert("¡Detective registrado con éxito!");
            window.location.href = "plataformas.html";
        })
        .catch(error => {
            console.error("Error crítico de conexión:", error);
        });
});


// ==========================================
// 4. GESTIÓN DEL FORMULARIO DE LOGIN 
// ==========================================

const formLogin = document.getElementById("form-login");

formLogin.addEventListener('submit', (event) => {
    // Frena la recarga del navegador al intentar loguear
    event.preventDefault();

    // Chivato temporal para comprobar que el botón funciona
    console.log("¡Formulario de login interceptado correctamente!");


    const nombreUsuario = document.getElementById("login-usuario").value;
    const passw = document.getElementById("login-password").value;

    const iniciarSesion = {

        nombreUsuario: nombreUsuario,
        passw: passw

    }

    console.log(iniciarSesion);

    fetch('http://localhost:8080/api/detectives/iniciarSesion', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(iniciarSesion)
    }).then(respuesta => {
        if (respuesta.ok) {
            return respuesta.json();
        } else {
            throw new Error("El servidor no devolvió un 200 OK.");
        }
    }).then(datos => {
        console.log("Respuesta afirmativa de Java:", datos);

        const idRescatado = datos.idUsuario || datos.id;

        localStorage.setItem("idUsuario", idRescatado);

        window.location.href = "mainPage.html";
    }).catch(error => {
        console.error("Error crítico de conexión:", error);
        alert("Hubo un fallo en el login. Mira la consola (F12).");
    });







});