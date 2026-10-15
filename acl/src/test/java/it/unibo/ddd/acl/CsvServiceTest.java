package it.unibo.ddd.acl;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/** The same tests run against both implementations, proving they behave the same way. */
class CsvServiceTest {
    @TempDir
    Path dir;

    static Stream<CsvService> services() {
        return Stream.of(CsvService.apacheCommons(',', '"'), CsvService.openCsv(',', '"'));
    }

    private final Table people = Table.withHeaders(
            Row.of("name", "surname", "notes"),
            Row.of("Mario", "Rossi", "likes commas, a lot"),
            Row.of("Luigi", "Verdi", "says \"hello\"")
    );

    @ParameterizedTest
    @MethodSource("services")
    void readsWhatItWrites(CsvService service) throws IOException {
        var file = dir.resolve("people.csv");
        service.write(file, people);
        var read = service.read(file);
        assertEquals(people, read);
        assertEquals(Row.of("name", "surname", "notes"), read.getHeaders());
    }

    @ParameterizedTest
    @MethodSource("services")
    void readsHandWrittenFiles(CsvService service) throws IOException {
        var file = dir.resolve("people.csv");
        Files.writeString(file, """
                name,surname,notes
                Mario,Rossi,"likes commas, a lot"
                Luigi,Verdi,"says ""hello\"\"\"
                """);
        assertEquals(people, service.read(file));
    }

    @ParameterizedTest
    @MethodSource("services")
    void readsWhatTheOtherWrites(CsvService service) throws IOException {
        var file = dir.resolve("people.csv");
        services().filter(other -> other.getClass() != service.getClass()).forEach(other -> {
            try {
                other.write(file, people);
                assertEquals(people, service.read(file));
            } catch (IOException e) {
                fail(e);
            }
        });
    }
}
