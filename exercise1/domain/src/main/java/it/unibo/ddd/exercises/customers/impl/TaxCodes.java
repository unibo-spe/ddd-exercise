package it.unibo.ddd.exercises.customers.impl;

import it.unibo.ddd.exercises.customers.Gender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Computation of Italian tax codes (codice fiscale), cf. https://it.wikipedia.org/wiki/Codice_fiscale.
 */
// ponytail: Italian municipalities only, no foreign countries (Z... codes) nor omocodia
final class TaxCodes {
    private static final String MONTHS = "ABCDEHLMPRST";
    // values of characters in odd positions, for 0..9 and A..Z alike
    private static final int[] ODD = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 16, 10, 22, 25, 24, 23};
    private static final Pattern PLACE_WITH_PROVINCE = Pattern.compile("(.+?)\\s*\\((\\w{2})\\)");
    private static final Map<String, String> CADASTRAL_CODES = loadCadastralCodes();

    private TaxCodes() {
    }

    static String compute(String name, String surname, LocalDate birthDate, Gender gender, String birthPlace) {
        Objects.requireNonNull(birthDate);
        Objects.requireNonNull(gender);
        var code = surnamePart(letters(surname))
                + namePart(letters(name))
                + "%02d".formatted(birthDate.getYear() % 100)
                + MONTHS.charAt(birthDate.getMonthValue() - 1)
                + "%02d".formatted(birthDate.getDayOfMonth() + (gender == Gender.FEMALE ? 40 : 0))
                + cadastralCode(birthPlace);
        return code + controlChar(code);
    }

    private static String letters(String string) {
        var withoutAccents = Normalizer.normalize(string, Normalizer.Form.NFD);
        var result = withoutAccents.replaceAll("[^A-Za-z]", "").toUpperCase(Locale.ROOT);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("Not a valid name: " + string);
        }
        return result;
    }

    private static String consonants(String letters) {
        return letters.replaceAll("[AEIOU]", "");
    }

    private static String vowels(String letters) {
        return letters.replaceAll("[^AEIOU]", "");
    }

    private static String firstThree(String letters) {
        return (letters + "XXX").substring(0, 3);
    }

    private static String surnamePart(String surname) {
        return firstThree(consonants(surname) + vowels(surname));
    }

    private static String namePart(String name) {
        var consonants = consonants(name);
        if (consonants.length() >= 4) {
            return "" + consonants.charAt(0) + consonants.charAt(2) + consonants.charAt(3);
        }
        return firstThree(consonants + vowels(name));
    }

    private static char controlChar(String code) {
        var sum = 0;
        for (var i = 0; i < code.length(); i++) {
            var c = code.charAt(i);
            var value = Character.isDigit(c) ? c - '0' : c - 'A';
            sum += i % 2 == 0 ? ODD[value] : value; // i is 0-based, positions in the spec are 1-based
        }
        return (char) ('A' + sum % 26);
    }

    private static String key(String name, String province) {
        return (name.trim() + (province == null ? "" : "|" + province)).toLowerCase(Locale.ROOT);
    }

    static String cadastralCode(String birthPlace) {
        var matcher = PLACE_WITH_PROVINCE.matcher(birthPlace.trim());
        var key = matcher.matches() ? key(matcher.group(1), matcher.group(2)) : key(birthPlace, null);
        var code = CADASTRAL_CODES.get(key);
        if (code == null) {
            throw new IllegalArgumentException("Unknown or ambiguous municipality: " + birthPlace);
        }
        return code;
    }

    /** Reads `name;province;code` lines, made out of the list of Italian municipalities by ISTAT. */
    private static Map<String, String> loadCadastralCodes() {
        var codes = new HashMap<String, String>();
        var ambiguous = new HashSet<String>();
        var resource = TaxCodes.class.getResourceAsStream("municipalities.csv");
        try (var reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resource), StandardCharsets.UTF_8))) {
            reader.lines().filter(line -> !line.startsWith("#")).forEach(line -> {
                var fields = line.split(";");
                codes.put(key(fields[0], fields[1]), fields[2]);
                if (codes.put(key(fields[0], null), fields[2]) != null) {
                    ambiguous.add(key(fields[0], null));
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        ambiguous.forEach(codes::remove); // e.g. "Castro" must be qualified: "Castro (BG)" or "Castro (LE)"
        return Map.copyOf(codes);
    }
}
