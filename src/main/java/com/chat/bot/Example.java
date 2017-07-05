package com.chat.bot;

/**
 * @author Manoj Janaka
 * @version 1.0
 * @since 7/4/17
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Example {
    private static final Logger logger = LoggerFactory.getLogger(Example.class);

    @RequestMapping("/")
    String home() {

        return "Hello World!";
    }

}