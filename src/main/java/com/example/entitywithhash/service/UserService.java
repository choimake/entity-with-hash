package com.example.entitywithhash.service;

import com.example.entitywithhash.entity.User;
import com.example.entitywithhash.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void insertUser(String userId, String userName) {
    var user = new User(userId, userName);
    userRepository.save(user);
  }

  public User findByUserId(String userId) {
    return userRepository.findById(userId);
  }

}
