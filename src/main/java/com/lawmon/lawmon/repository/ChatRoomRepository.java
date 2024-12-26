package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB
 */
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
  Optional<ChatRoom> findByRoomId(String roomId);
}
