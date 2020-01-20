package ua.training.restaurant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import ua.training.restaurant.entity.Dish;
import ua.training.restaurant.entity.OrderUnit;
import ua.training.restaurant.entity.order.Order;
import ua.training.restaurant.entity.order.Order_Status;
import ua.training.restaurant.entity.user.Role;
import ua.training.restaurant.entity.user.User;
import ua.training.restaurant.exceptions.EmptyOrderException;
import ua.training.restaurant.exceptions.OrderNotFoundException;
import ua.training.restaurant.repository.OrderRepository;
import ua.training.restaurant.service.OrderService;
import ua.training.restaurant.service.OrderServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Student on 18.01.2020
 */
public class OrderServiceImplTest {
    OrderService orderService;

    private OrderRepository orderRepository;

    @Before
    public void setUP() {
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    public void testFindByUser_ExpectedEquals() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        User user = new User(1L, "Maksym", "Максим", "maksym201051", "1234567890",
                0L, 0, 0L, LocalDate.now(), roles, true,
                true, true, true);
        Order order1 = new Order();
        Order order2 = new Order();
        order1.setId(1L);
        order2.setId(2L);
        order1.setUser(user);
        order2.setUser(user);
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        when(orderRepository.findByUser(user)).thenReturn(orders);
        Assert.assertEquals(orders,orderService.findByUser(user));
    }
    @Test
    public void testFindByUser_ExpectedNotEquals() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        User user = new User(1L, "Maksym", "Максим", "maksym201051", "1234567890",
                0L, 0, 0L, LocalDate.now(), roles, true,
                true, true, true);
        List<Order> orders = new ArrayList<>();
        when(orderRepository.findByUser(user)).thenReturn(orders);
        Assert.assertEquals(orders,orderService.findByUser(user));
    }
    @Test(expected = EmptyOrderException.class)
    public void testSave_EmptyOrder_ExpectedException() throws EmptyOrderException {
        Order order = new Order();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        User user = new User(1L, "Maksym", "Максим", "maksym201051", "1234567890",
                0L, 0, 0L, LocalDate.now(), roles, true,
                true, true, true);
        orderService.save(order,user);
    }
    @Test
    public void testSave_OneDishOrder_ExpectedStatusCreated() throws EmptyOrderException {
        Order order = new Order();
        Dish dish = new Dish();
        order.getOrderUnits().add(new OrderUnit(1L,dish,1));
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        User user = new User(1L, "Maksym", "Максим", "maksym201051", "1234567890",
                0L, 0, 0L, LocalDate.now(), roles, true,
                true, true, true);
        Order order1 = orderService.save(order,user);
        Assert.assertEquals(Order_Status.CREATED,order1.getStatus());
    }
    @Test
    public void testSave_OneDishOrder_ExpectedUserEqualsInput() throws EmptyOrderException {
        Order order = new Order();
        Dish dish = new Dish();
        order.getOrderUnits().add(new OrderUnit(1L,dish,1));
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        User user = new User(1L, "Maksym", "Максим", "maksym201051", "1234567890",
                0L, 0, 0L, LocalDate.now(), roles, true,
                true, true, true);
        Order order1 = orderService.save(order,user);
        Assert.assertEquals(user,order1.getUser());
    }
    @Test
    public void testAddDish_EmptyOrderInputDishQuantityOne_ExpectedNewOrderUnitWithInputDish() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setId(1L);
        Order order1 = orderService.addDish(order,dish,1);
        Assert.assertEquals(dish,order1.getOrderUnits().get(0).getDish());
    }
    @Test
    public void testAddDish_OrderOneDishQuantityOneInputQuantityTwo_ExpectedDishQuantityThree() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setId(1L);
        OrderUnit orderUnit = new OrderUnit();
        orderUnit.setDish(dish);
        orderUnit.setQuantity(1);
        List<OrderUnit> orderUnits =new ArrayList<>();
        orderUnits.add(orderUnit);
        order.setOrderUnits(orderUnits);
        Order order1 = orderService.addDish(order,dish,2);
        Assert.assertEquals(new Integer(3),order1.getOrderUnits().get(0).getQuantity());
    }
    @Test
    public void testAddDish_EmptyOrderOneDishQuantityOneInputQuantityMinusTen_ExpectedEmptyOrder() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setId(1L);
        OrderUnit orderUnit = new OrderUnit();
        orderUnit.setDish(dish);
        orderUnit.setQuantity(1);
        List<OrderUnit> orderUnits =new ArrayList<>();
        orderUnits.add(orderUnit);
        order.setOrderUnits(orderUnits);
        Order order1 = orderService.addDish(order,dish,-10);
        Assert.assertTrue(order1.getOrderUnits().isEmpty());
    }
    @Test
    public void updateDishTest_OrderWithOneDishQuantityOneInputQuantityTwo_ExpectedDishQuantityTwo() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setId(1L);
        OrderUnit orderUnit = new OrderUnit();
        orderUnit.setDish(dish);
        orderUnit.setQuantity(1);
        List<OrderUnit> orderUnits =new ArrayList<>();
        orderUnits.add(orderUnit);
        order.setOrderUnits(orderUnits);
        Order order1 = orderService.updateDish(order,dish.getId(),2);
        Assert.assertEquals(new Integer(2),order1.getOrderUnits().get(0).getQuantity());
    }
    @Test
    public void updateDishTest_OrderWithOneDishQuantityOneInputQuantityMinusTen_ExpectedEmptyOrder() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setId(1L);
        OrderUnit orderUnit = new OrderUnit();
        orderUnit.setDish(dish);
        orderUnit.setQuantity(1);
        List<OrderUnit> orderUnits =new ArrayList<>();
        orderUnits.add(orderUnit);
        order.setOrderUnits(orderUnits);
        Order order1 = orderService.updateDish(order,dish.getId(),-10);
        Assert.assertTrue(order1.getOrderUnits().isEmpty());
    }
    @Test
    public void removeDishTest_OrderWithOneDish_ExpectedEmptyOrder() {
        Order order = new Order();
        Dish dish = new Dish();
        dish.setId(1L);
        OrderUnit orderUnit = new OrderUnit();
        orderUnit.setDish(dish);
        orderUnit.setQuantity(1);
        List<OrderUnit> orderUnits =new ArrayList<>();
        orderUnits.add(orderUnit);
        order.setOrderUnits(orderUnits);
        Order order1 = orderService.removeDish(order,dish);
        Assert.assertTrue(order1.getOrderUnits().isEmpty());
    }
}