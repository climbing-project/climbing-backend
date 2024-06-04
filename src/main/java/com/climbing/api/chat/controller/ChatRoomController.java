package com.climbing.api.chat.controller;

import com.climbing.api.chat.response.ChatMessageResponse;
import com.climbing.api.chat.response.ChatRoomResponse;
import com.climbing.api.chat.service.ChatService;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;
    private final MemberService memberService;

    @GetMapping("/room")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ChatRoomResponse>> getChatRoomList() {
        List<ChatRoomResponse> responses = chatService.findAllChatRooms();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/room/{gymId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    public ResponseEntity<ChatRoomResponse> createChatRoom(@PathVariable Long gymId) throws Exception {
        String email = GetLoginMember.getLoginMemberEmail();
        String nickname = memberService.findMemberEmailToNickname(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChatRoom(nickname, gymId));
    }

    @GetMapping("/room/{roomId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    public ResponseEntity<ChatRoomResponse> roomInfo(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.findChatRoomById(roomId));
    }

    @GetMapping("/room-check/{nickname}/{gymId}")
    public ResponseEntity<Boolean> isRoomExists(@PathVariable String nickname, @PathVariable Long gymId) {
        return ResponseEntity.ok(chatService.isRoomExistsByNicknameAndGymId(nickname, gymId));
    }

    @GetMapping("/find/message/{roomId}")
    public Mono<ResponseEntity<List<ChatMessageResponse>>> findMessages(@PathVariable Long roomId) {
        Flux<ChatMessageResponse> responseFlux = chatService.findChatMessages(roomId);
        return responseFlux.collectList().map(ResponseEntity::ok);
    }
}
