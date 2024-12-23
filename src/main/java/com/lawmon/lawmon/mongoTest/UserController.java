package com.lawmon.lawmon.mongoTest;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;

  public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping
  public User createUser(@RequestBody User user) {
    return userRepository.save(user);
  }

  @GetMapping
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}

