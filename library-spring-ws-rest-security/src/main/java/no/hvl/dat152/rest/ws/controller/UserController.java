/**
 * 
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.service.UserService;

@RestController
@RequestMapping("/elibrary/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Object> getUsers(){
        List<User> users = userService.findAllUsers();
        if(users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) throws UserNotFoundException, OrderNotFoundException{
        User user = userService.findUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PostMapping(value = "/users")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @PutMapping(value = "/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/users/{id}/orders")
    public ResponseEntity<Object> getUserOrders(@PathVariable("id") Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.getUserOrders(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(value = "/users/{uid}/orders/{oid}")
    public ResponseEntity<?> getUserOrder(@PathVariable("uid") Long userId, @PathVariable("oid") Long orderId)
            throws UserNotFoundException, OrderNotFoundException {
        return new ResponseEntity<>(userService.getUserOrder(userId, orderId), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(value = "/users/{uid}/orders/{oid}")
    public ResponseEntity<?> deleteUserOrder(@PathVariable("uid") Long userId, @PathVariable("oid") Long orderId)
            throws UserNotFoundException, OrderNotFoundException {
        userService.deleteOrderForUser(userId, orderId);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/users/{id}/orders")
    public ResponseEntity<Object> createUserOrder(@PathVariable("id") Long id, @RequestBody Order order)
            throws UserNotFoundException, OrderNotFoundException {
        Order createdOrder = userService.createOrdersForUser(id, order);
        Set<Order> userOrders = userService.getUserOrders(id);

        for (Order userOrder : userOrders) {
            Link selfLink = linkTo(methodOn(OrderController.class).getBorrowOrder(userOrder.getId())).withSelfRel();
            Link userOrdersLink = linkTo(methodOn(UserController.class).getUserOrders(id)).withRel("user-orders");
            Link allOrdersLink = linkTo(methodOn(OrderController.class).getAllBorrowOrders(null, 0, 10)).withRel("all-orders");

            userOrder.add(selfLink);
            userOrder.add(userOrdersLink);
            userOrder.add(allOrdersLink);
        }

        return new ResponseEntity<>(userOrders, HttpStatus.CREATED);
    }
}


