package net.stawrul.controllers;

import net.stawrul.model.Order;
import net.stawrul.services.OrdersService;
import net.stawrul.services.exceptions.OutOfStockException;
import org.aspectj.weaver.ast.Or;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

@RunWith(MockitoJUnitRunner.class)
public class ShopControllerTest {
    @Mock
    OrdersService os;

    @Test
    public void listOrders() {
        // Arrange
        ShopController shopController = new ShopController(os);
        ArrayList<Order> orders = new ArrayList<>();
        for(int i = 0; i < 10; ++i){
            orders.add(new Order());
        }
        Mockito.when(os.findAll()).thenReturn(orders);

        // Assert
        assertEquals(orders, shopController.listOrders());
    }

    @Test
    public void getOrderThatExists() {
        // Arrange
        ShopController shopController = new ShopController(os);
        Order order = new Order();
        UUID id = UUID.randomUUID();
        Mockito.when(os.find(id)).thenReturn(order);

        // Assert
        assertEquals(ResponseEntity.ok(order), shopController.getOrder(id));
    }

    @Test
    public void getOrderThatDoesNotExist() {
        // Arrange
        ShopController shopController = new ShopController(os);
        Order order = new Order();
        UUID id = UUID.randomUUID();
        Mockito.when(os.find(id)).thenReturn(null);

        // Assert
        assertEquals(ResponseEntity.notFound().build(), shopController.getOrder(id));
    }

    @Test
    public void addCorrectOrder() {
        // Arrange
        ShopController shopController = new ShopController(os);
        Order order = new Order();
        URI location = URI.create("http://localhost:9090/orders/20a5eeae-8017-46e7-bb60-5437a89e540a");
        UriComponentsBuilder uriBuilder = Mockito.mock(
                UriComponentsBuilder.class,
                RETURNS_DEEP_STUBS
        );
        Mockito.when(
                uriBuilder
                        .path(any())
                        .buildAndExpand(order.getId())
                        .toUri()
        ).thenReturn(
                location
        );

        // Assert
        assertEquals(ResponseEntity.created(location).build(), shopController.addOrder(order, uriBuilder));
    }

    @Test
    public void addIncorrectOrder() {
        // Arrange
        ShopController shopController = new ShopController(os);
        Order order = new Order();
        URI location = URI.create("http://localhost:9090/orders/20a5eeae-8017-46e7-bb60-5437a89e540a");
        UriComponentsBuilder uriBuilder = Mockito.mock(
                UriComponentsBuilder.class,
                RETURNS_DEEP_STUBS
        );
        Mockito.doThrow(new OutOfStockException()).when(os).placeOrder(order);

        // Assert
        assertEquals(ResponseEntity.unprocessableEntity().build(), shopController.addOrder(order, uriBuilder));
    }
}