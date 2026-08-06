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


        // 1. Construimos al usuario fanático del Anime
        Usuario otaku = Usuario.builder()
                .nombreUsuario("OtakuSenpai")
                .nombreReal("Takeshi")
                .apellido1("Yamamoto")
                .email("anime@callahan.com")
                .passw("ghibli123")
                .fechaNacimiento(LocalDate.of(2002, 3, 14))
                .pais("España")
                .build();

        // Lo guardamos para generar su ID
        Usuario otakuGuardado = usuarioRepository.save(otaku);

        // 2. Construimos sus Preferencias orientadas a la animación
        Preferencias preferenciasOtaku = Preferencias.builder()
                .usuario(otakuGuardado)
                .epocapelicula(EpocasPeliculas.ACTUALIDAD) // O LOS_DOSMIL, según tengas en tu Enum
                .toleranciaALaDuracion(130.0) // Las pelis de anime no suelen ser excesivamente largas
                .idDirectorFav(608L) // Hayao Miyazaki (Studio Ghibli)
                .idDirectorOdiado(865L) // Michael Bay (Contraste absoluto: Transformers, explosiones...)
                .generosFavoritos(List.of(16, 14)) // 16: Animación, 14: Fantasía
                .generosVetados(List.of(37, 99)) // 37: Western, 99: Documental (Para probar los descartes)
                .plataformasContratadas(Set.of("CRUNCHYROLL", "NETFLIX", "AMAZON_PRIME")) // El combo de streaming de anime
                .build();
        // ==========================================
        // 3. EL INSPECTOR CLÁSICO (Anti-moderno)
        // ==========================================
        Usuario inspector = Usuario.builder()
                .nombreUsuario("HarryMagnum")
                .nombreReal("Harry")
                .apellido1("Callahan")
                .email("magnum@callahan.com")
                .passw("make_my_day")
                .fechaNacimiento(LocalDate.of(1965, 8, 12))
                .pais("España")
                .build();
        Usuario inspectorGuardado = usuarioRepository.save(inspector);

        Preferencias prefInspector = Preferencias.builder()
                .usuario(inspectorGuardado)
                .epocapelicula(EpocasPeliculas.LOS_SETENTAS) // Amante del cine crudo de los 70s
                .toleranciaALaDuracion(140.0)
                .idDirectorFav(190L) // Clint Eastwood
                .idDirectorOdiado(2710L) // James Cameron (Odia el CGI y el romance)
                .generosFavoritos(List.of(80, 53)) // 80: Crimen, 53: Thriller
                .generosVetados(List.of(10749, 14, 16)) // Romance, Fantasía, Animación
                .plataformasContratadas(Set.of("MAX", "FILMIN"))
                .build();
        preferenciasRepository.save(prefInspector);

        // ==========================================
        // 4. EL ADICTO A LA ADRENALINA (Solo acción)
        // ==========================================
        Usuario drifter = Usuario.builder()
                .nombreUsuario("DriftKing")
                .nombreReal("Sean")
                .apellido1("Boswell")
                .email("drift@callahan.com")
                .passw("nitro123")
                .fechaNacimiento(LocalDate.of(1999, 4, 15))
                .pais("España")
                .build();
        Usuario drifterGuardado = usuarioRepository.save(drifter);

        Preferencias prefDrifter = Preferencias.builder()
                .usuario(drifterGuardado)
                .epocapelicula(EpocasPeliculas.LOS_DOSMIL) // Época dorada de A Todo Gas
                .toleranciaALaDuracion(90.0) // No tiene paciencia para pelis largas
                .idDirectorFav(52857L) // Justin Lin
                .idDirectorOdiado(113010L) // Directores de cine indie/lento
                .generosFavoritos(List.of(28, 80)) // 28: Acción, 80: Crimen
                .generosVetados(List.of(99, 36, 10402)) // Documental, Historia, Música
                .plataformasContratadas(Set.of("NETFLIX", "PRIME_VIDEO"))
                .build();
        preferenciasRepository.save(prefDrifter);

        // ==========================================
        // 5. EL CINÉFILO OSCURO (Suspense psicológico)
        // ==========================================
        Usuario misterio = Usuario.builder()
                .nombreUsuario("Dae-su")
                .nombreReal("Oh")
                .apellido1("Dae-su")
                .email("oldboy@callahan.com")
                .passw("dumplings")
                .fechaNacimiento(LocalDate.of(1985, 11, 2))
                .pais("España")
                .build();
        Usuario misterioGuardado = usuarioRepository.save(misterio);

        Preferencias prefMisterio = Preferencias.builder()
                .usuario(misterioGuardado)
                .epocapelicula(EpocasPeliculas.LOS_DOSMIL)
                .toleranciaALaDuracion(150.0)
                .idDirectorFav(21684L) // Park Chan-wook
                .idDirectorOdiado(null) // No odia a nadie en particular
                .generosFavoritos(List.of(9648, 53, 18)) // 9648: Misterio, Thriller, Drama
                .generosVetados(List.of(35, 10751, 10749)) // Comedia, Familia, Romance
                .plataformasContratadas(Set.of("FILMIN", "APPLE_TV"))
                .build();
        preferenciasRepository.save(prefMisterio);

        // ==========================================
        // 6. EL TECNOLÓGICO (Ciencia Ficción y distopías)
        // ==========================================
        Usuario techie = Usuario.builder()
                .nombreUsuario("MirrorCoder")
                .nombreReal("Dev")
                .apellido1("Null")
                .email("tech@callahan.com")
                .passw("algoritmo")
                .fechaNacimiento(LocalDate.of(2003, 1, 19))
                .pais("España")
                .build();
        Usuario techieGuardado = usuarioRepository.save(techie);

        Preferencias prefTechie = Preferencias.builder()
                .usuario(techieGuardado)
                .epocapelicula(EpocasPeliculas.ACTUALIDAD)
                .toleranciaALaDuracion(135.0)
                .idDirectorFav(525L) // Christopher Nolan
                .idDirectorOdiado(null)
                .generosFavoritos(List.of(878, 53)) // 878: Ciencia Ficción
                .generosVetados(List.of(37, 10752)) // Western, Guerra
                .plataformasContratadas(Set.of("NETFLIX", "APPLE_TV", "MAX"))
                .build();
        preferenciasRepository.save(prefTechie);

        // ==========================================
        // 7. EL CONTRASTE TOTAL (Familia y Confort)
        // ==========================================
        Usuario cozy = Usuario.builder()
                .nombreUsuario("CozyWatcher")
                .nombreReal("Laura")
                .apellido1("Gómez")
                .email("cozy@callahan.com")
                .passw("manta_y_peli")
                .fechaNacimiento(LocalDate.of(1992, 12, 5))
                .pais("España")
                .build();
        Usuario cozyGuardado = usuarioRepository.save(cozy);

        Preferencias prefCozy = Preferencias.builder()
                .usuario(cozyGuardado)
                .epocapelicula(EpocasPeliculas.DECADA_2010)
                .toleranciaALaDuracion(105.0) // Películas cortitas y al pie
                .idDirectorFav(null)
                .idDirectorOdiado(138L) // Tarantino (Demasiada sangre para ella)
                .generosFavoritos(List.of(10751, 10749, 35)) // Familia, Romance, Comedia
                .generosVetados(List.of(27, 53, 80, 9648)) // Odia todo el terror, crimen y misterio
                .plataformasContratadas(Set.of("DISNEY_PLUS", "NETFLIX"))
                .build();
        preferenciasRepository.save(prefCozy);

        // Guardamos las preferencias
        preferenciasRepository.save(preferenciasOtaku);

        // Un pequeño chivato en consola para saber que todo ha ido bien
        System.out.println("==================================================");
        System.out.println("✅ DATA SEED EJECUTADO");


    }
}