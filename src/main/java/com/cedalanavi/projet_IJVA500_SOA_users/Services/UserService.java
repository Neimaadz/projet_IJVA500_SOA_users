package com.cedalanavi.projet_IJVA500_SOA_users.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.cedalanavi.projet_IJVA500_SOA_users.CreateUserRequest;
import com.cedalanavi.projet_IJVA500_SOA_users.UpdateUserRequest;
import com.cedalanavi.projet_IJVA500_SOA_users.Entities.User;
import com.cedalanavi.projet_IJVA500_SOA_users.Repositories.UserRepository;
import com.cedalanavi.projet_IJVA500_SOA_users.exceptions.NullUserException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	public User createUser(CreateUserRequest userRequest) {
		
		
		User userExist = userRepository.findByUsername(userRequest.Username);
		
		// Test si user existe déjà + le cas du username/mdp vide
		if(userExist == null && userRequest.Username != "" && userRequest.Password != "") {
			
			User newUser = new User();
			newUser.setUsername(userRequest.Username);
			newUser.setPassword(userRequest.Password);
			
			return userRepository.save(newUser);
		} else {
			return null;
		}
		
		
	}
	
	public void deleteUser(int id) {
		userRepository.deleteById(id);
	}
	
	public void updateUser(UpdateUserRequest userRequest, int id) {
		User updatedUser = userRepository.findById(id).get();

		updatedUser.setPassword(userRequest.Password);
		
		userRepository.save(updatedUser);
		
	}
	
}
