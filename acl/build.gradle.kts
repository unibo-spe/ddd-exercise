dependencies {
    // `implementation` (not `api`): library types must not leak to the users of this module,
    // which is the whole point of an anti-corruption layer
    // https://www.baeldung.com/apache-commons-csv
    implementation("org.apache.commons:commons-csv:1.14.1")
    // https://www.baeldung.com/opencsv
    implementation("com.opencsv:opencsv:5.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
}
