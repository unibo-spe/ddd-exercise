package it.unibo.ddd.exercises.money.frankfurter;

import com.sun.net.httpserver.HttpServer;
import it.unibo.ddd.exercises.money.MoneyFactory;
import it.unibo.ddd.exercises.money.impl.MoneyFactoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Runs against a fake Frankfurter server on localhost: no Internet needed, and deterministic responses. */
class FrankfurterExchangeServiceTest {
    private final MoneyFactory money = new MoneyFactoryImpl();
    private final List<URI> requests = new ArrayList<>();
    private HttpServer server;
    private FrankfurterExchangeService service;

    @BeforeEach
    void startFakeServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/v1/", exchange -> {
            requests.add(exchange.getRequestURI());
            var query = exchange.getRequestURI().getQuery();
            var ok = query.equals("base=EUR&symbols=USD");
            var body = (ok
                    ? "{\"amount\":1.0,\"base\":\"EUR\",\"date\":\"2024-01-05\",\"rates\":{\"USD\":1.0921}}"
                    : "{\"message\":\"not found\"}").getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(ok ? 200 : 404, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        var endpoint = URI.create("http://localhost:" + server.getAddress().getPort() + "/v1/");
        service = new FrankfurterExchangeService(HttpClient.newHttpClient(), endpoint, money);
    }

    @AfterEach
    void stopFakeServer() {
        server.stop(0);
    }

    @Test
    void retrievesTheRateOfTheRequestedDay() {
        // a Saturday: the server answers with the rate of the previous Friday
        var rate = service.getExchangeRate(money.eur(), money.usd(), Instant.parse("2024-01-06T15:00:00Z"));
        assertEquals(List.of(URI.create("/v1/2024-01-06?base=EUR&symbols=USD")), requests);
        assertEquals(new BigDecimal("1.0921"), rate.getRate());
        assertEquals(money.eur(), rate.getSource());
        assertEquals(money.usd(), rate.getDestination());
        assertEquals(Instant.parse("2024-01-05T00:00:00Z"), rate.getWhen());
    }

    @Test
    void failsOnErrorResponses() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getExchangeRate(money.gbp(), money.usd(), Instant.parse("2024-01-06T15:00:00Z")));
    }
}
