package com.lawmon.lawmon.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatRoomDto {
  private String roomId;
  private String name;

  public static ChatRoomDto create(String name) {
    ChatRoomDto chatRoomDto = new ChatRoomDto();
    chatRoomDto.roomId = UUID.randomUUID().toString();
    chatRoomDto.name = name;
    return chatRoomDto;
  }
}
