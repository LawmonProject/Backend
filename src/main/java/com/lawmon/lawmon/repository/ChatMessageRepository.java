package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * MongoDB
 */
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
