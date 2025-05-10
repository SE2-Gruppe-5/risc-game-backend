package com.se2gruppe5.risikobackend.sse;

public enum MessageType {
    SET_UUID,
    CHAT,
    JOIN_LOBBY,
    LEAVE_LOBBY,
    START_GAME,
    CHANGE_TERRITORY,
    CHANGE_PHASE,
    UPDATE_PLAYERS,
    CARD_ACTION,
}
