package net.stawrul.services;

import net.stawrul.model.Video;
import net.stawrul.model.Order;
import net.stawrul.services.exceptions.DuplicateException;
import net.stawrul.services.exceptions.ExceededAmountException;
import net.stawrul.services.exceptions.ExceededNumberException;
import net.stawrul.services.exceptions.OutOfStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Komponent (serwis) biznesowy do realizacji operacji na zamówieniach.
 */
@Service
public class OrdersService extends EntityService<Order> {

    //Instancja klasy EntityManger zostanie dostarczona przez framework Spring
    //(wstrzykiwanie zależności przez konstruktor).
    public OrdersService(EntityManager em) {

        //Order.class - klasa encyjna, na której będą wykonywane operacje
        //Order::getId - metoda klasy encyjnej do pobierania klucza głównego
        super(em, Order.class, Order::getId);
    }

    /**
     * Pobranie wszystkich zamówień z bazy danych.
     *
     * @return lista zamówień
     */
    public List<Order> findAll() {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    /**
     * Złożenie zamówienia w sklepie.
     * <p>
     * Zamówienie jest akceptowane, jeśli wszystkie objęte nim produkty są dostępne (przynajmniej 1 sztuka). W wyniku
     * złożenia zamówienia liczba dostępnych sztuk produktów jest zmniejszana o jeden. Metoda działa w sposób
     * transakcyjny - zamówienie jest albo akceptowane w całości albo odrzucane w całości. W razie braku produktu
     * wyrzucany jest wyjątek OutOfStockException.
     *
     * @param order zamówienie do przetworzenia
     */
    @Transactional
    public void placeOrder(Order order) {
        int numberLimit = 3, orderAmount;
        Set<Video> set = new HashSet<Video>(order.getVideos());
        if(set.size() < order.getVideos().size()){
            throw new DuplicateException();
        }
        if(order.getVideos().size() > numberLimit){
            throw new ExceededNumberException();
        }
        for (Video videoStub : order.getVideos()) {
            Video video = em.find(Video.class, videoStub.getId());
            if (video.getAmount() < 1) {
                //wyjątek z hierarchii RuntineException powoduje wycofanie transakcji (rollback)
                throw new OutOfStockException();
            } else {
                if((orderAmount = videoStub.getAmount()) > video.getAmount()){
                    throw new ExceededAmountException();
                }else{
                    int newAmount = video.getAmount() - orderAmount;
                    video.setAmount(newAmount);
                }
            }
        }

        //jeśli wcześniej nie został wyrzucony wyjątek OutOfStockException, zamówienie jest zapisywane w bazie danych
        save(order);
    }
}
