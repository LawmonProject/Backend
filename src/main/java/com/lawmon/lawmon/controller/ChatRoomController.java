package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.chatroom.ChatRoomDto;
import com.lawmon.lawmon.Entity.ChatRoom;
import com.lawmon.lawmon.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ChatRoom API", description = "채팅방 API")
public class ChatRoomController {

  //  private final ChatRoomRedisRepo chatRoomRepository;
  private final ChatRoomService chatRoomService;

  /**
   * 채팅 리스트 화면
   * @param model
   * @return 채팅 리스트 화면
   */
  @GetMapping("/room")
  public String rooms(Model model) {
    return "/chat/room";
  }

  /**
   * 모든 채팅방 목록 반환
   * @return List<ChatRoomDto>
   */
  @GetMapping("/rooms")
  @ResponseBody
  @Operation(summary = "채팅방 목록 조회", description = "모든 채팅방 목록을 조회합니다.")
  public List<ChatRoomDto> room() {
    List<ChatRoom> chatRooms =  chatRoomService.getAllRooms();
    return chatRooms.stream()
      .map(chatRoom -> ChatRoomDto.builder()
        .name(chatRoom.getName())
        .roomId(chatRoom.getRoomId())
        .build())  // 필요한 필드들로 변환
      .collect(Collectors.toList());
  }

  /**
   * 채팅방 생성
   * @param name
   * @return ChatRoomDto
   */
  @PostMapping("/rooms")
  @ResponseBody
  @Operation(summary = "채팅방 생성(테스트용)", description = "채팅방을 생성합니다(테스트용)")
  public ChatRoomDto createRoom(@RequestParam String name) {
    ChatRoom chatRoom =  chatRoomService.createChatRoom(name);
    return ChatRoomDto.builder()
      .name(chatRoom.getName())
      .roomId(chatRoom.getRoomId())
      .build();
  }

  /**
   * 채팅방 입장 화면
   * @param model
   * @param roomId
   * @return 채팅방 입장 화면
   */
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
  @GetMapping("/rooms/{roomId}")
  @ResponseBody
  @Operation(summary = "채팅방 정보 조회", description = "채팅방 정보(roodId, name)를 조회합니다.")
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
   * @param expertId
   * @param memberId
   * @return ;
   */
  @PostMapping("/rooms/expert")
  @ResponseBody
  @Operation(summary = "채팅방 생성", description = "expertId, memberId로 채팅방을 생성합니다.채팅방 이름 : (expertId_memberId)")
  public ResponseEntity<ChatRoomDto> startChatRoomWithExpert(@RequestParam long expertId, @RequestParam long memberId) {
    ChatRoom chatRoom = chatRoomService.startChatRoomWithExpert(expertId, memberId);
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
  @Operation(summary = "채팅방 조회(전문가용)", description = "자신이 속한 채팅방을 조회합니다(전문가용).")
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
