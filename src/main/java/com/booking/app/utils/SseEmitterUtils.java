package com.booking.app.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@UtilityClass
public class SseEmitterUtils {

    public void send(SseEmitter emitter) throws IOException {
        SseEmitter.SseEventBuilder event = SseEmitter.event();
        event.id("10").name("ticket").data("blaaa").comment("commentss");
        emitter.send(event);
    }
}
