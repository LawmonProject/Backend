package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.ChatRoom;
import com.lawmon.lawmon.pubsub.RedisSubscriber;
import com.lawmon.lawmon.repository.ChatRoomMongoRepo;
import com.lawmon.lawmon.repository.ChatRoomRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRedisRepo chatRoomRedisRepo;
  private final ChatRoomMongoRepo chatRoomMongoRepo;

  private final RedisMessageListenerContainer redisMessageListener;
  private final RedisSubscriber redisSubscriber;
  private Map<String, ChannelTopic> topics = new HashMap<>();

  public List<ChatRoom> getAllRooms() {
    // return chatRoomMongoRepo.findAll();
    return chatRoomRedisRepo.findAllRoom();
  }

  public ChatRoom getRoomById(String id) {
    return chatRoomMongoRepo.findByRoomId(id);
//    return chatRoomRedisRepo.findRoomById(id);
  }

  public ChatRoom createChatRoom(String name) {
    ChatRoom chatRoom = ChatRoom.create(name);
    chatRoomMongoRepo.save(chatRoom);
    chatRoomRedisRepo.saveChatRoom(chatRoom);
    return chatRoom;
  }

  public void enterChatRoom(String roomId) {
    ChannelTopic topic = topics.get(roomId);
    if (topic == null) {
      topic = new ChannelTopic(roomId);
      redisMessageListener.addMessageListener(redisSubscriber, topic);
      topics.put(roomId, topic);
    }
  }

  public ChannelTopic getTopic(String roomId) {
    return topics.get(roomId);
  }

  /**
   * 사용자가 전문가와 채팅하기 버튼을 누르면 전문가와 사용자의 이름으로 만들어진 채팅방 생성(1:1 채팅)
   * @param expertId 전문가 @ID
   * @param memberId 멤버 @ID
   * @return ChatRoom
   */
  public ChatRoom startChatRoomWithExpert(long expertId, long memberId) {
    ChatRoom chatRoom = ChatRoom.create(expertId + "_" + memberId);
    chatRoomMongoRepo.save(chatRoom);
    chatRoomRedisRepo.saveChatRoom(chatRoom);
    return chatRoom;
  }

  /**
   * 전문가가 자신이 속한 채팅방 목록 을 볼 수 있게
   * @param expertId 전문가 @ID
   * @return List<ChatRoom>
   */
  public List<ChatRoom> getExpertRooms(long expertId) {
    return chatRoomMongoRepo.findByNameStartingWith(expertId + "_");
  }

  public void insertBulkChatRoom() {
    List<ChatRoom> chatRooms = Arrays.asList(
      new ChatRoom()
    );
  }
}
