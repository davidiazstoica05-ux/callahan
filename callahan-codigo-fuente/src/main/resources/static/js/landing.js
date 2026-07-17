
//Gestión del modal 

const btnRegistro = document.querySelector('.btn-principal');
const modalRegistro = document.getElementById('modal-registro');
const overlay = document.getElementById('modal-overlay');
const btnCerrarRegistro = document.getElementById('cerrar-registro');

btnRegistro.addEventListener('click', () => {
    overlay.classList.remove('oculto');
    modalRegistro.classList.remove('oculto');
});

function cerrarModales() {
    overlay.classList.add('oculto');
    modalRegistro.classList.add('oculto');
}

btnCerrarRegistro.addEventListener('click', cerrarModales);
overlay.addEventListener('click', cerrarModales);



//Cargar lista de paises

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
    nuevaOpcion.textContent = pais


    datalistPaises.appendChild(nuevaOpcion);
});

//Gestón de dato Formularío 
const formRegistro = document.getElementById("form-registro");


formRegistro.addEventListener('submit', (event) => {

    event.preventDefault();

    const regUsuario = document.getElementById('reg-usuario').value;
    const regEmail = document.getElementById('reg-email').value;
    const regPassw = document.getElementById('reg-password').value;
    const regPrimerApellido = document.getElementById('reg-apellido').value;
    const regNombreReal = document.getElementById('reg-nombre').value;
    const regFecha = document.getElementById('reg-fecha').value;
    const regPais = document.getElementById('reg-pais').value;


    const esValido = paises.includes(regPais);

    if (esValido) {
        console.log(regUsuario);

        //El nombre exacto que hay en Java 
        const nuevoDetective = {
            nombreUsuario: regUsuario,
            email: regEmail,
            passw: regPassw,
            apellido1: regPrimerApellido,
            nombreReal: regNombreReal,
            fechaNacimiento: regFecha,
            pais: regPais
        };

        console.log(nuevoDetective);

        fetch('http://localhost:8080/api/detectives/registro', {

            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(nuevoDetective)

        })
            .then(respuesta => {

                if (respuesta.ok) {

                    return respuesta.json();

                } else {


                    throw new Error("Error al tratar los datos");
                    
                }
            

            })

            .then( datos =>  {

                localStorage.setItem("idUsuario",datos.idUsuario);
                window.location.href = "preferencias.html";

            })

            .catch(error => {

                console.error("Error crítico de conexión:", error);
            });



    } else {

        //Esto hay que llevarlo al backend
        throw new Error("El país no se encuentra en la lista");


    }







})


