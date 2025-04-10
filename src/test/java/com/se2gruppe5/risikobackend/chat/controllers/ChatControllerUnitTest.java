package com.se2gruppe5.risikobackend.chat.controllers;

import com.se2gruppe5.risikobackend.chat.messages.ChatMessage;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

class ChatControllerUnitTest {
    private ChatController chatController;
    private SseBroadcastService sseBroadcaster;

    @BeforeEach
    void setup() {
        sseBroadcaster = Mockito.mock(SseBroadcastService.class);
        chatController = new ChatController(sseBroadcaster);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Message to be sent", "Hello world!", "Another Example"})
    void testSendMessage(String s) {
        chatController.chat(s);
        Mockito.verify(sseBroadcaster, Mockito.times(1)).broadcast(new ChatMessage(s));
    }
}
