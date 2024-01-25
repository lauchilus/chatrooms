package com.lauchilus.websocket.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	
	private final ChatRoomRepository chatRepo;

	public Optional<String> getChatRoomId(String senderId,String recipientID,boolean createNewRoomIfNotExists){
		return chatRepo.findBySenderIdAndRecipientId(senderId,recipientID)
				.map(ChatRoom::getChatId)
				.or(() -> {
					if(createNewRoomIfNotExists) {
						var chatId = createChatId(senderId,recipientID);
						return Optional.of(chatId);
					}
					return Optional.empty();
				});
	}

	private String createChatId(String senderId, String recipientID) {
		var chatID = String.format("%s_%s",senderId,recipientID);
		ChatRoom senderRecipient = ChatRoom.builder()
				.chatId(chatID)
				.senderId(senderId)
				.recipientId(recipientID)
				.build();
		
		ChatRoom recipientSender = ChatRoom.builder()
				.chatId(chatID)
				.senderId(recipientID)
				.recipientId(senderId)
				.build();
		chatRepo.save(senderRecipient);
		chatRepo.save(recipientSender);
		return chatID;
	}
}
