package com.se2gruppe5.risikobackend.sse.services;

import com.se2gruppe5.risikobackend.sse.repositories.SseSinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class SseBroadcastServiceUnitTest {
    private SseBroadcastService sseBroadcaster;
    private SseSinkRepository mockSinkRepository;

    @BeforeEach
    void setup() {
        mockSinkRepository = Mockito.mock(SseSinkRepository.class);
        sseBroadcaster = new SseBroadcastService(mockSinkRepository);
    }

    @Test
    void testAddSinkWithUUID() {
        @SuppressWarnings("unchecked")
        FluxSink<String> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        UUID returnedUuid = sseBroadcaster.addSink(uuid, sink);

        assertEquals(uuid, returnedUuid);
        Mockito.verify(mockSinkRepository).addSink(uuid, sink);
    }

    @Test
    void testAddSinkWithoutUUID() {
        @SuppressWarnings("unchecked")
        FluxSink<String> sink = Mockito.mock(FluxSink.class);
        UUID returnedUuid = sseBroadcaster.addSink(sink);

        assertNotNull(returnedUuid);
        Mockito.verify(mockSinkRepository).addSink(Mockito.any(UUID.class), Mockito.eq(sink));
    }

    @Test
    void testAddSinkWithDuplicateGeneratedUUID() {
        @SuppressWarnings("unchecked")
        FluxSink<String> sink = Mockito.mock(FluxSink.class);

        // Pretend that the generated UUID was already taken the first time but not the second time
        Mockito.when(mockSinkRepository.hasSink(Mockito.any(UUID.class))).thenReturn(true, false);
        UUID returnedUuid = sseBroadcaster.addSink(sink);

        assertNotNull(returnedUuid);
        Mockito.verify(mockSinkRepository).addSink(Mockito.any(UUID.class), Mockito.eq(sink));
    }

    @Test
    void testRemoveSink() {
        sseBroadcaster.removeSink(UUID.randomUUID());

        Mockito.verify(mockSinkRepository).removeSink(Mockito.any(UUID.class));
    }

    @Test
    void testSend() {
        @SuppressWarnings("unchecked")
        FluxSink<String> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        Mockito.when(mockSinkRepository.getSink(uuid)).thenReturn(sink);
        sseBroadcaster.send(uuid, "Message to be sent");

        Mockito.verify(sink).next("Message to be sent");
    }

    @Test
    void testSendToInvalidSink() {
        @SuppressWarnings("unchecked")
        FluxSink<String> sink = Mockito.mock(FluxSink.class);
        sseBroadcaster.addSink(sink);

        UUID uuid = UUID.randomUUID();
        Mockito.when(mockSinkRepository.getSink(uuid)).thenReturn(null);
        sseBroadcaster.send(uuid, "Message to invalid sink");

        Mockito.verify(sink, Mockito.times(0)).next("Message to invalid sink");
    }

    @Test
    void testBroadcastWithoutUUIDs() {
        @SuppressWarnings("unchecked")
        List<FluxSink<String>> sinks = List.of(
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class)
        );

        Mockito.when(mockSinkRepository.getSinks()).thenReturn(sinks);
        sseBroadcaster.broadcast("Message to be sent");

        for(FluxSink<String> sink : sinks) {
            Mockito.verify(sink).next("Message to be sent");
        }
    }

    @Test
    void testBroadcastWithUUIDs() {
        List<UUID> uuids = List.of(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        @SuppressWarnings("unchecked")
        List<FluxSink<String>> sinks = List.of(
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class)
        );

        Mockito.when(mockSinkRepository.getSinks(uuids)).thenReturn(sinks);
        sseBroadcaster.broadcast(uuids, "Message to be sent");

        for(FluxSink<String> sink : sinks) {
            Mockito.verify(sink).next("Message to be sent");
        }
    }
}
