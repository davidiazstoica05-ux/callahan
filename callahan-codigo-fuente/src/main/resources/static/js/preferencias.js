const bolsasPeliculas = {

    accion: [155, 1566, 98, 76341, 245891],
    romance: [597, 11036, 313369, 114, 4584],
    terror: [348, 694, 539, 11324, 346364],
    comedia: [18785, 854, 771, 4247, 105],
    cienciaFiccion: [603, 157336, 27205, 11, 808],
    crimen: [238, 278, 680, 807, 550],
    familiar: [862, 858, 12, 14160, 129]
};




let barajaFinal = [];
let barajaPeliculas = [];

function recorrerBolsas(bolsasPeliculas){
    for (const bolsa in bolsasPeliculas) {
       
        if (!Object.hasOwn(bolsasPeliculas, bolsa)) continue;

        //Para acceder al array dentro del diccionario se hace así
        const rnd = Math.floor(Math.random()* bolsasPeliculas[bolsa].length); 


        const idPelicula = bolsasPeliculas[bolsa][rnd];


        barajaFinal.push(idPelicula)
        
        
    }
}

async function descargarPosters() {
    
    // Recorremos cada número (ID) que guardamos en la baraja
    for (const id of barajaFinal) {
    
        //Llamamos al controlador que hace la llamada a la API para no tener la apikey en el .js
        const urlCompleta = `http://localhost:8080/api/callahan/expedientes/${id}`;
        try {
            // 2. Llamamos a la API y pausamos el bucle hasta que responda
            const response = await fetch(urlCompleta);
            
            // 3. Abrimos el paquete y lo convertimos a JSON
            const datos = await response.json();
            
            // 4. Metemos la película completa en nuestro array global
            barajaPeliculas.push(datos);
            
        } catch (error) {
            // Si falla una película en concreto (ej. se cae el internet un segundo), 
            // nos avisa por consola pero el bucle sigue con la siguiente.
            console.error(`Error crítico al solicitar el expediente ${id}:`, error);
        }
    } 

    // Al salir del bucle, imprimimos el resultado de todo el trabajo
    console.log("Expedientes descargados correctamente. Baraja lista:", barajaPeliculas);
    
    //Se llama a la funcion aqui debido a la asincronía de js 
    pintarTarjeta();

}



recorrerBolsas(bolsasPeliculas);
descargarPosters();

console.log(barajaPeliculas);

let indiceActual = 0; 

function pintarTarjeta() {
   
    // 1. El Control de Seguridad
   if (indiceActual === barajaPeliculas.length) {
        window.location.href = "mainPage.html";
        return;
    } 

    const pelicula = barajaPeliculas[indiceActual];

    console.log(pelicula);
    
    const tituloDOM = document.getElementById("titulo-pelicula"); 
    const textoDOM = document.getElementById("director-pelicula");
    const posterDOM = document.getElementById("poster-pelicula");

    tituloDOM.textContent = pelicula.title;
    
    // Nota técnica: La llamada básica a TMDb no devuelve el director. 
    // Como apaño profesional, extraemos solo los primeros 4 caracteres de la fecha (el año).
    textoDOM.textContent = "Año de estreno: " + pelicula.release_date.substring(0, 4);

    // 5. Inyectar el póster construyendo la URL completa
    const urlBaseTMDb = "https://image.tmdb.org/t/p/w500";
    posterDOM.src = urlBaseTMDb + pelicula.poster_path;

    // 6. El Toque de CSS (Animación de entrada)
    posterDOM.classList.add("animacion-entrada");
    
    // Le quitamos la clase después de medio segundo para que esté lista para la siguiente carta
    setTimeout(() => {
        posterDOM.classList.remove("animacion-entrada");
    }, 500);
}

