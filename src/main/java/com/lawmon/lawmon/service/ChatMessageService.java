package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.ChatMessage;
import com.lawmon.lawmon.repository.ChatMessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {
  private final ChatMessageRepo chatMessageRepo;

  public List<ChatMessage> getChatMessages(String roomId) {
    return chatMessageRepo.findByRoomId(roomId);
  }

  public void addChatMessage(ChatMessage chatMessage) {
    chatMessageRepo.save(chatMessage);
    log.info("Saved chat message: {}", chatMessage.getMessage());
  }
}
