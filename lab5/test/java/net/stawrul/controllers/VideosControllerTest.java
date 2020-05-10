package net.stawrul.controllers;

import net.stawrul.model.Video;
import net.stawrul.services.VideosService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

import javax.persistence.EntityManager;
import static org.springframework.http.HttpStatus.CONFLICT;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class VideosControllerTest {
    @Mock
    VideosService vs;

    @Test
    public void listVideos() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        ArrayList<Video> videoArrayList = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            videoArrayList.add(new Video());
        }
        Mockito.when(vs.findAll()).thenReturn(videoArrayList);

        // Act

        // Assert
        assertEquals(videoArrayList, videosController.listVideos());
    }

    @Test
    public void whenGivenVideoThatDoesNotYetExist_RedirectTo201() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        UriComponentsBuilder uriBuilder = Mockito.mock(UriComponentsBuilder.class, Mockito.RETURNS_DEEP_STUBS);
        Video video = new Video();
        URI location = URI.create("http://localhost:9090/books/20a5eeae-8017-46e7-bb60-5437a89e540a");
        Mockito.when(vs.find(video.getId())).thenReturn(null);
        Mockito.when(
                uriBuilder
                    .path("/books/{id}")
                    .buildAndExpand(video.getId())
                    .toUri()
        ).thenReturn(
                location
        );
        Mockito.doNothing().when(vs).save(video);


        // Assert
        assertEquals(ResponseEntity.created(location).build(), videosController.addVideo(video, uriBuilder));
        Mockito.verify(vs, times(1)).save(video);
    }

    @Test
    public void whenGivenVideoThatAlreadyExists_ReturnConflict() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        UriComponentsBuilder uriBuilder = Mockito.mock(UriComponentsBuilder.class);
        Video video = new Video();
        Mockito.when(vs.find(video.getId())).thenReturn(video);

        // Assert
        assertEquals(ResponseEntity.status(CONFLICT).build(), videosController.addVideo(video, uriBuilder));
        Mockito.verify(vs, never()).save(video);
    }

    @Test
    public void getVideoThatExists() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        UUID id = UUID.randomUUID();
        Video video = new Video();
        Mockito.when(vs.find(id)).thenReturn(video);

        // Assert
        assertEquals(ResponseEntity.ok(video), videosController.getVideo(id));
    }

    @Test
    public void getVideoThatDoesNotExists() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        UUID id = UUID.randomUUID();
        Mockito.when(vs.find(id)).thenReturn(null);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), videosController.getVideo(id));
    }

    @Test
    public void updateVideoThatExists() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        Video video = new Video();
        Mockito.when(vs.find(video.getId())).thenReturn(video);

        // Assert
        assertEquals(ResponseEntity.ok().build(), videosController.updateVideo(video));
        Mockito.verify(vs, times(1)).save(video);
    }

    @Test
    public void updateVideoThatDoesNotExist() {
        // Arrange
        VideosController videosController = new VideosController(vs);
        Video video = new Video();
        Mockito.when(vs.find(video.getId())).thenReturn(null);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), videosController.updateVideo(video));
        Mockito.verify(vs, never()).save(video);
    }
}