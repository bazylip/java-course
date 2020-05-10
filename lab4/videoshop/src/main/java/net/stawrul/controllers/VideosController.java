package net.stawrul.controllers;

import net.stawrul.model.Video;
import net.stawrul.services.VideosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CONFLICT;


/**
 * Kontroler zawierający akcje związane z książkami w sklepie.
 *
 * Parametr "/books" w adnotacji @RequestMapping określa prefix dla adresów wszystkich akcji kontrolera.
 */
@RestController
@RequestMapping("/videos")
public class VideosController {

    //Komponent realizujący logikę biznesową operacji na książkach
    final VideosService videosService;

    //Instancja klasy BooksService zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public VideosController(VideosService videosService) {
        this.videosService = videosService;
    }

    /**
     * Pobieranie listy wszystkich filmów.
     *
     * Żądanie:
     * GET /videos
     *
     * @return lista filmów
     */
    @GetMapping
    public List<Video> listVideos() {
        return videosService.findAll();
    }

    /**
     * Dodawanie nowego filmu.
     *
     * Żądanie:
     * POST /videos
     *
     * @param video obiekt zawierający dane nowego filmu, zostanie zbudowany na podstawie danych
     *             przesłanych w ciele żądania (automatyczne mapowanie z formatu JSON na obiekt
     *             klasy Video)
     * @param uriBuilder pomocniczy obiekt do budowania adresu wskazującego na nowo dodany film,
     *                   zostanie wstrzyknięty przez framework Spring
     *
     * @return odpowiedź HTTP dla klienta
     */
    @PostMapping
    public ResponseEntity<Void> addVideo(@RequestBody Video video, UriComponentsBuilder uriBuilder) {

        if (videosService.find(video.getId()) == null) {
            //Identyfikator nie istnieje w bazie danych - nowa książka zostaje zapisana
            videosService.save(video);

            //Jeśli zapisywanie się powiodło zwracana jest odpowiedź 201 Created z nagłówkiem Location, który zawiera
            //adres nowo dodanej książki
            URI location = uriBuilder.path("/books/{id}").buildAndExpand(video.getId()).toUri();
            return ResponseEntity.created(location).build();

        } else {
            //Identyfikator książki już istnieje w bazie danych. Żądanie POST służy do dodawania nowych elementów,
            //więc zwracana jest odpowiedź z kodem błędu 409 Conflict
            return ResponseEntity.status(CONFLICT).build();
        }
    }

    /**
     * Pobieranie informacji o pojedynczym filmie.
     *
     * Żądanie:
     * GET /videos/{id}
     *
     * @param id identyfikator filmu
     *
     * @return odpowiedź 200 zawierająca dane filmu lub odpowiedź 404, jeśli film o podanym identyfikatorze nie
     * istnieje w bazie danych
     */
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideo(@PathVariable UUID id) {
        //wyszukanie książki w bazie danych
        Video video = videosService.find(id);

        //W warstwie biznesowej brak książki o podanym id jest sygnalizowany wartością null. Jeśli książka nie została
        //znaleziona zwracana jest odpowiedź 404 Not Found. W przeciwnym razie klient otrzymuje odpowiedź 200 OK
        //zawierającą dane książki w domyślnym formacie JSON
        return video != null ? ResponseEntity.ok(video) : ResponseEntity.notFound().build();
    }

    /**
     * Aktualizacja danych filmu.
     *
     * Żądanie:
     * PUT /videos/{id}
     *
     * @param video
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVideo(@RequestBody Video video) {
        if (videosService.find(video.getId()) != null) {
            //aktualizacja danych jest możliwa o ile książka o podanym id istnieje w bazie danych
            videosService.save(video);
            return ResponseEntity.ok().build();

        } else {
            //nie odnaleziono książki o podanym id - odpowiedź 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

}
