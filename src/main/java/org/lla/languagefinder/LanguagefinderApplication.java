package org.lla.languagefinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootApplication
public class LanguagefinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(LanguagefinderApplication.class, args);


    }

}
