package com.lauchilus.websocket.chatMessages;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatMessageService chatMessageService;
	private final SimpMessagingTemplate messaginTemplate;
	
	@MessageMapping("/chat")
	public void processMEssage(@Payload ChatMessage chatMessage) {
		ChatMessage savedMSG = chatMessageService.save(chatMessage);
		messaginTemplate.convertAndSendToUser(chatMessage.getRecipientId(),"/queue/messages",ChatNotification.builder()
				.id(savedMSG.getId())
				.senderId(savedMSG.getSenderId())
				.recipientId(savedMSG.getRecipientId())
				.content(savedMSG.getContent())
				.build());
	}
	
	@GetMapping("/messages/{senderId}/{recipientId}")
	public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,@PathVariable String recipientId){
		return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
	}
	
	
}
