package no.hvl.dat152.rest.ws.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UnauthorizedOrderActionException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.repository.OrderRepository;
import no.hvl.dat152.rest.ws.security.service.UserDetailsImpl;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public void deleteOrder(Long id) throws OrderNotFoundException {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));
		orderRepository.delete(order);
	}

	public Page<Order> findAllOrders(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}

	public Page<Order> findByExpiryDate(LocalDate expiry, Pageable page) {
		return orderRepository.findByExpiryBefore(expiry, page);
	}

	public Order updateOrder(Order order, Long id) {
		Order existingOrder = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));
		existingOrder.setIsbn(order.getIsbn());
		existingOrder.setExpiry(order.getExpiry());
		return orderRepository.save(existingOrder);
	}

	public Order findOrder(Long id) throws OrderNotFoundException {
		return orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found in the order list!"));
	}
}
