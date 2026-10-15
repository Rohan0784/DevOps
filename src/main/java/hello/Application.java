package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Starts the sample Spring greeting service. */
@SpringBootApplication
public class Application {
    /** Start the Spring application. */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
