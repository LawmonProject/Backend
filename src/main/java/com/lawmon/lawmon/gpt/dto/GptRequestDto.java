package com.lawmon.lawmon.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptRequestDto {

  private String model;
  private List<Message> messages;

  @AllArgsConstructor
  public static class Message {

    private String role;
    private String content;

    public static Message userMessage(String content) {
      return new Message("user", content);
    }

    public static Message systemMessage(String content) {
      return new Message("system", content);
    }
  }
}
