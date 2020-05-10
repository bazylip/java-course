package net.stawrul.services;

import net.stawrul.model.Video;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na filmach.
 */
@Service
public class VideosService extends EntityService<Video> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public VideosService(EntityManager em) {

        //Video.class - klasa encyjna, na której będą wykonywane operacje
        //Video::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Video.class, Video::getId);
    }

    /**
     * Pobranie wszystkich filmów z bazy danych.
     *
     * @return lista filmów
     */
    public List<Video> findAll() {
        //pobranie listy wszystkich książek za pomocą zapytania nazwanego (ang. named query)
        //zapytanie jest zdefiniowane w klasie Video
        return em.createNamedQuery(Video.FIND_ALL, Video.class).getResultList();
    }

}
