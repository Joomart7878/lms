package com.joomart.lms.dem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoTest {
        @GetMapping("/hello")
        public String sayHello() {
            return "Hello, World!!!";
        }
    }

