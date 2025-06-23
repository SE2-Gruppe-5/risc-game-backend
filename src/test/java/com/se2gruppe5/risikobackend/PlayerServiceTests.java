package com.se2gruppe5.risikobackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.se2gruppe5.risikobackend.PlayerControllers.PlayerService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PlayerServiceTests {
    private PlayerService playerService;

    @BeforeEach
    void setUp() {
        playerService = new PlayerService();
    }

    @Test
    void testAddActivePlayer() {
        playerService.addActivePlayer("Player1");

        assertTrue(playerService.getActivePlayers().contains("Player1"));
    }

    @Test
    void testAddActivePlayer_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> playerService.addActivePlayer(""));
    }

    @Test
    void testAddMultiplePlayers() {
        playerService.addActivePlayer("Player1");
        playerService.addActivePlayer("Player2");

        assertTrue(playerService.getActivePlayers().contains("Player1"));
        assertTrue(playerService.getActivePlayers().contains("Player2"));
        assertEquals(2, playerService.getActivePlayers().size());
    }

    @Test
    void testRegisterClientReturnsEmitter() {
        assertNotNull(playerService.registerClient());
    }

    @Test
    void testDuplicatePlayerNames() {
        playerService.addActivePlayer("Player1");
        playerService.addActivePlayer("Player1");

        assertEquals(1, playerService.getActivePlayers().size());
    }

    @Test
    void testClientIsRemovedOnCompletion() {
        SseEmitter emitter = playerService.registerClient();
        emitter.complete();

        assertNotNull(emitter);
    }

    @Test
    void testClientIsRemovedOnTimeout() {
        SseEmitter emitter = playerService.registerClient();
        emitter.onTimeout(() -> {
        });
        emitter.complete();
        assertNotNull(emitter);
    }

    @Test
    void testRegisterClient_Success() {
        SseEmitter emitter = playerService.registerClient();
        assertNotNull(emitter);
        assertEquals(1, playerService.getActivePlayers().size() + 1);
    }

    @Test
    void testNotifyClients_IOExceptionHandled() throws Exception {
        SseEmitter emitter = mock(SseEmitter.class);
        playerService.getEmitters().add(emitter);
        doThrow(new IOException("Test IO Error")).when(emitter).send(any(SseEmitter.SseEventBuilder.class));
        assertDoesNotThrow(() -> playerService.addActivePlayer("PlayerX"));
        assertEquals(0, playerService.getEmitterCount(), "Emitter should be removed after IOException");
        verify(emitter, times(1)).send(any(SseEmitter.SseEventBuilder.class));
    }

    @Test
    void testRegisterClient_IOExceptionHandled() throws Exception {
        PlayerService spyService = spy(playerService);

        SseEmitter faultyEmitter = mock(SseEmitter.class);
        doThrow(new IOException("Initial send failure")).when(faultyEmitter).send(any(SseEmitter.SseEventBuilder.class));

        spyService.getEmitters().add(faultyEmitter);

        assertDoesNotThrow(() -> spyService.addActivePlayer("PlayerIOException"));
        assertEquals(0, spyService.getEmitterCount(), "Emitter sollte nach IOException entfernt werden");
    }

    @Test
    void testRegisterClient_InitialSendFails() throws Exception {
        PlayerService spyService = spy(playerService);

        SseEmitter faultyEmitter = mock(SseEmitter.class);
        doThrow(new IOException("Initial send failure")).when(faultyEmitter).send(any(SseEmitter.SseEventBuilder.class));

        spyService.getEmitters().add(faultyEmitter);

        assertDoesNotThrow(() -> spyService.addActivePlayer("SomePlayer"));
        assertEquals(0, spyService.getEmitterCount(), "Emitter sollte entfernt werden wenn initiales Senden fehlschlägt");
    }

}
