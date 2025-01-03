package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * MongoDB
 */
public interface ChatMessageRepo extends MongoRepository<ChatMessage, String> {
  List<ChatMessage> findByRoomId(String roomId);
}
