package json.tokenizer;

public class TokenError extends Error {

    private int line;
    private int column;
    private String cause;

    public TokenError(String cause, int line, int column) {
        super(cause);
        this.cause = cause;
        this.line = line;
        this.column = column;
    }

    public String getMessage() {
        return "Error in line " + this.line + " at character " + this.column + ": " + this.cause;
    }

    private static final long serialVersionUID = 234L;
}