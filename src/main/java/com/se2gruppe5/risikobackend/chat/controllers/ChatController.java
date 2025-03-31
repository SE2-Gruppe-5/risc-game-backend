package com.se2gruppe5.risikobackend.chat.controllers;

import com.se2gruppe5.risikobackend.sse.SseBroadcaster;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
    @PostMapping("/chat/send")
    public void chat(@RequestParam String message) {
        System.out.println("Received message: " + message);
        SseBroadcaster.broadcast(message);
    }
}
