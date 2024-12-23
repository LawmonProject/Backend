package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.ChatRoom;
import com.lawmon.lawmon.dto.ChatRoomDto;
import com.lawmon.lawmon.pubsub.RedisSubscriber;
import com.lawmon.lawmon.repository.ChatRoomRedisRepository;
import com.lawmon.lawmon.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ChatRoomService {
  private final ChatRoomRedisRepository chatRoomRedisRepository; //Redis 연동
  private final ChatRoomRepository chatRoomRepository;  //MongoDB 연동

  @Autowired
  public ChatRoomService(ChatRoomRedisRepository chatRoomRedisRepository, ChatRoomRepository chatRoomRepository) {
    this.chatRoomRedisRepository = chatRoomRedisRepository;
    this.chatRoomRepository = chatRoomRepository;
  }
  // 채팅방 생성 서비스
  public ChatRoom createChatRoom(String name) {
    // 1. MongoDB에 채팅방 저장
    ChatRoom chatRoom = ChatRoom.builder()
      .name(name)
      .createdAt(LocalDateTime.now())
      .updatedAt(LocalDateTime.now())
      .build();
    chatRoomRepository.save(chatRoom);

    // 2. Redis에 채팅방 생성
    return chatRoomRedisRepository.createChatRoom(name);
  }

  // MongoDB에서 모든 채팅방 조회
  public List<ChatRoom> getAllChatRoomsFromDB() {
    return chatRoomRedisRepository.findAllRoom();
  }

  // Redis에서 모든 채팅방 조회
  public List<ChatRoom> getAllChatRoomsFromRedis() {
    return chatRoomRedisRepository.findAllRoom();
  }

  // MongoDB에서 채팅방 ID로 조회
  public ChatRoom getChatRoomByIdFromDB(String roomId) {
    return chatRoomRedisRepository.findRoomById(roomId);
  }

  // Redis에서 채팅방 ID로 조회
  public ChatRoom getChatRoomByIdFromRedis(String roomId) {
    return chatRoomRedisRepository.findRoomById(roomId);
  }

  // 채팅방 입장 서비스
  public void enterChatRoom(String roomId) {
    chatRoomRedisRepository.enterChatRoom(roomId);
  }
}
