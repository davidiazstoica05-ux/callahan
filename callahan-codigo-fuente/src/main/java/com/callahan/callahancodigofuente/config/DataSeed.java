package com.callahan.callahancodigofuente.config; // Ajusta el paquete si es necesario

import com.callahan.callahancodigofuente.models.EpocasPeliculas;
import com.callahan.callahancodigofuente.models.Preferencias;
import com.callahan.callahancodigofuente.models.Usuario;
import com.callahan.callahancodigofuente.repository.PreferenciasRepository;
import com.callahan.callahancodigofuente.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeed implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PreferenciasRepository preferenciasRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. Construimos al Detective con el patrón Builder
        Usuario hater = Usuario.builder()
                .nombreUsuario("Hater99")
                .nombreReal("Paco")
                .apellido1("García")
                .email("hater@callahan.com")
                .passw("1234")
                .fechaNacimiento(LocalDate.of(1995, 5, 20))
                .pais("España")
                // La fecha de registro se pone sola por tu @PrePersist
                .build();

        // Lo guardamos primero para que la base de datos le asigne su ID oficial
        Usuario usuarioGuardado = usuarioRepository.save(hater);

        // 2. Construimos sus Preferencias Extremas
        Preferencias preferenciasHater = Preferencias.builder()
                .usuario(usuarioGuardado) // Mapeamos con el @MapsId pasándole el objeto completo
                .epocapelicula(EpocasPeliculas.LOS_OCHENTAS)
                .toleranciaALaDuracion(120.0)
                .idDirectorFav(578L) // Ridley Scott (Para probar la matemática)
                .idDirectorOdiado(138L) // Quentin Tarantino (Para probar la guillotina)
                .generosFavoritos(List.of(878)) // Ciencia Ficción
                .generosVetados(List.of(28, 35, 10749)) // Acción, Comedia, Romance
                .plataformasContratadas(Set.of("NETFLIX", "MAX"))
                .build();

        // Guardamos las preferencias
        preferenciasRepository.save(preferenciasHater);

        // Un pequeño chivato en consola para saber que todo ha ido bien
        System.out.println("==================================================");
        System.out.println("✅ DATA SEED EJECUTADO: Perfil Hater99 inyectado.");
        System.out.println("==================================================");
    }
}