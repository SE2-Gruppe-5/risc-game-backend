package com.se2gruppe5.risikobackend.sse.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SseSinkRepositoryImplUnitTest {
    SseSinkRepository sinkRepository;

    @BeforeEach
    void setup() {
        sinkRepository = new SseSinkRepositoryImpl();
    }

    @Test
    void testHasSinkAfterAdd() {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        sinkRepository.addSink(uuid, sink);

        assertTrue(sinkRepository.hasSink(uuid));
    }

    @Test
    void testRemoveSink() {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        sinkRepository.addSink(uuid, sink);
        sinkRepository.removeSink(uuid);

        assertFalse(sinkRepository.hasSink(uuid));
    }

    @Test
    void testGetSink() {
        @SuppressWarnings("unchecked")
        FluxSink<ServerSentEvent<String>> sink = Mockito.mock(FluxSink.class);

        UUID uuid = UUID.randomUUID();
        sinkRepository.addSink(uuid, sink);

        FluxSink<ServerSentEvent<String>> returnedSink = sinkRepository.getSink(uuid);
        assertNotNull(returnedSink);
        assertSame(sink, sinkRepository.getSink(uuid));
    }

    @Test
    void testGetInvalidSink() {
        UUID uuid = UUID.randomUUID();
        FluxSink<ServerSentEvent<String>> returnedSink = sinkRepository.getSink(uuid);
        assertNull(returnedSink);
    }

    @Test
    void testGetSinksWithoutUUIDs() {
        @SuppressWarnings("unchecked")
        List<FluxSink<ServerSentEvent<String>>> sinks = List.of(
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class)
        );

        for (FluxSink<ServerSentEvent<String>> sink : sinks) {
            sinkRepository.addSink(UUID.randomUUID(), sink);
        }

        List<FluxSink<ServerSentEvent<String>>> returnedSinks = sinkRepository.getSinks();

        assertNotSame(sinks, returnedSinks);
        assertTrue(listsHaveSameElements(sinks, returnedSinks));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 3",
            "0, 2",
            "0, 1",
            "1, 3",
            "1, 2",
            "2, 3"
    })
    void testGetSinksWithUUIDs(int from, int to) {
        @SuppressWarnings("unchecked")
        List<FluxSink<ServerSentEvent<String>>> sinks = List.of(
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class),
                Mockito.mock(FluxSink.class)
        );

        List<UUID> uuids = List.of(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        for (int i = 0; i < uuids.size(); i++) {
            sinkRepository.addSink(uuids.get(i), sinks.get(i));
        }

        List<FluxSink<ServerSentEvent<String>>> returnedSubsetSinks = sinkRepository.getSinks(uuids.subList(from, to));
        List<FluxSink<ServerSentEvent<String>>> subsetSinks = sinks.subList(from, to);

        assertNotSame(subsetSinks, returnedSubsetSinks);
        assertTrue(listsHaveSameElements(subsetSinks, returnedSubsetSinks));
    }

    // A simple equals would also check the order of the list elements
    // However, the order does not matter in these tests
    private <E> boolean listsHaveSameElements(List<E> list1, List<E> list2) {
        for(E el : list1) {
            if(!list2.contains(el)) {
                return false;
            }
        }

        for(E el : list2) {
            if(!list1.contains(el)) {
                return false;
            }
        }

        return true;
    }
}
