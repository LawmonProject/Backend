package com.lawmon.lawmon.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDto {
  private String id;
  private MessageType type; // 메시지 타입
  private String roomId; // 방번호
  private String sender; // 메시지 보낸사람
  private SenderType senderType;
  private String message; // 메시지
  private LocalDateTime sentAt; // 메시지 전송 시간

  public enum MessageType {
    ENTER, TALK
  }
  public enum SenderType {
    USER, GPT
  }
}
