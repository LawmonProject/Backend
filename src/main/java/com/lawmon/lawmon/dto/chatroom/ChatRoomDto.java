package com.lawmon.lawmon.dto.chatroom;

import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
public class ChatRoomDto implements Serializable {

  @Serial
  private static final long serialVersionUID = 6494678977089006639L;

  private String roomId;
  private String name;
}
