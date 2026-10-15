package it.unibo.ddd.di;

import com.google.gson.JsonParser;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

/**
 * Issues invoices for carts whose prices are expressed in EUR, converting totals in the customer's currency.
 */
public class InvoiceService {
    public Invoice issue(String customer, Map<String, BigDecimal> pricesInEur, String currency) {
        var today = LocalDate.now();
        var total = pricesInEur.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (!currency.equals("EUR")) {
            total = total.multiply(fetchRate(currency, today));
        }
        var archive = InvoiceArchive.getInstance();
        var invoice = new Invoice(
                archive.getAll().size() + 1L,
                customer,
                total.setScale(2, RoundingMode.HALF_EVEN),
                currency,
                today,
                today.plusDays(30)
        );
        archive.store(invoice);
        System.out.println("Dear " + customer + ", here is your invoice: " + invoice);
        return invoice;
    }

    private BigDecimal fetchRate(String currency, LocalDate date) {
        var client = HttpClient.newHttpClient();
        var uri = URI.create("https://api.frankfurter.dev/v1/" + date + "?base=EUR&symbols=" + currency);
        try {
            var body = client.send(HttpRequest.newBuilder(uri).build(), HttpResponse.BodyHandlers.ofString()).body();
            return JsonParser.parseString(body).getAsJsonObject().getAsJsonObject("rates").get(currency).getAsBigDecimal();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
