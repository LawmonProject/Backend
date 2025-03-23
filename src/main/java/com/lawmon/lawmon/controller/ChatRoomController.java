package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.ChatRoomDto;
import com.lawmon.lawmon.Entity.ChatRoom;
import com.lawmon.lawmon.dto.ChatStartRequest;
import com.lawmon.lawmon.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

  //  private final ChatRoomRedisRepo chatRoomRepository;
  private final ChatRoomService chatRoomService;

  // 채팅 리스트 화면
  @GetMapping("/room")
  public String rooms(Model model) {
    return "/chat/room";
  }
  // 모든 채팅방 목록 반환
  @GetMapping("/rooms")
  @ResponseBody
  public List<ChatRoomDto> room() {
    List<ChatRoom> chatRooms =  chatRoomService.getAllRooms();
    return chatRooms.stream()
      .map(chatRoom -> ChatRoomDto.builder()
        .name(chatRoom.getName())
        .roomId(chatRoom.getRoomId())
        .build())  // 필요한 필드들로 변환
      .collect(Collectors.toList());
  }

  // 채팅방 생성
  @PostMapping("/room")
  @ResponseBody
  public ChatRoomDto createRoom(@RequestParam String name) {
    ChatRoom chatRoom =  chatRoomService.createChatRoom(name);
    return ChatRoomDto.builder()
      .name(chatRoom.getName())
      .roomId(chatRoom.getRoomId())
      .build();
  }
  // 채팅방 입장 화면
  @GetMapping("/room/enter/{roomId}")
  public String roomDetail(Model model, @PathVariable String roomId) {
    model.addAttribute("roomId", roomId);
    return "/chat/roomdetail";
  }

  /**
   * 채팅방 정보 조회
   * @param roomId RoomId
   * @return RoomId, Name
   */
  @GetMapping("/room/{roomId}")
  @ResponseBody
  public ResponseEntity<ChatRoomDto> roomInfo(@PathVariable String roomId) {
    ChatRoom chatRoom =  chatRoomService.getRoomById(roomId);
    return ResponseEntity.ok(ChatRoomDto.builder()
      .name(chatRoom.getName())
      .roomId(chatRoom.getRoomId())
      .build());
  }

  /**
   * 사용자가 전문가와 채팅하기 버튼을 누르면 전문가와 사용자의 @ID로 만들어진 채팅방 생성(1:1 채팅)
   * 채팅방 이름 : ${ExpertId_MemberId}
   * @param chatStartRequest ;
   * @return ;
   */
  @PostMapping("/room/expert")
  @ResponseBody
  public ResponseEntity<ChatRoomDto> startChatRoomWithExpert(@RequestBody ChatStartRequest chatStartRequest) {
    ChatRoom chatRoom = chatRoomService.startChatRoomWithExpert(chatStartRequest.getExpertId(), chatStartRequest.getMemberId());
    return ResponseEntity.ok(ChatRoomDto.builder()
      .name(chatRoom.getName())
      .roomId(chatRoom.getRoomId())
      .build());
  }

  /**
   * 전문가가 자신이 속한 채팅방 목록 을 볼 수 있게
   * @param expertId 전문가 @ID
   * @return list of [RoomId, Name]
   */
  @GetMapping("/rooms/{expertId}")
  @ResponseBody
  public ResponseEntity<List<ChatRoomDto>> room(@PathVariable long expertId) {
    List<ChatRoom> chatRooms =  chatRoomService.getExpertRooms(expertId);
    return ResponseEntity.ok(chatRooms.stream()
      .map(chatRoom -> ChatRoomDto.builder()
        .name(chatRoom.getName())
        .roomId(chatRoom.getRoomId())
        .build())
      .collect(Collectors.toList()));
  }
}
