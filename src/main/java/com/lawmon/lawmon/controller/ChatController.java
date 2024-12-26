package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.ChatMessageDto;
import com.lawmon.lawmon.pubsub.RedisPublisher;
import com.lawmon.lawmon.repository.ChatRoomRedisRepository;
import com.lawmon.lawmon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@RequiredArgsConstructor
@Controller
public class ChatController {

  private final RedisPublisher redisPublisher;
  private final ChatRoomRedisRepository chatRoomRedisRepository;
  private final ChatService chatService;

  /**
   * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
   */
  @MessageMapping("/chat/message")
  public void message(ChatMessageDto message) {
    if (ChatMessageDto.MessageType.ENTER.equals(message.getType())) {
      chatRoomRedisRepository.enterChatRoom(message.getRoomId());
      message.setMessage(message.getSender() + "님이 입장하셨습니다.");
    }
    // Websocket에 발행된 메시지를 redis로 발행한다(publish)
    redisPublisher.publish(chatRoomRedisRepository.getTopic(message.getRoomId()), message);
  }
}