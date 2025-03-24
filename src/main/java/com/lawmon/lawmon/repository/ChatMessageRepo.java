package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * MongoDB
 */
public interface ChatMessageRepo extends MongoRepository<ChatMessage, String> {
  List<ChatMessage> findByRoomId(String roomId);

  // GPT 요약용 (오래된 → 최신)
  List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

  // 채팅방 UI용 (최신 → 오래된)
  List<ChatMessage> findByRoomIdOrderByTimestampDesc(String roomId);
}
