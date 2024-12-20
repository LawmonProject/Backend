package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ChatController {

  private final SimpMessageSendingOperations messagingTemplate;

  //MessageMapping 을 통해 WebSocket으로 들어오는 메시지 발행을 처리
  @MessageMapping("/chat/message")
  public void message(ChatMessageDto message) {
    if (ChatMessageDto.MessageType.ENTER.equals(message.getType()))
      message.setMessage(message.getSender() + "님이 입장하셨습니다.");
    messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
  }
}