package com.helpmeCookies.chat.service;


import com.helpmeCookies.chat.dto.ChatRoomInfo;
import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.repository.ChatMessageRepository;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ImageStorageUtil imageStorageUtil;

    public ChatMessage saveMessage(ChatRoom chatRoom, User sender, String content) {
        ChatMessage message = new ChatMessage(chatRoom, sender, content);
        return chatMessageRepository.save(message);
    }

    public void deleteMessage(Long messageId) {
        chatMessageRepository.deleteById(messageId);
    }

    public ChatMessage saveFileMessage(ChatRoom chatRoom, User sender, MultipartFile file) throws IOException {
        // 파일 저장 및 URL 반환
        String folderPath = "chatRoom_" + chatRoom.getId();
        String imageUrl = imageStorageUtil.saveChatImage(file, folderPath);

        // 메시지 생성 (URL만 포함)
        ChatMessage message = new ChatMessage(chatRoom, sender, "image", imageUrl);

        return chatMessageRepository.save(message); // 메시지 저장
    }

    // 특정 채팅방의 메시지를 시간 순서대로 가져오는 메서드
    public List<ChatMessage> getMessagesByChatRoom(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    // 특정 유저의 채팅룸 ID를 반환하는 메서드
    public List<Long> getChatRoomIdsByUser(User user) {
        List<ChatMessage> messages = chatMessageRepository.findBySender(user);
        return messages.stream()
                .map(message -> message.getChatRoom().getId())
                .distinct() // 중복 제거
                .collect(Collectors.toList());
    }

    // 특정 유저의 채팅룸 ID와 제목을 반환하는 메서드
    public List<ChatRoomInfo> getChatRoomInfosByUser(User user) {
        return chatMessageRepository.findChatRoomInfosByUser(user);
    }

}
