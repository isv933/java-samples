package org.isv.samples.shortener.load;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Tag("load")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-clickhouse-test.properties")
@Slf4j
public class ServiceLoadTest {
    @LocalServerPort
    private int servicePort;
    @Value("${num-threads}")
    private int numThreads;
    @Value("${num-urls}")
    private int numUrls;
    private String baseUrl;
    private ExecutorService executorService;
    private volatile boolean stopped;
    private final AtomicLong counter = new AtomicLong(0);

    @BeforeEach
    public void init(){
        executorService = Executors.newFixedThreadPool(numThreads);
        baseUrl = String.format("http://localhost:%d/shortener", servicePort);
        stopped = false;
    }
    @AfterEach
    public void close(){
        executorService.shutdownNow();
        stopped  = true;
    }

    @Test
    public void createUrls() {
        var startTime = System.nanoTime();
        var tasks = Stream.generate(() -> CompletableFuture.runAsync(this::runLoad,executorService))
                            .limit(numThreads).toList();

        var speedTask = CompletableFuture.runAsync(()->{
            while(!stopped) {
                try {
                    Thread.sleep(Duration.ofMinutes(2).toMillis());
                    var urls = counter.get();
                    log.info("Processed {} urls, speed = {} url/sec", urls,
                            (double) urls * 1000_000_000/(
                                    (System.nanoTime() - startTime)));
                }
                catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        });

        tasks.forEach(CompletableFuture::join);
        speedTask.join();

        log.info("Finished {} urls in {} threads in {} time", numUrls, numThreads,
                            Duration.ofNanos(System.nanoTime()-startTime));

    }

    private void runLoad() {
        var myUrls = numUrls / numThreads;
        var client = RestClient.builder().baseUrl(baseUrl).build();

        for(var i = 0;  i < myUrls && !stopped; i++) {
            var url = "http://" + UUID.randomUUID() + ".com";

            var result =
                    client.put().uri(uriBuilder ->
                            uriBuilder.queryParam("url", url)
                                    .build()).retrieve().toBodilessEntity();

            if (result.getStatusCode() != HttpStatus.OK) {
                log.error("failed put url {}", url);
            }
            counter.incrementAndGet();
        }

    }
}
