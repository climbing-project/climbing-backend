package com.climbing.api.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping("/rooms")
    //@PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatService.findAllRoom();
    }

    @PostMapping("/room")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom createChatRoom(@RequestParam String name) {
        return chatService.createChatRoom(name);
    }

    @GetMapping("/room/enter/{roomId}") //채팅방 입장
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    public String enterRoom(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/enterRoom";
    }

    @GetMapping("/room/{roomId}")
    //@PreAuthorize("hasRole('USER'||'ADMIN' || 'MANAGER')")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findById(roomId);
    }
}
