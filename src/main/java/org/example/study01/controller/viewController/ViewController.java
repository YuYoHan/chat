package org.example.study01.controller.viewController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/signup")
    private String signup() {
        return "signup";
    }

    @GetMapping("/login")
    private String login() {
        return "login";
    }

    @GetMapping("/chat-rooms")
    private String chatrooms() {
        return "chat-rooms";
    }

    @GetMapping("/chat-room")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    private String chatroom() {
        return "chat-room";
    }
}
