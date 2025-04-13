package com.se2gruppe5.risikobackend.sse.services;

import com.google.gson.Gson;
import com.se2gruppe5.risikobackend.sse.Message;
import com.se2gruppe5.risikobackend.sse.repositories.SseSinkRepository;
import com.se2gruppe5.risikobackend.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

@Service
public class SseBroadcastService {
    private final Gson gson = new Gson();
    private final SseSinkRepository sinkRepository;

    @Autowired
    public SseBroadcastService(SseSinkRepository sinkRepository) {
        this.sinkRepository = sinkRepository;
    }

    public UUID addSink(FluxSink<ServerSentEvent<String>> sink) {
        return addSink(IdUtil.generateUuid(sinkRepository::hasSink), sink);
    }

    public UUID addSink(UUID uuid, FluxSink<ServerSentEvent<String>> sink) {
        sinkRepository.addSink(uuid, sink);
        return uuid;
    }

    public boolean hasSink(UUID uuid) {
        return sinkRepository.hasSink(uuid);
    }

    public void removeSink(UUID uuid) {
        sinkRepository.removeSink(uuid);
    }

    public void send(UUID uuid, Message message) {
        send(sinkRepository.getSink(uuid), message);
    }

    public void broadcast(Message message) {
        for (FluxSink<ServerSentEvent<String>> sink : sinkRepository.getSinks()) {
            send(sink, message);
        }
    }

    public void broadcast(Collection<UUID> uuids, Message message) {
        for (FluxSink<ServerSentEvent<String>> sink : sinkRepository.getSinks(uuids)) {
            send(sink, message);
        }
    }

    public void send(FluxSink<ServerSentEvent<String>> sink, Message message) {
        if (sink != null) {
            String data = Base64.getEncoder().encodeToString(gson.toJson(message).getBytes(StandardCharsets.UTF_8));
            sink.next(ServerSentEvent.builder(data)
                    .event(message.getType().name())
                    .build());
        }
    }
}
