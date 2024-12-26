package com.lawmon.lawmon.Entity;

import com.lawmon.lawmon.dto.ChatRoomDto;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * ChatRoom은 채팅방을 기준으로 하며,
 * 채팅방에 포함된 멤버들을 참조
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_rooms")  // MongoDB의 컬렉션 이름을 지정
public class ChatRoom implements Serializable {

  @Serial
  private static final long serialVersionUID = 6494678977089006639L;

  @Id
  private String roomId;  // MongoDB에서 _id 필드를 사용
  private String name;  // 채팅방 이름
  private LocalDateTime createdAt;  // 채팅방 생성 시간
  private LocalDateTime updatedAt;
  // 추가적인 필드 (예: 생성자, 업데이트 시간 등) 필요 시 추가

  public static ChatRoom create(String name) {
    ChatRoom chatRoom = new ChatRoom();
    chatRoom.roomId = UUID.randomUUID().toString();
    chatRoom.name = name;
    chatRoom.createdAt = LocalDateTime.now();
    chatRoom.updatedAt = LocalDateTime.now();
    return chatRoom;
  }
}
