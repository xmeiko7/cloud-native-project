package org.example.cloudnativeproject;

import io.github.bucket4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@RestController
public class GreetController {

    private final Bucket bucket;
    public GreetController() {
        Bandwidth limit = Bandwidth.classic(100, Refill.greedy(100, Duration.ofSeconds(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/greet")
    public ResponseEntity<Map<String, String>> hello() {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(Collections.singletonMap("msg", "hello"));
        } else {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }
}