/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.OrderNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Order;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.UserRepository;

/**
 * @author tdoy
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	
	public List<User> findAllUsers(){
		
		List<User> allUsers = (List<User>) userRepository.findAll();
		
		return allUsers;
	}
	
	public User findUser(Long userid) throws UserNotFoundException {
		
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: "+userid+" not found"));
		
		return user;
	}
	
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}
	
	public void deleteUser(Long id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(()-> new UserNotFoundException("User with id: " + id + " not found"));
		userRepository.delete(user);
	}

	public User updateUser(Long id, User user) {
		return userRepository.save(user);
	}
	
	public Set<Order> getUserOrders(Long userid) throws UserNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: " + userid + " not found"));
		return user.getOrders();
	}
	
	public Order getUserOrder(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: " + userid + " not found"));
		return user.getOrders().stream().filter(order -> order.getId().equals(oid)).findFirst()
				.orElseThrow(()-> new OrderNotFoundException("Order with id: " + oid + " not found"));
	}

	public void deleteOrderForUser(Long userid, Long oid) throws UserNotFoundException, OrderNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(()-> new UserNotFoundException("User with id: " + userid + " not found"));
		Order order = user.getOrders().stream().filter(o -> o.getId().equals(oid)).findFirst()
				.orElseThrow(()-> new OrderNotFoundException("Order with id: " + oid + " not found"));

		user.getOrders().remove(order);
		userRepository.save(user);
	}
	
	public Order createOrdersForUser(Long userid, Order order) throws UserNotFoundException {
		User user = userRepository.findById(userid)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + userid + " not found"));

		user.addOrder(order);
		userRepository.save(user);
		return order;
	}
}
