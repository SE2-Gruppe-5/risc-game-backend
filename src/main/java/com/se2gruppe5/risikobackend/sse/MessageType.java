package com.se2gruppe5.risikobackend.sse;

public enum MessageType {
    SET_UUID,
    CHAT,
    JOIN_LOBBY,
    LEAVE_LOBBY,
    START_GAME,
    UPDATE_PHASE,
    UPDATE_PLAYERS,
    UPDATE_TERRITORIES,
    ACCUSE_CHEATING,
    PLAYER_WON
}
