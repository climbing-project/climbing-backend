package com.climbing.api.chat;

import com.climbing.auth.login.GetLoginMember;
import com.climbing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;
    private final MemberService memberService;

    @GetMapping("/room")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    @PostMapping("/room/{gymId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom createChatRoom(@PathVariable Long gymId) throws Exception {
        String email = GetLoginMember.getLoginMemberEmail();
        String nickname = memberService.findMemberEmailToNickname(email);
        return chatService.createChatRoom(nickname, gymId);
    }

    @GetMapping("/room/{roomId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }

    @GetMapping("/room-check/{nickname}/{gymId}")
    @ResponseBody
    public boolean isRoomExits(@PathVariable String nickname, @PathVariable Long gymId) {
        return chatService.isRoomExistByNicknameAndGymId(nickname, gymId);
    }
}
