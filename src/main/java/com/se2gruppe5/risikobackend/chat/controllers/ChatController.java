package com.se2gruppe5.risikobackend.chat.controllers;

import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
    private final SseBroadcastService sseBroadcaster;

    @Autowired
    public ChatController(SseBroadcastService sseBroadcaster) {
        this.sseBroadcaster = sseBroadcaster;
    }

    @PostMapping("/chat/send")
    public void chat(@RequestParam String message) {
        System.out.println("Received message: " + message);
        sseBroadcaster.broadcast(message);
    }
}
