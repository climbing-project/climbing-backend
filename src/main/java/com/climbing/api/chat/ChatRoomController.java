package com.climbing.api.chat;

import com.climbing.auth.login.GetLoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping("/room")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    @PostMapping("/room/{gymId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom createChatRoom(@PathVariable Long gymId) {
        String email = GetLoginMember.getLoginMemberEmail();
        return chatService.createChatRoom(email, gymId);
    }

    @GetMapping("/room/{roomId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}
