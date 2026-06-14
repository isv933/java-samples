package org.isv.samples.shortener.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlShortenerController {
    @PutMapping("/shortener")
    ResponseEntity<String> createShortUrl(@RequestParam String url) {
        return ResponseEntity.status(200).body("http://sample");
    }

}
