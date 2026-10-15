package it.unibo.ddd.di;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Objects;

/** Exchange rates from the European Central Bank, via <a href="https://frankfurter.dev">Frankfurter</a>. */
public class FrankfurterExchangeRates implements ExchangeRates {
    public static final URI DEFAULT_ENDPOINT = URI.create("https://api.frankfurter.dev/v1/");

    private final HttpClient client;
    private final URI endpoint;

    public FrankfurterExchangeRates(HttpClient client, URI endpoint) {
        this.client = Objects.requireNonNull(client);
        this.endpoint = Objects.requireNonNull(endpoint);
    }

    @Override
    public BigDecimal rate(String from, String to, LocalDate date) {
        var request = HttpRequest.newBuilder(endpoint.resolve(date + "?base=" + from + "&symbols=" + to)).build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("Cannot convert " + from + " to " + to + ": " + response.body());
            }
            return JsonParser.parseString(response.body()).getAsJsonObject()
                    .getAsJsonObject("rates").get(to).getAsBigDecimal();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
