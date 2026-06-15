package org.isv.samples.shortener.api;

import lombok.RequiredArgsConstructor;
import org.isv.samples.shortener.service.UrlShortenerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UrlShortenerController {
    private final UrlShortenerService shortenerService;

    @PutMapping("/shortener")
    String createShortUrl(@RequestParam String url) {
        return shortenerService.addUrl(url);
    }

    @GetMapping("/shortener/{id}")
    ResponseEntity<String> getUrl(@PathVariable String id) {
        var url = shortenerService.getOriginalUrl(id);
        return url.<ResponseEntity<String>>map(x -> ResponseEntity.status(302).header("Location", x).build())
                .orElse(ResponseEntity.notFound().build());
    }


}
