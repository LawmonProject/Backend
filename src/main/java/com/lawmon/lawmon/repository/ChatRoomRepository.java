package com.lawmon.lawmon.repository;


import com.lawmon.lawmon.dto.ChatRoomDto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ChatRoomRepository {

  private Map<String, ChatRoomDto> chatRoomMap;

  @PostConstruct
  private void init() {
    chatRoomMap = new LinkedHashMap<>();
  }

  public List<ChatRoomDto> findAllRoom() {
    // 채팅방 생성순서 최근 순으로 반환
    List<ChatRoomDto> chatRooms = new ArrayList<>(chatRoomMap.values());
    Collections.reverse(chatRooms);
    return chatRooms;
  }

  public ChatRoomDto findRoomById(String id) {
    return chatRoomMap.get(id);
  }

  public ChatRoomDto createChatRoom(String name) {
    ChatRoomDto chatRoomDto = ChatRoomDto.create(name);
    chatRoomMap.put(chatRoomDto.getRoomId(), chatRoomDto);
    return chatRoomDto;
  }
}
