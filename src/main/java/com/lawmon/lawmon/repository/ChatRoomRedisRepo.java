package com.lawmon.lawmon.repository;

import com.lawmon.lawmon.Entity.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRedisRepo {

  private static final String CHAT_ROOMS = "CHAT_ROOM";
  private final RedisTemplate<String, Object> redisTemplate;
  private HashOperations<String, String, ChatRoom> opsHashChatRoom;

  @PostConstruct
  private void init() {
    opsHashChatRoom = redisTemplate.opsForHash();
  }

  public List<ChatRoom> findAllRoom() {
    return opsHashChatRoom.values(CHAT_ROOMS);
  }

  public ChatRoom findRoomById(String id) {
    return opsHashChatRoom.get(CHAT_ROOMS, id);
  }

  public void saveChatRoom(ChatRoom chatRoom) {
    opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
  }
}