package io.github.artsobol.bookcrossing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BookCrossingApplication {

    static void main(String[] args) {
        SpringApplication.run(BookCrossingApplication.class, args);
    }

}
