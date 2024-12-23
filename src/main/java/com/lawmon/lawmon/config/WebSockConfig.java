package com.lawmon.lawmon.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

//Stomp 사용 X
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSocket
//public class WebSockConfig implements WebSocketConfigurer {
//  private final WebSocketHandler webSocketHandler;
//
//  @Override
//  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//    registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
//  }
//}

@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker("/sub");
    config.setApplicationDestinationPrefixes("/pub");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws-stomp")
      //배포 환경에서는 실제 도메인을 설정
      .setAllowedOriginPatterns("*")
      .withSockJS();
  }
}