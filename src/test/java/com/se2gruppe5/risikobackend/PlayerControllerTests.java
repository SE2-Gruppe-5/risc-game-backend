package com.se2gruppe5.risikobackend;

import com.se2gruppe5.risikobackend.PlayerControllers.PlayerController;
import com.se2gruppe5.risikobackend.PlayerControllers.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }

    @Test
    void testActivatePlayer() throws Exception {

        mockMvc.perform(post("/players/activate")
                        .param("playerName", "Player1"))
                .andExpect(status().isOk());
    }

    @Test
    void testActivatePlayer_Fail() throws Exception {
        doThrow(new IllegalArgumentException("Invalid player name")).when(playerService).addActivePlayer("Player1");

        mockMvc.perform(post("/players/activate")
                        .param("playerName", "Player1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testStreamActivePlayers() throws Exception {
        SseEmitter mockEmitter = mock(SseEmitter.class);
        when(playerService.registerClient()).thenReturn(mockEmitter);

        mockMvc.perform(get("/players/stream"))
                .andExpect(status().isOk());
    }
}