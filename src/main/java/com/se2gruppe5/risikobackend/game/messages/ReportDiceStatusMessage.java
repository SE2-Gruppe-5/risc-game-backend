package com.se2gruppe5.risikobackend.game.messages;

import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.MessageType;

import java.util.List;

public record ReportDiceStatusMessage(List<Integer> results) implements Message {
    @Override
    public MessageType getType() {
        return MessageType.REPORT_DICE_STATUS;
    }
}
