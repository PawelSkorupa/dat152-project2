/**
 * 
 */
package no.hvl.dat152.rest.ws.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.UserNotFoundException;
import no.hvl.dat152.rest.ws.model.Role;
import no.hvl.dat152.rest.ws.model.User;
import no.hvl.dat152.rest.ws.repository.RoleRepository;
import no.hvl.dat152.rest.ws.repository.UserRepository;
@Service
public class AdminUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	public User updateUserRole(Long id, String roleName) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

		Role role = roleRepository.findByName(roleName);
		if (role == null) {
			throw new IllegalArgumentException("Role " + roleName + " not found");
		}

		if (!user.getRoles().contains(role)) {
			user.getRoles().add(role);
			userRepository.save(user);
		}

		return user;
	}

	public User revokeRoleFromUser(Long id, String roleName) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User with id: " + id + " not found"));

		Role role = roleRepository.findByName(roleName);
		if (role == null) {
			throw new IllegalArgumentException("Role " + roleName + " not found");
		}

		if (user.getRoles().contains(role)) {
			user.getRoles().remove(role);
			userRepository.save(user);
		} else {
			throw new IllegalArgumentException("User does not have role " + roleName);
		}

		return user;
	}
}

