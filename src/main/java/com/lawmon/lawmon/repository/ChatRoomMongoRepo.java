package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomMongoRepo extends MongoRepository<ChatRoom, String> {

  List<ChatRoom> findAll();
  ChatRoom findByRoomId(String id);
  List<ChatRoom> findByNameStartingWith(String prefix);
}
