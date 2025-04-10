package com.se2gruppe5.risikobackend.sse.services;

import com.se2gruppe5.risikobackend.chat.messages.ChatMessage;
import com.se2gruppe5.risikobackend.sse.MessageType;
import com.se2gruppe5.risikobackend.sse.repositories.SseSinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        UUID returnedUuid = sseBroadcaster.addSink(uuid, sink);

        assertEquals(uuid, returnedUuid);
        Mockito.verify(mockSinkRepository).addSink(uuid, sink);
    }

    @Test
    void testAddSinkWithoutUUID() {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);
        UUID returnedUuid = sseBroadcaster.addSink(sink);

        assertNotNull(returnedUuid);
        Mockito.verify(mockSinkRepository).addSink(Mockito.any(UUID.class), Mockito.eq(sink));
    }

    @Test
    void testAddSinkWithDuplicateGeneratedUUID() {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);

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

    @ParameterizedTest
    @ValueSource(strings = {"Message to be sent", "Hello world!", "Another Example"})
    void testSend(String s) {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        Mockito.when(mockSinkRepository.getSink(uuid)).thenReturn(sink);
        sseBroadcaster.send(uuid, new ChatMessage(s));

        Mockito.verify(sink, Mockito.times(1)).next(ServerSentEvent.<String>builder()
                .event(MessageType.CHAT.name())
                .data(Base64.getEncoder().encodeToString(("{\"message\":\"" + s + "\"}").getBytes(StandardCharsets.UTF_8)))
                .build());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Message to invalid sink", "Another invalid message", "Another example"})
    void testSendToInvalidSink(String s) {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);
        sseBroadcaster.addSink(sink);

        UUID uuid = UUID.randomUUID();
        Mockito.when(mockSinkRepository.getSink(uuid)).thenReturn(null);
        sseBroadcaster.send(uuid, new ChatMessage(s));

        Mockito.verify(sink, Mockito.times(0)).next(ServerSentEvent.<String>builder()
                .event(MessageType.CHAT.name())
                .data(Base64.getEncoder().encodeToString(("{\"message\":\"" + s + "\"}").getBytes(StandardCharsets.UTF_8)))
                .build());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Message to be sent", "Hello world!", "Another Example"})
    void testBroadcastWithoutUUIDs(String s) {
        @SuppressWarnings("unchecked")
        List<FluxSink<ServerSentEvent<String>>> sinks = List.of(
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class)
        );

        Mockito.when(mockSinkRepository.getSinks()).thenReturn(sinks);
        sseBroadcaster.broadcast(new ChatMessage(s));

        for(FluxSink<ServerSentEvent<String>> sink : sinks) {
            Mockito.verify(sink, Mockito.times(1)).next(ServerSentEvent.<String>builder()
                    .event(MessageType.CHAT.name())
                    .data(Base64.getEncoder().encodeToString(("{\"message\":\"" + s + "\"}").getBytes(StandardCharsets.UTF_8)))
                    .build());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Message to be sent", "Hello world!", "Another Example"})
    void testBroadcastWithUUIDs(String s) {
        List<UUID> uuids = List.of(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        @SuppressWarnings("unchecked")
        List<FluxSink<ServerSentEvent<String>>> sinks = List.of(
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class)
        );

        Mockito.when(mockSinkRepository.getSinks(uuids)).thenReturn(sinks);
        sseBroadcaster.broadcast(uuids, new ChatMessage(s));

        for(FluxSink<ServerSentEvent<String>> sink : sinks) {
            Mockito.verify(sink, Mockito.times(1)).next(ServerSentEvent.<String>builder()
                    .event(MessageType.CHAT.name())
                    .data(Base64.getEncoder().encodeToString(("{\"message\":\"" + s + "\"}").getBytes(StandardCharsets.UTF_8)))
                    .build());
        }
    }
}
