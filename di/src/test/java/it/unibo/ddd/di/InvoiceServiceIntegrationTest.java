package it.unibo.ddd.di;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test: real implementations, yet lightweight and isolated ones,
 * i.e. a throw-away file, and a fake exchange-rate server on localhost.
 */
class InvoiceServiceIntegrationTest {
    @TempDir
    Path dir;

    private HttpServer server;
    private final List<String> requests = new ArrayList<>();

    @BeforeEach
    void startFakeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/v1/", exchange -> {
            requests.add(exchange.getRequestURI().toString());
            var body = "{\"amount\":1.0,\"base\":\"EUR\",\"date\":\"2026-01-15\",\"rates\":{\"USD\":1.1}}"
                    .getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
    }

    @AfterEach
    void stopFakeServer() {
        server.stop(0);
    }

    @Test
    void invoicesSurviveRestarts() {
        var endpoint = URI.create("http://localhost:" + server.getAddress().getPort() + "/v1/");
        var clock = Clock.fixed(Instant.parse("2026-01-15T10:00:00Z"), ZoneOffset.UTC);
        var file = dir.resolve("invoices.csv");
        var cart = Map.of("book", new BigDecimal("20.00"));

        var first = new InvoiceService(new FrankfurterExchangeRates(HttpClient.newHttpClient(), endpoint),
                new FileInvoiceArchive(file), invoice -> { }, clock);
        var issued = first.issue("Mario Rossi", cart, "USD");
        assertEquals(new BigDecimal("22.00"), issued.total());
        assertEquals(List.of("/v1/2026-01-15?base=EUR&symbols=USD"), requests);

        // a new service on the same file, as if the application was restarted
        var second = new InvoiceService((from, to, date) -> BigDecimal.ONE, new FileInvoiceArchive(file), invoice -> { }, clock);
        assertEquals(2, second.issue("Luigi Verdi", cart, "EUR").number());
        assertEquals(issued, new FileInvoiceArchive(file).getAll().get(0));
    }
}
