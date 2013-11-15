appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%c{5} %msg%n %ex{0}"
    }
}
root(ERROR, ["STDOUT"])