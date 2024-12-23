package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.ChatRoom;
import com.lawmon.lawmon.dto.ChatRoomDto;
import com.lawmon.lawmon.repository.ChatRoomRedisRepository;
import com.lawmon.lawmon.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @Autowired
  public ChatRoomController(ChatRoomService chatRoomService) {
    this.chatRoomService = chatRoomService;
  }

  // 채팅 리스트 화면
  @GetMapping("/room")
  public String rooms(Model model) {
    return "/chat/room";
  }
  // 모든 채팅방 목록 반환
  @GetMapping("/rooms")
  @ResponseBody
  public List<ChatRoom> room() {
    return chatRoomService.getAllChatRoomsFromRedis();
  }
  // 채팅방 생성
  @PostMapping("/room")
  @ResponseBody
  public ChatRoomDto createRoom(@RequestParam String name) {
    ChatRoom chatRoom = chatRoomService.createChatRoom(name);
    return ChatRoomDto.builder()
      .roomId(chatRoom.getRoomId())
      .name(chatRoom.getName())
      .build();
  }
  // 채팅방 입장 화면
  @GetMapping("/room/enter/{roomId}")
  public String roomDetail(Model model, @PathVariable String roomId) {
    model.addAttribute("roomId", roomId);
    return "/chat/roomdetail";
  }
  // 특정 채팅방 조회
  @GetMapping("/room/{roomId}")
  @ResponseBody
  public ChatRoomDto roomInfo(@PathVariable String roomId) {
    ChatRoom chatRoom = chatRoomService.getChatRoomByIdFromRedis(roomId);
    return ChatRoomDto.builder()
      .roomId(chatRoom.getRoomId())
      .name(chatRoom.getName())
      .build();
  }
}