package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.ChatMessage;
import com.lawmon.lawmon.dto.ChatMessageDto;
import com.lawmon.lawmon.dto.RoomRequest;
import com.lawmon.lawmon.pubsub.RedisPublisher;
import com.lawmon.lawmon.service.ChatMessageService;
import com.lawmon.lawmon.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class ChatController {

  private final RedisPublisher redisPublisher;
  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;
//  private final ChatRoomRedisRepo chatRoomRepository;

  /**
   * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
   */
  @MessageMapping("/chat/message")
  public void message(ChatMessageDto messageDto) {
    if (ChatMessageDto.MessageType.ENTER.equals(messageDto.getType())) {
      chatRoomService.enterChatRoom(messageDto.getRoomId());
      messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
    }
    // Websocket에 발행된 메시지를 redis로 발행한다(publish)
    redisPublisher.publish(chatRoomService.getTopic(messageDto.getRoomId()), messageDto);
  }

  /**
   * 채팅방 내의 메세지 조회
   * @param roomRequest ;
   * @return List<ChatMessageDto>
   */
  @PostMapping("/chat/get")
  @ResponseBody
  public ResponseEntity<List<ChatMessageDto>> get(@RequestBody RoomRequest roomRequest) {
    List<ChatMessage> chatMessages = chatMessageService.getChatMessages(roomRequest.getRoomId());
    return ResponseEntity.ok(chatMessages.stream()
      .map(chatMessage -> ChatMessageDto.builder()
        .type(chatMessage.getType())
        .roomId(chatMessage.getRoomId())
        .sender(chatMessage.getSender())
        .message(chatMessage.getMessage())
        .timestamp(chatMessage.getTimestamp())
        .build())
      .collect(Collectors.toList()));
  }
}
