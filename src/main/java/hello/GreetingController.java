package hello;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Provides the sample greeting REST endpoint. */
@RestController
public class GreetingController {
    /** Template used to create greeting messages. */
    private static final String TEMPLATE = "Hello, %s!";
    /** Thread-safe counter for greeting identifiers. */
    private final AtomicLong counter = new AtomicLong();

    /** Return a greeting for the supplied name. */
    @GetMapping("/greeting")
    public Greeting greeting(
            @RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(
                counter.getAndIncrement(),
                String.format(TEMPLATE, name));
    }
}
