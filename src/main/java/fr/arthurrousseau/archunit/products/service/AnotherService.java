package fr.arthurrousseau.archunit.products.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AnotherService {

    public String echo() {
        log.info("Hello world !");
        return "Hello world !";
    }
}
