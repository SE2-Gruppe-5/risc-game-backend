package com.se2gruppe5.risikobackend.sse.controllers;

import com.se2gruppe5.risikobackend.sse.messages.SetUuidMessage;
import com.se2gruppe5.risikobackend.sse.services.SseBroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Controller
@RequestMapping("/sse")
public class SseController {
    private final SseBroadcastService sseBroadcaster;

    @Autowired
    public SseController(SseBroadcastService sseBroadcaster) {
        this.sseBroadcaster = sseBroadcaster;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream() {
        return Flux.create(sink -> {
            UUID uuid = sseBroadcaster.addSink(sink);
            sseBroadcaster.send(sink, new SetUuidMessage(uuid));
        });
    }

    @GetMapping(path = "/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamRejoining(@PathVariable UUID id) {
        if (sseBroadcaster.hasSink(id)) {
            throw new IllegalStateException("UUID already exists");
        }
        return Flux.create(sink -> {
            sseBroadcaster.addSink(id, sink);
            sseBroadcaster.send(sink, new SetUuidMessage(id));
        });
    }
}
