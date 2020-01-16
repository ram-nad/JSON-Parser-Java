package json.value;

public class JSONError extends Error {

    public JSONError(String cause) {
        super(cause);
    }

    private static final long serialVersionUID = 235L;
}