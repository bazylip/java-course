package net.stawrul.services;

import net.stawrul.model.Video;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@RunWith(MockitoJUnitRunner.class)
public class VideosServiceTest {

    @Test
    public void findAll() {
        //Arrange
        EntityManager entityManager = Mockito.mock(
                EntityManager.class, //klasa do zamockowania
                RETURNS_DEEP_STUBS //tryb mockowania
        );
        ArrayList<Video> videosList = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            videosList.add(new Video());
        }
        VideosService videosService = new VideosService(entityManager);
        Mockito.when(
                entityManager
                        .createNamedQuery(Video.FIND_ALL, Video.class)
                        .getResultList()
        ).thenReturn(
                videosList
        );

        //Assert
        assertEquals(videosList, videosService.findAll());
    }

}