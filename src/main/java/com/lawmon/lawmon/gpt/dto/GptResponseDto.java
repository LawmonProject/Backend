package com.lawmon.lawmon.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GptResponseDto {

  private List<Choice> choices;

  public static class Choice {
    private int index;

    @Getter
    private Message message;

  }

  public static class Message {
    private String role;

    @Getter
    private String content;

  }
}
