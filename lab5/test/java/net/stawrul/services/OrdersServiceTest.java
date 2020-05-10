package net.stawrul.services;

import net.stawrul.model.Video;
import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.DuplicateException;
import net.stawrul.services.exceptions.ExceededAmountException;
import net.stawrul.services.exceptions.ExceededNumberException;
import net.stawrul.services.exceptions.OutOfStockException;
import org.aspectj.weaver.ast.Or;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class OrdersServiceTest {

    @Mock
    EntityManager em;

    @Test(expected = OutOfStockException.class)
    public void whenOrderedVideoNotAvailable_placeOrderThrowsOutOfStockEx() {
        //Arrange
        Order order = new Order();
        Video video = new Video();
        video.setAmount(0);
        order.getVideos().add(video);

        Mockito.when(em.find(Video.class, video.getId())).thenReturn(video);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test(expected = ExceededAmountException.class)
    public void whenOrderedVideoNumberNotAvailable_placeOrderThrowsExceededAmountEx() {
        //Arrange
        Integer amount = new Integer(10);
        Order order = new Order();
        Video videosAvailable = new Video();
        videosAvailable.setAmount(amount);

        Video videosOrdered = new Video();
        videosOrdered.setAmount(amount+1);
        order.getVideos().add(videosOrdered);

        Mockito.when(em.find(Video.class, videosOrdered.getId())).thenReturn(videosAvailable);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test(expected = DuplicateException.class)
    public void whenOrderedDuplicatedVideos_placeOrderThrowsDuplicateEx() {
        //Arrange
        Order order = new Order();
        Video video = new Video();
        order.getVideos().add(video);
        order.getVideos().add(video);

        Mockito.when(em.find(Video.class, video.getId())).thenReturn(video);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test(expected = ExceededNumberException.class)
    public void whenOrderedTooManyVideos_placeOrderThrowsExceededNumberEx() {
        //Arrange
        Order order = new Order();
        OrdersService ordersService = new OrdersService(em);

        for(int i=0; i<(ordersService.getNumberLimit() + 1); ++i){
            order.getVideos().add(new Video());
        }

        //Act
        ordersService.placeOrder(order);

        //Assert - exception expected
    }

    @Test
    public void whenOrderedVideoAvailable_placeOrderDecreasesAmountByOne() {
        //Arrange
        Order order = new Order();
        Video video = new Video();
        video.setAmount(1);
        order.getVideos().add(video);

        Mockito.when(em.find(Video.class, video.getId())).thenReturn(video);

        OrdersService ordersService = new OrdersService(em);

        //Act
        ordersService.placeOrder(order);

        //Assert
        //dostępna liczba książek zmniejszyła się:
        assertEquals(0, (int) video.getAmount());
        //nastąpiło dokładnie jedno wywołanie em.persist(order) w celu zapisania zamówienia:
        Mockito.verify(em, times(1)).persist(order);
    }

    @Test
    public void whenGivenListOfOrders_findAllReturnsTheSameListOfOrders() {
        //Arrange
        EntityManager entityManager = Mockito.mock(
                EntityManager.class, //klasa do zamockowania
                RETURNS_DEEP_STUBS //tryb mockowania
        );
        OrdersService ordersService = new OrdersService(entityManager);
        ArrayList<Order> ordersList = new ArrayList<>();

        //Act
        for(int i = 0; i < 10; ++i){
            Order order = new Order();
            Video video = new Video();
            video.setAmount(1);
            Mockito.when(entityManager.find(Video.class, video.getId())).thenReturn(video);
            order.getVideos().add(video);
            ordersList.add(order);
            ordersService.placeOrder(order);
        }
        Mockito.when(
                entityManager
                    .createQuery("SELECT o FROM Order o", Order.class)
                    .getResultList()
        ).thenReturn(
                ordersList
        );
        System.out.println(ordersList);
        //Assert
        //findAll() returns all orders that have been place
        assertNotNull(ordersService.findAll());
        assertEquals(ordersList, ordersService.findAll());

    }

    @Test
    public void whenGivenLowercaseString_toUpperReturnsUppercase() {

        //Arrange
        String lower = "abcdef";

        //Act
        String result = lower.toUpperCase();

        //Assert
        assertEquals("ABCDEF", result);
    }
}
