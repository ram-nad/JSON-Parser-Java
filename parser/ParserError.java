package json.parser;

public class ParserError extends Error {

    public ParserError(String cause) {
        super(cause);
    }

    private static final long serialVersionUID = 235L;
}