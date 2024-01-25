package com.lauchilus.websocket.chatMessages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lauchilus.websocket.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository chatRepo;
	private final ChatRoomService chatService;
	
	public ChatMessage save(ChatMessage chatMessage) {
		var chatId = chatService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true).orElseThrow(); //CREATE EXC
		chatMessage.setChatId(chatId);
		chatRepo.save(chatMessage);
		return chatMessage;
	}
	
	public List<ChatMessage> findChatMessages(String senderId,String recipientId){
		var chatId = chatService.getChatRoomId(senderId, recipientId, false);
		return chatId.map(chatRepo::findByChatId).orElse(new ArrayList<>());
	}
}
