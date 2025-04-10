package com.se2gruppe5.risikobackend.sse.controllers;

import com.google.gson.Gson;
import com.se2gruppe5.risikobackend.sse.MessageType;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

class SseControllerUnitTest {
    private SseController sseController;
    private SseBroadcastService sseBroadcaster;

    @BeforeEach
    void setup() {
        sseBroadcaster = Mockito.mock(SseBroadcastService.class);
        sseController = new SseController(sseBroadcaster);

        // Fix gson being null
        ReflectionTestUtils.setField(sseBroadcaster, "gson", new Gson());
    }

    @Test
    void testStream() {
        UUID testUuid = UUID.randomUUID();
        Mockito.when(sseBroadcaster.addSink(Mockito.any())).thenReturn(testUuid);
        Mockito.doCallRealMethod().when(sseBroadcaster).send(Mockito.<FluxSink<ServerSentEvent<String>>>any(), Mockito.any());

        Flux<ServerSentEvent<String>> stream = sseController.stream();

        // Verify that the Flux has been created and that the next message is the UUID
        StepVerifier.create(stream)
                .expectNext(ServerSentEvent.<String>builder()
                        .event(MessageType.SET_UUID.name())
                        .data(Base64.getEncoder().encodeToString(("{\"uuid\":\"" + testUuid + "\"}").getBytes(StandardCharsets.UTF_8)))
                        .build())
                .thenCancel()
                .verify();

        Mockito.verify(sseBroadcaster).addSink(Mockito.any());
    }
}
