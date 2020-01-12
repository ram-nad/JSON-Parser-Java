package json.value;

public class JSONError extends Error {
    private String cause;

    public JSONError(String cause) {
        super(cause);
        this.cause = cause;
    }

    public String getMessage() {
        return this.cause;
    }

    private static final long serialVersionUID = 235L;
}