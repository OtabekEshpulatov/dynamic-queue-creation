package org.example.dynamicqueues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DynamicQueuesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicQueuesApplication.class, args);
    }

}
