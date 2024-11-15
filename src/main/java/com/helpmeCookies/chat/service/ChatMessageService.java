package com.helpmeCookies.chat.service;


import com.helpmeCookies.chat.dto.ChatMessageDto;
import com.helpmeCookies.chat.dto.ChatRoomInfo;
import com.helpmeCookies.chat.entity.ChatMessage;
import com.helpmeCookies.chat.entity.ChatRoom;
import com.helpmeCookies.chat.entity.MessageType;
import com.helpmeCookies.chat.repository.ChatMessageRepository;
import com.helpmeCookies.chat.repository.ChatRoomRepository;
import com.helpmeCookies.chat.util.ImageStorageUtil;
import com.helpmeCookies.global.exception.user.UserNotFoundException;
import com.helpmeCookies.user.entity.User;
import com.helpmeCookies.user.repository.UserRepository;
import com.helpmeCookies.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ImageStorageUtil imageStorageUtil;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    @Transactional
    public ChatMessage saveMessage(Long chatRoomId, ChatMessageDto messageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));

        User sender = userService.findByEmail(messageDto.getSender())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        ChatMessage message = new ChatMessage(chatRoom, sender, messageDto.getContent(), messageDto.getMessageType());

        return chatMessageRepository.save(message);
    }

    @Transactional
    public ChatMessage saveFileMessage(Long chatRoomId, String userEmail, String fileBase64) throws IOException, UserNotFoundException {
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        User sender = userService.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("발신자를 찾을 수 없습니다: " + userEmail));

        String folderPath = "chatRoom_" + chatRoom.getId();
        String imageUrl = imageStorageUtil.saveBase64Image(fileBase64, folderPath);  // 공통 메서드를 사용하여 저장

        ChatMessage message = new ChatMessage(chatRoom, sender, imageUrl, MessageType.IMAGE);

        return chatMessageRepository.save(message);
    }

    public byte[] convertImageUrlToBytes(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        return Files.readAllBytes(imageFile.toPath());
    }

    public String convertBytesToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public void deleteMessage(Long messageId) {
        chatMessageRepository.deleteById(messageId);
    }

    public List<ChatMessage> getMessagesByChatRoom(ChatRoom chatRoom) {
        return chatMessageRepository.findByChatRoomOrderByTimestampAsc(chatRoom);
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

}
