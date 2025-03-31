package com.se2gruppe5.risikobackend.sse.controllers;

import com.se2gruppe5.risikobackend.sse.SseBroadcaster;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
public class SseController {
    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream() {
        return Flux.create(SseBroadcaster::addSink);
    }
}
