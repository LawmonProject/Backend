package com.lawmon.lawmon.Entity;

import com.lawmon.lawmon.dto.chatmessage.ChatMessageDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Builder
@Document("chat")
@CompoundIndex(name="roomId_timestamp_index", def = "{'roomId': 1, 'timestamp': -1}")
public class ChatMessage {
    // 메시지 타입 : 입장, 채팅
    private ChatMessageDto.MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지

    @Builder.Default
    private LocalDateTime timestamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();  // 메시지 전송 시간
}
