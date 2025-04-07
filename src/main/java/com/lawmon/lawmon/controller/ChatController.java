package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.ChatMessage;
import com.lawmon.lawmon.dto.chatmessage.ChatMessageDto;
import com.lawmon.lawmon.pubsub.RedisPublisher;
import com.lawmon.lawmon.service.ChatMessageService;
import com.lawmon.lawmon.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1")
@Tag(name = "Chat API", description = "채팅 API")
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
   * @param roomId
   * @return List<ChatMessageDto>
   */
  @PostMapping("/chat/get")
  @ResponseBody
  @Operation(summary = "채팅방 메세지 조회", description = "채팅방의 메세지를 조회합니다.(최신순)")
  public ResponseEntity<List<ChatMessageDto>> get(@RequestParam String roomId) {
    List<ChatMessage> chatMessages = chatMessageService.getChatMessages(roomId);
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



  /**
   * MongoDB Bulk Insert
   * @param roomId
   * @param repetition
   */
  @GetMapping("/chat/bulkinsert")
  @ResponseBody
  @Operation(summary = "MongoDB Bulk Insert", description = "MongoDB indexing 성능 테스트를 위해 bulk insert : index -> data의 개수")
  public void bulkInsert(@RequestParam String roomId, @RequestParam int repetition) {
    chatMessageService.insertBulkChatMessages(roomId, repetition);
  }
}
