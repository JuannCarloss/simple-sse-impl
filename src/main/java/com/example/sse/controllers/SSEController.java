package com.example.sse.controllers;

import com.example.sse.models.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController("/sse")
public class SSEController {

    private final HashMap<String, List<SseEmitter>> emitters = new HashMap<>();

    @GetMapping("/subscribe/{range}")
    public SseEmitter getEvents(@PathVariable("range") String range){

        SseEmitter emitter = new SseEmitter(0L);
        emitters.computeIfAbsent(range, key -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(range));
        emitter.onTimeout(() -> this.emitters.remove(range));
        emitter.onError(throwable -> this.emitters.remove(range));

        return emitter;
    }

    @PostMapping("/send/{range}")
    @ResponseStatus(HttpStatus.CREATED)
    public void sendEvent(@PathVariable("range")String range) {
        List<SseEmitter> sseEmitters = emitters.getOrDefault(range, Collections.emptyList());
        Message message = new Message();

        String uuid = UUID.randomUUID().toString();

        message.setUuid(uuid);
        message.setMessage("Mensage uuid: " + uuid);

        sseEmitters.parallelStream().forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event().data(message));
            }catch (IOException e){
                emitter.completeWithError(e);
                emitters.get(range).remove(emitter);
            }
        });
    }
}
