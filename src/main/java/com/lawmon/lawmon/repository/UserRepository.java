package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.mongoTest.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  User findByName(String name);
}

