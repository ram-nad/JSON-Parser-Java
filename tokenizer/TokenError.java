package json.tokenizer;

public class TokenError extends Error {

    private int line;
    private int column;

    public TokenError(String cause, int line, int column) {
        super(cause);
        this.line = line;
        this.column = column;
    }

    @Override
    public String getMessage() {
        return "Error in line " + this.line + " at character " + this.column + ": " + super.getMessage();
    }

    private static final long serialVersionUID = 234L;
}