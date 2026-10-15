package it.unibo.ddd.acl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/** The same tests run against all implementations, proving they behave the same way (cf. step 2 of acl/walkthrough.md). */
// TODO: remove @Disabled once CsvService has at least one implementation
@Disabled("TODO: not implemented yet")
class CsvServiceTest {
    @TempDir
    Path dir;

    static Stream<CsvService> services() {
        // TODO: return one instance per implementation, created via the static factory methods of CsvService
        return Stream.of();
    }

    private final Table people = Table.withHeaders(
            Row.of("name", "surname", "notes"),
            Row.of("Mario", "Rossi", "likes commas, a lot"),
            Row.of("Luigi", "Verdi", "says \"hello\"")
    );

    @ParameterizedTest
    @MethodSource("services")
    void readsWhatItWrites(CsvService service) {
        // TODO: write people into dir.resolve("people.csv"), read it back, and compare
        fail("TODO");
    }

    @ParameterizedTest
    @MethodSource("services")
    void readsHandWrittenFiles(CsvService service) {
        // TODO: write a CSV file by hand (via Files.writeString), read it, and compare with people
        fail("TODO");
    }

    @ParameterizedTest
    @MethodSource("services")
    void readsWhatTheOtherWrites(CsvService service) {
        // TODO: write with another implementation, read with this one
        fail("TODO");
    }
}
