package it.unibo.ddd.exercises.money.frankfurter;

import com.google.gson.JsonParser;
import it.unibo.ddd.exercises.money.Currency;
import it.unibo.ddd.exercises.money.CurrencyExchangeService;
import it.unibo.ddd.exercises.money.ExchangeRate;
import it.unibo.ddd.exercises.money.MoneyFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Adapter retrieving exchange rates from the European Central Bank, via the free <a href="https://frankfurter.dev">Frankfurter</a> API.
 * Rates are published once per business day: for other days, the most recent previous rate is returned.
 */
public class FrankfurterExchangeService implements CurrencyExchangeService {
    public static final URI DEFAULT_ENDPOINT = URI.create("https://api.frankfurter.dev/v1/");

    private final HttpClient client;
    private final URI endpoint;
    private final MoneyFactory moneyFactory;

    public FrankfurterExchangeService(HttpClient client, URI endpoint, MoneyFactory moneyFactory) {
        this.client = Objects.requireNonNull(client);
        this.endpoint = Objects.requireNonNull(endpoint);
        this.moneyFactory = Objects.requireNonNull(moneyFactory);
    }

    public FrankfurterExchangeService(MoneyFactory moneyFactory) {
        this(HttpClient.newHttpClient(), DEFAULT_ENDPOINT, moneyFactory);
    }

    @Override
    public ExchangeRate getExchangeRate(Currency source, Currency destination, Instant when) {
        var date = LocalDate.ofInstant(when, ZoneOffset.UTC);
        var uri = endpoint.resolve(date + "?base=" + source.getAcronym() + "&symbols=" + destination.getAcronym());
        var body = get(uri);
        // e.g. {"amount":1.0,"base":"EUR","date":"2024-01-05","rates":{"USD":1.0921}}
        // DTO -> domain object: the JSON structure does not leak outside this class
        var json = JsonParser.parseString(body).getAsJsonObject();
        var rate = json.getAsJsonObject("rates").get(destination.getAcronym());
        if (rate == null) {
            throw new IllegalArgumentException("No exchange rate from " + source.getAcronym() + " to " + destination.getAcronym());
        }
        var actualDate = LocalDate.parse(json.get("date").getAsString());
        return moneyFactory.exchangeRate(source, destination, rate.getAsBigDecimal(), actualDate.atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    private String get(URI uri) {
        try {
            var response = client.send(HttpRequest.newBuilder(uri).GET().build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IllegalArgumentException("GET " + uri + " failed with status " + response.statusCode() + ": " + response.body());
            }
            return response.body();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }
}
