package com.spotlight.back.spotlight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Clock;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAsync
public class SpotlightApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotlightApplication.class, args);
	}

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
