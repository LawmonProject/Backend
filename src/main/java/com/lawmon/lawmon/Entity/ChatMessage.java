package com.lawmon.lawmon.Entity;

import com.lawmon.lawmon.dto.ChatMessageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "chat")
public class ChatMessage {

    @Id
    private Long id;

    private ChatMessageDto.MessageType type;
    private String roomId;
    private String sender;  //채팅을 보낸 사람
    private SenderType senderType;
    private String message;

    @Column(name = "sent_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime sentAt;

    public enum SenderType {
        USER, GPT
    }
}