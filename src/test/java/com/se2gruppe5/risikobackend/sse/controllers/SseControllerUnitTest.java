package com.se2gruppe5.risikobackend.sse.controllers;

import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

class SseControllerUnitTest {
    private SseController sseController;
    private SseBroadcastService sseBroadcaster;

    @BeforeEach
    void setup() {
        sseBroadcaster = Mockito.mock(SseBroadcastService.class);
        sseController = new SseController(sseBroadcaster);
    }

    @Test
    void testStream() {
        UUID testUuid = UUID.randomUUID();
        Mockito.when(sseBroadcaster.addSink(Mockito.any())).thenReturn(testUuid);

        Flux<String> stream = sseController.stream();

        // Verify that the Flux has been created and that the next message is the UUID
        StepVerifier.create(stream)
                .expectNext(testUuid.toString())
                .thenCancel()
                .verify();

        Mockito.verify(sseBroadcaster).addSink(Mockito.any());
    }
}
