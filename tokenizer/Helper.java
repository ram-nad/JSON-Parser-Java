package json.tokenizer;

public class Helper {
    public final static char DOUBLE_QUOTE = '\"';

    public final static char COLON = ':';

    public final static char COMMA = ',';

    public final static char SLASH = '\\';

    public final static char NEWLINE = '\n';

    public final static char SQUARE_BRACKET_OPEN = '[';

    public final static char SQUARE_BRACKET_CLOSE = ']';

    public final static char CURLY_BRACKETS_OPEN = '{';

    public final static char CURLY_BRACKETS_CLOSE = '}';

    public final static char NULL_CHAR = '\0';

    public final static char DOT = '.';

    public static boolean isWhiteSpace(char character) {
        if (character == ' ') {
            return true;
        } else if (character == '\t') {
            return true;
        } else if (character == '\n') {
            return true;
        } else if (character == '\r') {
            return true;
        }
        return false;
    }

    public static boolean isDigit(char character) {
        if (character >= '0' && character <= '9') {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isLetter(char character) {
        if (character >= 'a' && character <= 'z') {
            return true;
        } else {
            return false;
        }
    }
}