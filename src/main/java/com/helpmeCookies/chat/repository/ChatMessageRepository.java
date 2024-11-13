package com.helpmeCookies.chat.repository;


import com.helpmeCookies.chat.dto.ChatRoomInfo;
import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 채팅방의 메시지들을 시간순으로 정렬하여 반환하는 메서드
    List<ChatMessage> findByChatRoomOrderByTimestampAsc(ChatRoom chatRoom);
    //특정 유저의 채팅룸 ID를 조회
    List<ChatMessage> findBySender(User sender);

    //특정 유저의 채팅룸 id와 제목을 같이 반환한다.
    @Query("SELECT new com.helpmeCookies.chat.dto.ChatRoomInfo(cm.chatRoom.id, cm.chatRoom.title) FROM ChatMessage cm WHERE cm.sender = :user GROUP BY cm.chatRoom.id, cm.chatRoom.title")
    List<ChatRoomInfo> findChatRoomInfosByUser(User user);
}
