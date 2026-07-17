package com.callahan.callahancodigofuente.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// Importamos tu DTO para poder usarlo en la prueba
import com.callahan.callahancodigofuente.dtos.ProcesamientoPreferenciasDTO;

public class PreferenciasServiceTest {

    // @Test le dice a Java: "Este método no es parte de la aplicación normal,
    // es un experimento de laboratorio. Ponle un botón de 'Play' al lado".
    @Test
    public void testAlgoritmoPuntuacionGeneros() {

        // ==========================================
        // FASE 1: ARRANGE (Preparamos los ingredientes)
        // ==========================================

        // 1. Instanciamos el servicio. Como no estamos arrancando Spring Boot,
        // @Autowired no funciona aquí. Tenemos que crear el objeto manualmente con "new".
        PreferenciasService servicio = new PreferenciasService();

        // 2. Creamos listas de prueba rápidas. Arrays.asList() es un atajo de Java
        // para no tener que hacer lista.add() cuatro veces seguidas.
        // Simulamos que el usuario le ha dado 'Me gusta' a tres películas de Acción (28) y una de Comedia (35).
        List<Integer> gustadas = Arrays.asList(28, 28, 28, 35);

        // Simulamos que ha descartado una de Aventura (12) y una de Acción (28).
        List<Integer> descartadas = Arrays.asList(12, 28);

        // 3. Empaquetamos los datos. Como tu servicio ahora exige recibir la "caja cerrada" (el DTO),
        // usamos el patrón .builder() de Lombok para construir un DTO falso y meterle nuestras listas.
        ProcesamientoPreferenciasDTO datosCrudosTest = ProcesamientoPreferenciasDTO.builder()
                .PeliculasGustadas(gustadas)
                .PeliculasNoGustadas(descartadas)
                .build();


        // ==========================================
        // FASE 2: ACT (Encendemos la máquina)
        // ==========================================

        // 4. Le pasamos nuestra caja falsa al método real.
        // El algoritmo hará sus cálculos internos ciegamente y nos devolverá la lista ordenada.
        List<Map.Entry<Integer, Integer>> resultado = servicio.procesarDatosCrudos(datosCrudosTest);


        // ==========================================
        // FASE 3: ASSERT (Comprobamos si la máquina nos miente)
        // ==========================================

        // Los métodos assertEquals siempre funcionan igual: assertEquals(LO_QUE_YO_ESPERO, LO_QUE_HA_PASADO_REALMENTE)

        // Comprobación A: ¿Ha contado bien los géneros únicos?
        // Hemos metido los géneros 28, 35 y 12. Debería haber exactamente 3 filas en la libreta.
        assertEquals(3, resultado.size());

        // Comprobación B: ¿Ha ordenado bien?
        // El género 28 es el que más interacciones tiene. Si todo está bien, debería estar coronado en la posición 0.
        // get(0) saca la primera fila, y getKey() saca el ID del género.
        assertEquals(28, resultado.get(0).getKey());

        // Comprobación C: ¿Ha sumado y restado bien los puntos asimétricos?
        // El 28 tiene tres "Me gusta" (+6 puntos) y un descarte (-1 punto). El total debe ser 5.
        // get(0) saca la primera fila, y getValue() saca la puntuación final.
        assertEquals(5, resultado.get(0).getValue());
    }
}