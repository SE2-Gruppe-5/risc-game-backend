package com.se2gruppe5.risikobackend.chat.controllers;

import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ChatControllerUnitTest {
    private ChatController chatController;
    private SseBroadcastService sseBroadcaster;

    @BeforeEach
    void setup() {
        sseBroadcaster = Mockito.mock(SseBroadcastService.class);
        chatController = new ChatController(sseBroadcaster);
    }

    @Test
    void testSendMessage() {
        chatController.chat("Hello world!");
        Mockito.verify(sseBroadcaster).broadcast("Hello world!");
    }
}
