package com.climbing.api.chat.controller;

import com.climbing.api.chat.ChatRoom;
import com.climbing.api.chat.service.ChatService;
import com.climbing.auth.login.GetLoginMember;
import com.climbing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;
    private final MemberService memberService;

    @GetMapping("/room")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllChatRooms();
    }

    @PostMapping("/room/{gymId}")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom createChatRoom(@PathVariable Long gymId) throws Exception {
        String email = GetLoginMember.getLoginMemberEmail();
        String nickname = memberService.findMemberEmailToNickname(email);
        String createDate = String.valueOf(LocalDateTime.now());
        return chatService.createChatRoom(nickname, gymId, createDate);
    }

    @GetMapping("/room/{roomId}")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Long roomId) {
        return chatService.findChatRoomById(roomId);
    }

    @GetMapping("/room-check/{nickname}/{gymId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean isRoomExits(@PathVariable String nickname, @PathVariable Long gymId) {
        return chatService.isRoomExistsByNicknameAndGymId(nickname, gymId);
    }
}
