package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.ChatMessage;
import com.lawmon.lawmon.dto.chatmessage.ChatMessageDto;
import com.lawmon.lawmon.repository.ChatMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepo chatMessageRepo;

  public List<ChatMessage> getChatMessages(String roomId) {
    return chatMessageRepo.findByRoomIdOrderByTimestampDesc(roomId);
  }

  public void addChatMessage(ChatMessage chatMessage) {
    chatMessageRepo.save(chatMessage);
    log.info("Saved chat message: {}", chatMessage.getMessage());
  }

  public void insertBulkChatMessages(String roomId, int repetition) {
    List<ChatMessage> chatMessages = new ArrayList<>();
    for (int i = 0; i < repetition; i++) {
      chatMessages.add(ChatMessage.builder()
        .type(ChatMessageDto.MessageType.TALK)
        .roomId(roomId)
        .sender("Bulk Insert Test")
        .message("Test - " + i)
        .build());
    }

    chatMessageRepo.saveAll(chatMessages);
  }
}
