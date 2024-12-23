package com.lawmon.lawmon.pubsub;

import com.lawmon.lawmon.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
  private final RedisTemplate<String, Object> redisTemplate;

  public void publish(ChannelTopic topic, ChatMessageDto message) {
    redisTemplate.convertAndSend(topic.getTopic(), message);
  }
}
