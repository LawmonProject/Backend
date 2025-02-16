//package com.lawmon.lawmon.Entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
///**
// * ChatRoomMember는 특정 멤버가 어떤 채팅방에 속해 있는지를 표현
// */
//@Getter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//public class ChatRoomMember {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//  private String roomId;
//  @ManyToOne
//  @JoinColumn(name = "member_id")
//  private Member;
//
//  private LocalDateTime enterTime;
//}
