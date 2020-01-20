package json.parser;

public class ParserError extends Error {

    ParserError(String cause) {
        super(cause);
    }

    private static final long serialVersionUID = 235L;
}